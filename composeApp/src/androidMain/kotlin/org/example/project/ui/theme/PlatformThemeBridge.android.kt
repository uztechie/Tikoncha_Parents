package org.example.project.ui.theme

import androidx.appcompat.app.AppCompatDelegate

actual object   PlatformThemeBridge {

    actual fun applyInitial(mode: ThemeMode) {
        onModeChanged(mode)
    }

    actual fun onModeChanged(mode: ThemeMode) {
        val nightMode = when (mode) {
            ThemeMode.SYSTEM -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
            ThemeMode.LIGHT  -> AppCompatDelegate.MODE_NIGHT_NO
            ThemeMode.DARK   -> AppCompatDelegate.MODE_NIGHT_YES
        }
        println("PlatformThemeBridge onModeChanged=$mode")
        AppCompatDelegate.setDefaultNightMode(nightMode)

    }
}