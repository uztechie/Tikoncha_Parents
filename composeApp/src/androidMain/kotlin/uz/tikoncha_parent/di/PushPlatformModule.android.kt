package uz.tikoncha_parent.di

import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module
import uz.tikoncha_parent.platform.push.AndroidPushNotifier
import uz.tikoncha_parent.presentation.push.PushNotifier

actual val pushPlatformModule: Module = module {
    single<PushNotifier> { AndroidPushNotifier(androidContext()) }
}