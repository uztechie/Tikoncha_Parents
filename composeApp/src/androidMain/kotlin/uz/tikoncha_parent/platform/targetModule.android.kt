package uz.tikoncha_parent.platform

import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

actual val targetModule = module {
    single<AppIconLoader> { AndroidAppIconLoader(androidContext()) }
}