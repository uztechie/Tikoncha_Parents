package uz.tikoncha_parent.platform

import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.interop.UIKitView
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.readValue
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

    // ✅ faqat url param o‘zgarganda load qilish uchun
    var lastLoadedUrl by remember { mutableStateOf<String?>(null) }

    // ✅ bir xil jsonni qayta-qayta yubormaslik uchun
    var lastSentJson by remember { mutableStateOf<String?>(null) }

    val latestOnIncomingJson by rememberUpdatedState(onIncomingJson)
    val latestOnBackPressed by rememberUpdatedState(onBackPressed)

    val messageHandler = remember {
        IOSJsonMessageHandler(
            onJsonFromWeb = { latestOnIncomingJson(it) },
            onWebBackPressed = { latestOnBackPressed() }
        )
    }

    LaunchedEffect(json) {
        Logger.d("UniversalJsonWebView", "json=$json")
    }


    val delegate = remember {
        object : NSObject(), WKNavigationDelegateProtocol {
            override fun webView(webView: WKWebView, didFinishNavigation: WKNavigation?) {
                isPageLoaded = true
            }
        }
    }


    UIKitView(
        modifier = modifier.fillMaxSize(),
        factory = {
            val controller = WKUserContentController()

            val bridgeJs = """
(function () {

    // faqat bir marta o‘rnatsin
    if (window.__KMP_BRIDGE_INSTALLED__) return;
    window.__KMP_BRIDGE_INSTALLED__ = true;

    // ===== AndroidJson bridge =====
    window.AndroidJson = window.AndroidJson || {
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

    // ===== CONSOLE.LOG FORWARD =====
    try {
        function send(level, args) {
            var msg = Array.prototype.map.call(args, function (a) {
                if (typeof a === "string") return a;
                try { return JSON.stringify(a); }
                catch(e) { return String(a); }
            }).join(" ");

            window.webkit.messageHandlers.AndroidJson.postMessage({
                type: "console",
                level: level,
                payload: msg
            });
        }

        var _log = console.log;
        console.log = function () {
            send("log", arguments);
            try { _log.apply(console, arguments); } catch(e) {}
        };

        var _warn = console.warn;
        console.warn = function () {
            send("warn", arguments);
            try { _warn.apply(console, arguments); } catch(e) {}
        };

        var _error = console.error;
        console.error = function () {
            send("error", arguments);
            try { _error.apply(console, arguments); } catch(e) {}
        };

        window.addEventListener("error", function (e) {
            send("js_error", [e.message || e]);
        });

        window.addEventListener("unhandledrejection", function (e) {
            var reason = e && e.reason ? (e.reason.message || String(e.reason)) : "unknown";
            send("promise_rejection", [reason]);
        });

    } catch (e) {}

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
            }

            WKWebView(
                frame = platform.CoreGraphics.CGRectZero.readValue(),
                configuration = config
            ).apply {
                navigationDelegate = delegate
                // ❗️loadRequestni update ichida qilamiz (stable control)
            }
        },

//        update =
//            { webView ->
//            // ✅ URL faqat parametr o‘zgarganda reload bo‘lsin
//            if (lastLoadedUrl != url) {
//                lastLoadedUrl = url
//                lastSentJson = null // yangi page -> jsonni qayta yuborishga ruxsat
//                isPageLoaded = false
//
//                val nsUrl = NSURL.URLWithString(url)
//                if (nsUrl != null) {
//                    webView.loadRequest(NSURLRequest.requestWithURL(nsUrl))
//                }
//            }
//
//            // ✅ Native -> Web JSON (faqat page loaded + json bor + oldin yuborilmagan bo‘lsa)
//            if (isPageLoaded && json != null && lastSentJson != json) {
//                lastSentJson = json
//
//                // Agar json string bo‘lib, ichida quotes bo‘lsa ham ishlashi uchun:
//                val escaped = escapeJsString(json)
//                // json shu ko‘rinishda bo‘lsin: {"a":1} yoki "text" (JS literal)
//                val script = "window.postMessage($escaped, '*');"
//                Logger.d("UniversalJsonWebView", "isPageLoaded=$isPageLoaded, json=$json")
//                Logger.d("UniversalJsonWebView", "isPageLoaded=$isPageLoaded, escaped=$escaped")
//
//                webView.evaluateJavaScript(script, completionHandler = null)
//            }
//        },
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
    private val onWebBackPressed: () -> Unit,

    ) : NSObject(), WKScriptMessageHandlerProtocol {

    override fun userContentController(
        userContentController: WKUserContentController,
        didReceiveScriptMessage: WKScriptMessage
    ) {
        // message.body odatda JS object bo‘ladi:
        // { type: "sendData", payload: "..." } yoki { type: "backPressed" }
        val body = didReceiveScriptMessage.body

        // Eng sodda parsing: NSDictionary bo‘lishi mumkin
        val dict = body as? Map<*, *> ?: run {
            // agar string kelib qolsa ham qabul qilamiz
            onJsonFromWeb(body?.toString())
            return
        }

        when (dict["type"]?.toString()) {
            "sendData" -> onJsonFromWeb(dict["payload"]?.toString())
            "backPressed" -> onWebBackPressed()
            "console" -> {
                val level = dict["level"]?.toString() ?: "log"
                val msg = dict["payload"]?.toString() ?: ""
                Logger.d("WEB_CONSOLE_$level", msg)
            }

            else -> {
                // noma’lum message — xohlasangiz log qilasiz
            }
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