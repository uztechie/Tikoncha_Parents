package org.example.project

import androidx.compose.ui.window.ComposeUIViewController
import org.example.project.core.initKoin

fun MainViewController() = ComposeUIViewController(
    configure = {
        initKoin()
    }
) { App() }