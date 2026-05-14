package uz.tikoncha_parent.presentation.player

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import platform.UIKit.UIApplication
import platform.UIKit.UIStatusBarAnimation
import platform.UIKit.setStatusBarHidden

actual class ImmersiveController {

    actual fun enter() {
        UIApplication.sharedApplication.setStatusBarHidden(
            true,
            withAnimation = UIStatusBarAnimation.UIStatusBarAnimationNone
        )
    }

    actual fun exit() {
        UIApplication.sharedApplication.setStatusBarHidden(
            false,
            withAnimation = UIStatusBarAnimation.UIStatusBarAnimationNone
        )
    }
}

@Composable
actual fun rememberImmersiveController(): ImmersiveController =
    remember { ImmersiveController() }