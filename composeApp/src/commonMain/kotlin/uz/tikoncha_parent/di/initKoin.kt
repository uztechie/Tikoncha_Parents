// composeApp/src/commonMain/kotlin/uz/tikoncha_parent/core/InitKoin.kt
package uz.tikoncha_parent.di

import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

fun initKoin(config: KoinAppDeclaration? = null) {
    startKoin {
        config?.invoke(this)
        modules(
            sharedModule,
            pushModule,          // ParseFcmPayloadUseCase + PushCoordinator
            pushPlatformModule,  // PushNotifier (Android/iOS)
            targetModule,
        )
    }
}