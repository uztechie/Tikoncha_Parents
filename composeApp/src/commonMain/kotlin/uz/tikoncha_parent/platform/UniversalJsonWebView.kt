package uz.tikoncha_parent.platform

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
expect fun UniversalJsonWebView(
    url: String,
    json: String?,                       // Native -> Web
    onIncomingJson: (String?) -> Unit,   // Web -> Native
    modifier: Modifier = Modifier,
    onBackPressed: () -> Unit
)