package uz.tikoncha_parent.platform

import androidx.compose.runtime.Composable

@Composable
expect fun KmpWebView(
    url: String,
    onCreated: (KmpWebViewController) -> Unit = {}
)

interface KmpWebViewController {
    fun postJson(json: String)
}
