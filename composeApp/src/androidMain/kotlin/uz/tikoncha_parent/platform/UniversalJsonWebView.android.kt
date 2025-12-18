package uz.tikoncha_parent.platform


import androidx.compose.runtime.Composable
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.ViewGroup
import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView


@Composable
actual fun UniversalJsonWebView(
    url: String,
    json: String?,
    onIncomingJson: (String?) -> Unit,
    modifier: Modifier,
    onBackPressed: () -> Unit
) {
    val context = LocalContext.current

    var webView by remember { mutableStateOf<WebView?>(null) }
    var isPageLoaded by remember { mutableStateOf(false) }

    BackHandler(enabled = true) {
        val wv = webView
        if (wv != null && wv.canGoBack()) wv.goBack() else onBackPressed()
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .imePadding()
    ) {
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = {
                WebView(context).apply {
                    webView = this

                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )

                    settings.javaScriptEnabled = true
                    settings.domStorageEnabled = true
                    settings.cacheMode = WebSettings.LOAD_NO_CACHE
                    settings.javaScriptCanOpenWindowsAutomatically = true

                    // Web -> Android ko‘prik (Web: AndroidJson.sendData / AndroidJson.backPressed)
                    addJavascriptInterface(
                        AndroidJsonBridge(
                            onJsonFromWeb = onIncomingJson,
                            onWebBackPressed = onBackPressed
                        ),
                        "AndroidJson"
                    )

                    webChromeClient = object : WebChromeClient() {}
                    webViewClient = object : WebViewClient() {
                        override fun onPageFinished(view: WebView?, url: String?) {
                            isPageLoaded = true
                        }
                    }

                    loadUrl(url)
                }
            }
        )

        // Native -> Web: sahifa yuklangandan keyin yuboramiz
        LaunchedEffect(isPageLoaded, json) {
            val wv = webView ?: return@LaunchedEffect
            if (isPageLoaded && json != null) {
                val script = "window.postMessage($json);"
                Log.d("WEB_LOG", "postJson: $script")
                wv.evaluateJavascript(script, null)
            }
        }
    }
}

private class AndroidJsonBridge(
    private val onJsonFromWeb: (String?) -> Unit,
    private val onWebBackPressed: () -> Unit
) {
    private val mainHandler = Handler(Looper.getMainLooper())

    @JavascriptInterface
    fun sendData(json: String?) {
        mainHandler.post { onJsonFromWeb(json) }
    }

    @JavascriptInterface
    fun backPressed() {
        mainHandler.post { onWebBackPressed() }
    }
}