package uz.tikoncha_parent.platform

import android.util.Log
import android.webkit.ConsoleMessage
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.viewinterop.AndroidView

@Composable
actual fun KmpWebView(
    url: String,
    onCreated: (KmpWebViewController) -> Unit
) {
    AndroidView(
        modifier = Modifier
            .fillMaxSize(),
        factory = { context ->
            WebView(context).apply {
                settings.javaScriptEnabled = true

                webChromeClient = object : WebChromeClient() {
                    override fun onConsoleMessage(consoleMessage: ConsoleMessage?): Boolean {
                        Log.d(
                            "WEBVIEW_LOG",
                            "${consoleMessage?.message()} " +
                                    "(line ${consoleMessage?.lineNumber()} in ${consoleMessage?.sourceId()})"
                        )
                        return true
                    }
                }
                loadUrl(url)
                onCreated(object : KmpWebViewController {
                    override fun postJson(json: String) {
                        val js = "window.postMessage($json)"
                        val script = "window.postMessage($json)"
                        Log.d("WEBVIEW_LOG", "postJson: $json")
                        Log.d("WEBVIEW_LOG", "postJson: $script")
                        evaluateJavascript(script, null)
                    }
                })
            }
        }
    )
}