package org.example.project.ui.theme

import com.russhwolf.settings.Settings
import com.russhwolf.settings.set

object ThemePrefs {
    private const val KEY = "theme_mode"
    private val settings: Settings = Settings() // Android: SharedPrefs, iOS: UserDefaults

    fun load(): ThemeMode {
        val code = settings.getStringOrNull(KEY) ?: "SYSTEM"
        return runCatching { ThemeMode.valueOf(code) }.getOrDefault(ThemeMode.SYSTEM)
    }

    fun save(mode: ThemeMode) {
        settings[KEY] = mode.name
    }
}