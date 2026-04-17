package uz.tikoncha_parent.platform

import dev.icerock.moko.geo.LocationTracker
import dev.icerock.moko.permissions.ios.PermissionsController
import org.koin.dsl.module
import platform.CoreLocation.kCLLocationAccuracyBest
import uz.tikoncha_parent.data.in_app_update.InAppUpdateDataSource
import uz.tikoncha_parent.domain.service.IOSPaymentService
import uz.tikoncha_parent.domain.service.PaymentService
import uz.tikoncha_parent.platform.AppIconLoader
import uz.tikoncha_parent.presentation.map.LocationViewModel

actual val targetModule = module {
    single<AppIconLoader> { IosAppIconLoader() }
    single<PaymentService> { IOSPaymentService() }
    single<InAppUpdateDataSource> { IosInAppUpdateDataSource() }

    single {
        PermissionsController()
    }

    single {
        LocationTracker(
            permissionsController = get(),
            accuracy = kCLLocationAccuracyBest
        )
    }

    single {
        LocationViewModel(
            tracker = get()
        )
    }
}