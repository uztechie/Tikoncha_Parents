package uz.tikoncha_parent.presentation.task.create_task

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController

@Composable
fun rememberHideKeyboard(): ()-> Unit {
    val focusManager = LocalFocusManager.current
    val keyboard = LocalSoftwareKeyboardController.current
    return {
        focusManager.clearFocus(force = true)
        keyboard?.hide()
    }
}