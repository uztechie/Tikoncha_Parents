package uz.tikoncha_parent.platform

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.model.AppUpdateType
import kotlinx.coroutines.tasks.await
import uz.tikoncha_parent.domain.model.in_app_update.UpdateEffect
import uz.tikoncha_parent.domain.model.in_app_update.UpdateType
import uz.tikoncha_parent.presentation.in_app_update.UpdateViewModel

@Composable
actual fun HandleUpdateEffect(viewModel: UpdateViewModel) {
    val context = LocalContext.current
    val activity = context as? Activity ?: return
    val appUpdateManager = AppUpdateManagerFactory.create(context)

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartIntentSenderForResult()
    ) { /* natija */ }

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is UpdateEffect.StartUpdateFlow -> {
                    val info = appUpdateManager.appUpdateInfo.await()
                    val type = if (effect.type == UpdateType.FLEXIBLE)
                        AppUpdateType.FLEXIBLE else AppUpdateType.IMMEDIATE
                    appUpdateManager.startUpdateFlowForResult(
                        info, launcher, AppUpdateOptions.newBuilder(type).build()
                    )
                }
                UpdateEffect.OpenAppStore -> Unit
            }
        }
    }
}