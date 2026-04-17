//package uz.tikoncha_parent.platform
//
//import android.content.Intent
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.ui.platform.LocalContext
//import androidx.core.net.toUri
//
//
//@Composable
//actual fun OpenUrl(url: String) {
//    val context = LocalContext.current
//    LaunchedEffect(url) {
//        val intent = Intent(Intent.ACTION_VIEW, url.toUri()).apply {
//            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//        }
//        context.startActivity(intent)
//    }
//}