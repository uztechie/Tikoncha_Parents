package uz.tikoncha_parent.platform

import android.util.Log
import android.view.ViewGroup
import android.webkit.ConsoleMessage
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import uz.tikoncha_parent.presentation.base.Loading
import uz.tikoncha_parent.presentation.base.LoadingDialog

@Composable
actual fun KmpWebView(
    url: String,
    onCreated: (KmpWebViewController) -> Unit
) {
    val context = LocalContext.current

    var webView: WebView? by remember { mutableStateOf(null) }
    var isLoading by remember { mutableStateOf(true) }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        AndroidView(
            modifier = Modifier
                .fillMaxSize(),
            factory = {
                // Bitta WebView yaratamiz
                val wv = WebView(context).apply {
                    webView = this

                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )

                    WebView.setWebContentsDebuggingEnabled(true)

                    // WebView sozlamalar
                    settings.javaScriptEnabled = true
                    settings.domStorageEnabled = true
                    settings.cacheMode = WebSettings.LOAD_NO_CACHE
                    settings.javaScriptCanOpenWindowsAutomatically = true

                    // Console loglarni olish (console.log, console.error va hokazo)
                    webChromeClient = object : WebChromeClient() {
                        override fun onConsoleMessage(consoleMessage: ConsoleMessage?): Boolean {
                            Log.d(
                                "WEB_LOG",
                                "${consoleMessage?.message()}  (line: ${consoleMessage?.lineNumber()})"
                            )
                            return true
                        }
                    }
                }

                // 🔥 Controller: sahifa tayyor bo'lmaguncha JSON'ni queue qiladi
                class AndroidKmpWebViewController(
                    private val webViewInner: WebView
                ) : KmpWebViewController {

                    private var isReady = false
                    private val pending = mutableListOf<String>()

                    fun markReady() {
                        if (isReady) return
                        isReady = true

                        // Sahifa yuklangandan keyin – barcha navbatdagi JSON'larni yuboramiz
                        pending.forEach { json ->
                            reallySend(json)
                        }
                        pending.clear()
                    }

                    override fun postJson(json: String) {
                        if (!isReady) {
                            // Hali JS / hook tayyor emas – navbatga qo'yamiz
                            pending += json
                        } else {
                            reallySend(json)
                        }
                    }

                    private fun reallySend(json: String) {
                        // JS tarafdagi window.postMessage hook'iga yuboriladi
                        val script = """
                            window.postMessage($json);
                        """.trimIndent()

                        Log.d("WEB_LOG", "postJson script: $script")
                        webViewInner.evaluateJavascript(script, null)
                    }
                }

                val controller = AndroidKmpWebViewController(wv)

                // Requestlar, headers, sahifa holatini log qilish
                wv.webViewClient = object : WebViewClient() {

                    override fun onPageFinished(view: WebView?, url: String?) {
                        super.onPageFinished(view, url)
                        isLoading = false
                        Log.d("WEB_LOG", "Page finished: $url")

                        // 👇 Sahifa va JS tayyor – endi queued JSON'larni yuboramiz
                        controller.markReady()
                    }

                    override fun shouldInterceptRequest(
                        view: WebView?,
                        request: WebResourceRequest?
                    ): WebResourceResponse? {

                        Log.d("WEB_LOG", "URL = ${request?.url}")
                        Log.d("WEB_LOG", "Headers = ${request?.requestHeaders}")

                        return super.shouldInterceptRequest(view, request)
                    }
                }

                // URL'ni yuklaymiz
                wv.loadUrl(url)

                // Controller'ni tashqi dunyoga beramiz
                onCreated(controller)

                wv
            },
            update = {
                // Hozircha update ichida hech narsa qilish shart emas
            }
        )

        // Agar xohlasang, bu yerda loading indicator qo'yib olishing mumkin:

        if (isLoading) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 100.dp)
            ) {
                Loading(isLoading)
            }
        }

    }
}
