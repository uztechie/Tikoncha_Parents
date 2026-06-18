package uz.tikoncha_parent

import androidx.compose.ui.window.ComposeUIViewController
import uz.tikoncha_parent.di.initKoin

fun MainViewController() = ComposeUIViewController(
    configure = {
        initKoin()

    }
) {

    App()
}