package uz.tikoncha_parent.platform

import dev.icerock.moko.geo.LocationTracker
import dev.icerock.moko.permissions.PermissionsController
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import uz.tikoncha_parent.domain.service.AndroidPaymentService
import uz.tikoncha_parent.domain.service.PaymentService
import uz.tikoncha_parent.presentation.map.LocationViewModel
import uz.tikoncha_parent.presentation.profile.subscription.payment.PaymentViewModel

actual val targetModule = module {
    single<AppIconLoader> { AndroidAppIconLoader(androidContext()) }
    single<PaymentService> { AndroidPaymentService(androidContext()) }

    single {
        PermissionsController(
            applicationContext = androidContext()
        )
    }

    single {
        LocationTracker(
            permissionsController = get()
        )
    }

    single {
        LocationViewModel(
            tracker = get(),
            childrenLocationUseCase = get()
        )
    }

}