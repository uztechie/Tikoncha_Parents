package uz.tikoncha_parent.di

import org.koin.core.module.Module
import org.koin.dsl.module
import uz.tikoncha_parent.platform.push.IosPushNotifier
import uz.tikoncha_parent.presentation.push.PushNotifier

actual val pushPlatformModule: Module = module {
    single<PushNotifier> { IosPushNotifier() }
}