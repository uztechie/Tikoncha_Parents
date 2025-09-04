package uz.tikoncha_parent.ui.theme

expect object PlatformThemeBridge {
    fun onModeChanged(mode: ThemeMode)
    fun applyInitial(mode: ThemeMode)
}