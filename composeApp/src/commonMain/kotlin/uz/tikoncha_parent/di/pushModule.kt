package uz.tikoncha_parent.di

import org.koin.dsl.module
import uz.tikoncha_parent.domain.use_case.push.ParseFcmPayloadUseCase
import uz.tikoncha_parent.presentation.push.PushCoordinator

val pushModule = module {
    factory { ParseFcmPayloadUseCase() }
    single { PushCoordinator(parse = get(), notifier = get()) }
}