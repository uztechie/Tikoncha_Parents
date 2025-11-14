package uz.tikoncha_parent.platform

import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import uz.tikoncha_parent.domain.service.AndroidPaymentService
import uz.tikoncha_parent.domain.service.PaymentService
import uz.tikoncha_parent.presentation.profile.subscription.PaymentViewModel

actual val targetModule = module {
    single<AppIconLoader> { AndroidAppIconLoader(androidContext()) }
    single<PaymentService> { AndroidPaymentService(androidContext()) }
}