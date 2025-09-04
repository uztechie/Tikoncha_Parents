package uz.tikoncha_parent.ui.theme

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object ThemeController {
    private val _mode = MutableStateFlow(ThemePrefs.load())
    val mode: StateFlow<ThemeMode> = _mode.asStateFlow()

    init {
        // App startida iOS window style ni to'g'rilash (Android uchun no-op)
        PlatformThemeBridge.applyInitial(_mode.value)
    }

    fun setMode(newMode: ThemeMode) {
        if (_mode.value == newMode) return
        _mode.value = newMode
        ThemePrefs.save(newMode)
        PlatformThemeBridge.onModeChanged(newMode)
    }
}