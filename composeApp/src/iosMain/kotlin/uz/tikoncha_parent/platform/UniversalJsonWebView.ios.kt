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

    LaunchedEffect(json){
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

            WKWebView(
                frame = platform.CoreGraphics.CGRectZero.readValue(),
                configuration = config
            ).apply {
                navigationDelegate = delegate
                // ❗️loadRequestni update ichida qilamiz (stable control)
            }
        },
        update = { webView ->
            // ✅ URL faqat parametr o‘zgarganda reload bo‘lsin
            if (lastLoadedUrl != url) {
                lastLoadedUrl = url
                lastSentJson = null // yangi page -> jsonni qayta yuborishga ruxsat
                isPageLoaded = false

                val nsUrl = NSURL.URLWithString(url)
                if (nsUrl != null) {
                    webView.loadRequest(NSURLRequest.requestWithURL(nsUrl))
                }
            }

            // ✅ Native -> Web JSON (faqat page loaded + json bor + oldin yuborilmagan bo‘lsa)
            if (isPageLoaded && json != null && lastSentJson != json) {
                lastSentJson = json

                // Agar json string bo‘lib, ichida quotes bo‘lsa ham ishlashi uchun:
                // json shu ko‘rinishda bo‘lsin: {"a":1} yoki "text" (JS literal)
                val script = "window.postMessage($json);"
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