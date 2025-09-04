package uz.tikoncha_parent.presentation.map

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner

@Composable
fun OnScreenActive(
    launchedToSettings: Boolean,
    onReturned: () -> Unit,
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val callback by rememberUpdatedState(onReturned)

    DisposableEffect(lifecycleOwner, launchedToSettings) {
        if (!launchedToSettings) return@DisposableEffect onDispose {}

        var leftOnce = false   // ← Avval ekrandan chiqqanimizni belgilaymiz

        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_PAUSE,
                Lifecycle.Event.ON_STOP -> {
                    leftOnce = true      // Ekrandan chiqib ketdik
                }

                Lifecycle.Event.ON_RESUME -> {
                    if (leftOnce) {      // Faqat qaytgandan keyin 🔄
                        leftOnce = false // (agar keyingi safar ham kerak bo‘lsa)
                        callback()
                    }
                }

                else -> Unit
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }
}
