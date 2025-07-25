package org.example.project.platform

import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module

actual val targetModule = module {
    single<Localization> { Localization(context = androidContext()) }
}