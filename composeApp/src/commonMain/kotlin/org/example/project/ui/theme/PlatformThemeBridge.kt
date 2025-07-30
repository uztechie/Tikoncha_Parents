package org.example.project.ui.theme

expect object PlatformThemeBridge {
    fun onModeChanged(mode: ThemeMode)
    fun applyInitial(mode: ThemeMode)
}