package org.example.project.platform

import platform.Foundation.NSUserDefaults

@Suppress(names = ["EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING"])
actual class Localization {
    actual fun applyLanguage(ios: String) {
        NSUserDefaults.Companion.standardUserDefaults.setObject(
            arrayListOf(ios),"AppleLanguage"
        )
    }
}