package uz.tikoncha_parent.platform

import org.koin.dsl.module
import uz.tikoncha_parent.platform.AppIconLoader

actual val targetModule = module {
    single<AppIconLoader> { IosAppIconLoader() }
}