package uz.tikoncha_parent

import androidx.compose.ui.window.ComposeUIViewController
import uz.tikoncha_parent.core.initKoin

fun MainViewController() = ComposeUIViewController(
    configure = {
        initKoin()

    }
) {

    App()
}