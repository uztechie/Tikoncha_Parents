package org.example.project.presentation.map

import org.example.project.platform.isLocationServiceEnabled
import org.example.project.platform.openLocationSettings

class EnsureGpsEnabledUseCase {

    operator fun invoke(): Boolean {
        if (isLocationServiceEnabled()) return true
        openLocationSettings()
        return false
    }
}