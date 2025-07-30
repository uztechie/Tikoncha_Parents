package org.example.project.ui.theme

import platform.UIKit.*
import platform.Foundation.*
import platform.darwin.dispatch_async
import platform.darwin.dispatch_get_main_queue

private fun allWindows(): List<UIWindow> {
    val app = UIApplication.sharedApplication

    // Swift Set<UIScene> -> K/N Set<*>
    val windowScenes: List<UIWindowScene> =
        (app.connectedScenes as? Set<*>)
            ?.filterIsInstance<UIWindowScene>()
            ?.toList()
            ?: emptyList()

    val fromScenes: List<UIWindow> =
        windowScenes.flatMap { ws ->
            // NSArray -> K/N List<*>, keyin aniq tipga filtrlaymiz
            (ws.windows as? List<*>)?.filterIsInstance<UIWindow>() ?: emptyList()
        }

    if (fromScenes.isNotEmpty()) return fromScenes

    // Fallback: iOS 12 yoki scenelar bo'lmasa
    return (app.windows as? List<*>)?.filterIsInstance<UIWindow>().orEmpty()
}

private fun UIWindow.applyStyle(style: UIUserInterfaceStyle) {
    this.overrideUserInterfaceStyle = style
}

private fun applyStyleToAll(style: UIUserInterfaceStyle) {
    dispatch_async(dispatch_get_main_queue()) {
        allWindows().forEach { it.applyStyle(style) }
    }
}


actual object PlatformThemeBridge {
    actual fun onModeChanged(mode: ThemeMode) {
        val style = when (mode) {
            ThemeMode.SYSTEM -> UIUserInterfaceStyle.UIUserInterfaceStyleUnspecified
            ThemeMode.LIGHT  -> UIUserInterfaceStyle.UIUserInterfaceStyleLight
            ThemeMode.DARK   -> UIUserInterfaceStyle.UIUserInterfaceStyleDark
        }
        applyStyleToAll(style)
    }

    actual fun applyInitial(mode: ThemeMode) {
        onModeChanged(mode)
    }
}