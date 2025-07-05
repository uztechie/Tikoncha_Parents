package org.example.project.platform

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

@Composable
actual fun rememberPlatformKeyboardState(): State<Boolean> {
    return remember {
        mutableStateOf(false)
    }
}