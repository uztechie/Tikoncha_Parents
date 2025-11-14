package uz.tikoncha_parent.platform

import org.koin.dsl.module
import uz.tikoncha_parent.domain.service.IOSPaymentService
import uz.tikoncha_parent.domain.service.PaymentService
import uz.tikoncha_parent.platform.AppIconLoader

actual val targetModule = module {
    single<AppIconLoader> { IosAppIconLoader() }
    single<PaymentService> { IOSPaymentService() }
}