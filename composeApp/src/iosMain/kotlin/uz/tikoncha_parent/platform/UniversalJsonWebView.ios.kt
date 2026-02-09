package uz.tikoncha_parent.platform

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.UIKitView
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.ObjCSignatureOverride
import kotlinx.cinterop.readValue
import platform.CoreGraphics.CGRectZero
import platform.Foundation.NSError
import platform.Foundation.NSURL
import platform.Foundation.NSURLRequest
import platform.WebKit.*
import platform.darwin.NSObject

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun UniversalJsonWebView(
    url: String,
    json: String?,
    onIncomingJson: (String?) -> Unit,
    modifier: Modifier,
    onBackPressed: () -> Unit
) {
    var isPageLoaded by remember { mutableStateOf(false) }
    var lastLoadedUrl by remember { mutableStateOf<String?>(null) }
    var lastSentJson by remember { mutableStateOf<String?>(null) }

    val latestOnIncomingJson by rememberUpdatedState(onIncomingJson)
    val latestOnBackPressed by rememberUpdatedState(onBackPressed)

    val messageHandler = remember {
        IOSJsonMessageHandler(
            onJsonFromWeb = { latestOnIncomingJson(it) },
            onWebBackPressed = { latestOnBackPressed() }
        )
    }

    val delegate = remember {
        object : NSObject(), WKNavigationDelegateProtocol {

            override fun webView(webView: WKWebView, didFinishNavigation: WKNavigation?) {
                Logger.d("UniversalJsonWebView", "✅ didFinishNavigation")
                isPageLoaded = true

                webView.evaluateJavaScript("document.readyState") { r, e ->
                    Logger.d(
                        "UniversalJsonWebView",
                        "readyState=$r err=${e?.localizedDescription}"
                    )
                }
            }

            @ObjCSignatureOverride
            override fun webView(
                webView: WKWebView,
                didFailProvisionalNavigation: WKNavigation?,
                withError: NSError
            ) {
                Logger.e(
                    "UniversalJsonWebView",
                    "❌ didFailProvisionalNavigation: ${withError.domain}(${withError.code}) ${withError.localizedDescription}"
                )
                isPageLoaded = false
            }

            @ObjCSignatureOverride
            override fun webView(
                webView: WKWebView,
                didFailNavigation: WKNavigation?,
                withError: NSError
            ) {
                Logger.e(
                    "UniversalJsonWebView",
                    "❌ didFailNavigation: ${withError.domain}(${withError.code}) ${withError.localizedDescription}"
                )
                isPageLoaded = false
            }
        }
    }

    UIKitView(
        modifier = modifier,
        factory = {
            val controller = WKUserContentController()

            val bridgeJs = """
                (function () {
                    if (window.__KMP_BRIDGE__) return;
                    window.__KMP_BRIDGE__ = true;

                    // Android-like bridge object
                    if (!window.AndroidJson) {
                        window.AndroidJson = {
                            sendData: function (payload) {
                                try {
                                    window.webkit.messageHandlers.AndroidJson.postMessage({
                                        type: "sendData",
                                        payload: payload
                                    });
                                } catch (e) {}
                            },
                            backPressed: function () {
                                try {
                                    window.webkit.messageHandlers.AndroidJson.postMessage({
                                        type: "backPressed"
                                    });
                                } catch (e) {}
                            }
                        };
                    }

                    // ---- JS error + promise forwarding (for debug) ----
                    window.addEventListener('error', function(e){
                        try {
                            window.webkit.messageHandlers.AndroidJson.postMessage({
                                type: 'log',
                                payload: 'JSERR: ' + (e.message || e)
                            });
                        } catch(_) {}
                    });

                    window.addEventListener('unhandledrejection', function(e){
                        try {
                            var msg = (e && e.reason) ? (e.reason.message || String(e.reason)) : 'unknown';
                            window.webkit.messageHandlers.AndroidJson.postMessage({
                                type: 'log',
                                payload: 'PROMISE: ' + msg
                            });
                        } catch(_) {}
                    });

                    // Optional: forward console.error too
                    var oldErr = console.error;
                    console.error = function(){
                        try {
                            window.webkit.messageHandlers.AndroidJson.postMessage({
                                type: 'log',
                                payload: 'ERR: ' + Array.prototype.join.call(arguments, ' ')
                            });
                        } catch(_) {}
                        try { oldErr.apply(console, arguments); } catch(_) {}
                    };
                })();
            """.trimIndent()

            controller.addUserScript(
                WKUserScript(
                    source = bridgeJs,
                    injectionTime = WKUserScriptInjectionTime.WKUserScriptInjectionTimeAtDocumentStart,
                    forMainFrameOnly = true
                )
            )
            controller.addScriptMessageHandler(messageHandler, name = "AndroidJson")

            val config = WKWebViewConfiguration().apply {
                userContentController = controller

                // ✅ chat/auth uchun muhim: cookie + localStorage persistent bo‘lsin
                websiteDataStore = WKWebsiteDataStore.defaultDataStore()

                // ✅ JS enable (iOS 14+)
                defaultWebpagePreferences.allowsContentJavaScript = true
                preferences.javaScriptCanOpenWindowsAutomatically = true
            }

            WKWebView(
                frame = CGRectZero.readValue(),
                configuration = config
            ).apply {
                navigationDelegate = delegate

                // (ixtiyoriy) WAF/browser-detection muammosi bo‘lsa sinab ko‘ring:
                // customUserAgent =
                //   "Mozilla/5.0 (iPhone; CPU iPhone OS 17_0 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/17.0 Mobile/15E148 Safari/604.1"
            }
        },
        update = { webView ->
            // URL faqat o‘zgarganda load
            if (lastLoadedUrl != url) {
                lastLoadedUrl = url
                lastSentJson = null
                isPageLoaded = false

                val nsUrl = NSURL.URLWithString(url)
                if (nsUrl != null) {
                    Logger.d("UniversalJsonWebView", "load url=$url")
                    webView.loadRequest(NSURLRequest.requestWithURL(nsUrl))
                } else {
                    Logger.e("UniversalJsonWebView", "Invalid URL: $url")
                }
            }

            // Native -> Web JSON (safe + retry: chat SPA boot timing)
            if (isPageLoaded && json != null && lastSentJson != json) {
                lastSentJson = json

                val escaped = escapeJsString(json)

                val script = """
                    (function(){
                      try {
                        var raw = '$escaped';
                        var tries = 0;

                        function sendNow(){
                          try {
                            var p = JSON.parse(raw);

                            // Safari: targetOrigin kerak bo‘lishi mumkin
                            try { window.postMessage(p, '*'); } catch(e) {}

                            // Ba’zi web’lar message event tinglaydi
                            try { window.dispatchEvent(new MessageEvent('message', { data: p })); } catch(e) {}

                            // Debug uchun
                            try { window.__NATIVE_PAYLOAD__ = p; } catch(e) {}
                          } catch(e) {}
                        }

                        // immediate + retry ~3s
                        sendNow();
                        var t = setInterval(function(){
                          tries++;
                          var ready = (document.readyState === 'complete' || document.readyState === 'interactive');
                          if (ready) sendNow();
                          if (tries >= 20) clearInterval(t);
                        }, 150);
                      } catch(e) {}
                    })();
                """.trimIndent()

                Logger.d("UniversalJsonWebView", "send payload len=${json.length}")
                webView.evaluateJavaScript(script, completionHandler = null)
            }
        },
        onRelease = { webView ->
            webView.navigationDelegate = null
            webView.configuration.userContentController
                .removeScriptMessageHandlerForName("AndroidJson")
        }
    )
}

private class IOSJsonMessageHandler(
    private val onJsonFromWeb: (String?) -> Unit,
    private val onWebBackPressed: () -> Unit
) : NSObject(), WKScriptMessageHandlerProtocol {

    override fun userContentController(
        userContentController: WKUserContentController,
        didReceiveScriptMessage: WKScriptMessage
    ) {
        val body = didReceiveScriptMessage.body
        val dict = body as? Map<*, *> ?: run {
            onJsonFromWeb(body?.toString())
            return
        }

        when (dict["type"]?.toString()) {
            "sendData" -> onJsonFromWeb(dict["payload"]?.toString())
            "backPressed" -> onWebBackPressed()
            "log" -> Logger.e("WEB", dict["payload"]?.toString() ?: "")
            else -> Unit
        }
    }
}

private fun escapeJsString(s: String): String =
    s.replace("\\", "\\\\")
        .replace("'", "\\'")
        .replace("\n", "\\n")
        .replace("\r", "\\r")
        .replace("\u2028", "\\u2028")
        .replace("\u2029", "\\u2029")
