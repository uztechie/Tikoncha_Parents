@file:OptIn(ExperimentalForeignApi::class)

package uz.tikoncha_parent.platform

import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.UIKitView
import io.ktor.client.request.invoke
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.readValue
import platform.CoreGraphics.CGRectZero
import platform.Foundation.NSURL
import platform.Foundation.NSURLRequest
import platform.WebKit.WKWebView
import platform.WebKit.WKWebViewConfiguration

@Composable
actual fun KmpWebView(
    url: String,
    onCreated: (KmpWebViewController) -> Unit
) {
    UIKitView(
        factory = {
            val config = WKWebViewConfiguration()
            val webView = WKWebView(frame = CGRectZero.readValue(), config)

            webView.loadRequest(NSURLRequest(URL = NSURL(string = url)))

            onCreated(object : KmpWebViewController {
                override fun postJson(json: String) {
                    val script = "window.onNativeMessage($json)"
                    webView.evaluateJavaScript(script, null)
                }
            })

            webView
        }
    )
}