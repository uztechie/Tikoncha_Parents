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

    val messageHandler = remember {
        IOSJsonMessageHandler(
            onJsonFromWeb = onIncomingJson,
            onWebBackPressed = onBackPressed
        )
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
            // JS bridge: AndroidJson.sendData / AndroidJson.backPressed ni iOS’da ham ishlatamiz
            val controller = WKUserContentController()

            val bridgeJs = """
                (function () {
                    if (window.AndroidJson) return;
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

            WKWebView(frame = platform.CoreGraphics.CGRectZero.readValue(), configuration = config).apply {
                navigationDelegate = delegate
                val nsUrl = NSURL.URLWithString(url)
                if (nsUrl != null) {
                    loadRequest(NSURLRequest.requestWithURL(nsUrl))
                }
            }
        },
        update = { webView ->
            // URL o‘zgarsa reload
            val current = webView.URL?.absoluteString
            if (current != url) {
                isPageLoaded = false
                val nsUrl = NSURL.URLWithString(url)
                if (nsUrl != null) {
                    webView.loadRequest(NSURLRequest.requestWithURL(nsUrl))
                }
            }

            // Native -> Web JSON
            if (isPageLoaded && json != null) {
                val script = "window.postMessage($json);"
                webView.evaluateJavaScript(script, completionHandler = null)
            }
        },
        onRelease = { webView ->
            // clean up (leak bo‘lmasin)
            webView.navigationDelegate = null
            webView.configuration.userContentController.removeScriptMessageHandlerForName("AndroidJson")
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
            else -> {
                // noma’lum message — xohlasangiz log qilasiz
            }
        }
    }
}