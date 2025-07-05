import androidx.compose.runtime.Composable
import androidx.compose.runtime.State

import androidx.compose.runtime.*
import kotlinx.cinterop.*
import platform.Foundation.*
import platform.UIKit.*
import platform.darwin.*
import kotlin.native.concurrent.ThreadLocal

@Composable
actual fun rememberPlatformKeyboardState(): State<Boolean> {
    val keyboardVisible = remember { mutableStateOf(false) }

    DisposableEffect(Unit) {
        val showObserver = NSNotificationCenter.defaultCenter.addObserverForName(
            name = UIKeyboardWillShowNotification,
            `object` = null,
            queue = NSOperationQueue.mainQueue
        ) { _ ->
            keyboardVisible.value = true
        }

        val hideObserver = NSNotificationCenter.defaultCenter.addObserverForName(
            name = UIKeyboardWillHideNotification,
            `object` = null,
            queue = NSOperationQueue.mainQueue
        ) { _ ->
            keyboardVisible.value = false
        }

        onDispose {
            NSNotificationCenter.defaultCenter.removeObserver(showObserver)
            NSNotificationCenter.defaultCenter.removeObserver(hideObserver)
        }
    }

    return keyboardVisible
}
