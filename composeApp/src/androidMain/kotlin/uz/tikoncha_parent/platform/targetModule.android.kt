package uz.tikoncha_parent.platform

import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.Priority
import dev.icerock.moko.geo.LocationTracker
import dev.icerock.moko.permissions.PermissionsController
import kotlinx.coroutines.flow.emptyFlow
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import uz.tikoncha_parent.data.in_app_update.InAppUpdateDataSource
import uz.tikoncha_parent.data.player.PlayerEngine
import uz.tikoncha_parent.domain.model.in_app_update.InstallEvent
import uz.tikoncha_parent.domain.model.in_app_update.UpdateStatus
import uz.tikoncha_parent.domain.model.in_app_update.UpdateType
import uz.tikoncha_parent.domain.service.AndroidPaymentService
import uz.tikoncha_parent.domain.service.PaymentService
import uz.tikoncha_parent.presentation.policy.location_rule.LocationRuleScreenModel
import uz.tikoncha_parent.presentation.profile.subscription.payment.PaymentViewModel
import uz.tikoncha_parent.presentation.tracking.TrackingScreenModel

actual val targetModule = module {
    single<AppIconLoader> { AndroidAppIconLoader(androidContext()) }
    single<PaymentService> { AndroidPaymentService(androidContext()) }
    single<InAppUpdateDataSource> { PlayCoreInAppUpdateDataSource(androidContext()) }
//    single<InAppUpdateDataSource> {
//        object : InAppUpdateDataSource {
//            override suspend fun checkUpdate() = UpdateStatus.UpdateAvailable(
//                allowedTypes = setOf(UpdateType.FLEXIBLE),
//                versionCodeAvailable = 99
//            )
//            override fun observeInstallEvents() = emptyFlow<InstallEvent>()
//            override suspend fun completeFlexibleUpdate() {}
//        }
//    }
    single {
        PermissionsController(
            applicationContext = androidContext()
        )
    }

    single {
        LocationTracker(
            permissionsController = get(),
            interval = 10000L,
            priority = Priority.PRIORITY_BALANCED_POWER_ACCURACY
        )
    }


    factory {
        TrackingScreenModel(
            childrenLocationUseCase = get(),
            locationTracker = get(),
            permissionsController = get(),
            subscriptionLimitUseCase = get(),
            permissionStatusUseCase = get()
        )
    }

    factory { LocationRuleScreenModel(get(), get()) }

    single { PlayerEngine(androidContext()) }

}