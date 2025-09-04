package uz.tikoncha_parent.presentation.map

import uz.tikoncha_parent.platform.isLocationServiceEnabled
import uz.tikoncha_parent.platform.openLocationSettings

class EnsureGpsEnabledUseCase {

    operator fun invoke(): Boolean {
        if (isLocationServiceEnabled()) return true
        openLocationSettings()
        return false
    }
}