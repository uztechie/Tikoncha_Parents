package uz.tikoncha_parent.platform

import android.content.Context
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import uz.tikoncha_parent.data.in_app_update.InAppUpdateDataSource
import uz.tikoncha_parent.domain.model.in_app_update.InstallEvent
import uz.tikoncha_parent.domain.model.in_app_update.UpdateStatus
import uz.tikoncha_parent.domain.model.in_app_update.UpdateType

class PlayCoreInAppUpdateDataSource(
    private val context: Context
) : InAppUpdateDataSource {

    private val appUpdateManager = AppUpdateManagerFactory.create(context)

    override suspend fun checkUpdate(): UpdateStatus {
        return try {
            val info: AppUpdateInfo = appUpdateManager.appUpdateInfo.await()

            // 1) Avval: oldingi flexible update yuklab olingan-u, o'rnatilmaganmi?
            if (info.installStatus() == InstallStatus.DOWNLOADED) {
                return UpdateStatus.Downloaded
            }

            // 2) Yangilanish mavjudmi?
            val isAvailable =
                info.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE

            if (!isAvailable) return UpdateStatus.NoUpdate

            // 3) Qaysi turlar ruxsat etilgan?
            val allowed = buildSet {
                if (info.isUpdateTypeAllowed(AppUpdateOptions.defaultOptions(AppUpdateType.FLEXIBLE))) {
                    add(UpdateType.FLEXIBLE)
                }
                if (info.isUpdateTypeAllowed(AppUpdateOptions.defaultOptions(AppUpdateType.IMMEDIATE))) {
                    add(UpdateType.IMMEDIATE)
                }
            }

            if (allowed.isEmpty()) {
                UpdateStatus.NoUpdate
            } else {
                UpdateStatus.UpdateAvailable(
                    allowedTypes = allowed,
                    versionCodeAvailable = try {
                        info.availableVersionCode()
                    } catch (_: Exception) {
                        null
                    }
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
            UpdateStatus.NotSupported
        }
    }

    override fun observeInstallEvents(): Flow<InstallEvent> = callbackFlow {
        val listener = InstallStateUpdatedListener { state ->
            when (state.installStatus()) {
                InstallStatus.DOWNLOADING -> {
                    trySend(
                        InstallEvent.Downloading(
                            bytesDownloaded = state.bytesDownloaded(),
                            totalBytes = state.totalBytesToDownload()
                        )
                    )
                }

                InstallStatus.DOWNLOADED -> {
                    trySend(InstallEvent.Downloaded)
                }

                InstallStatus.FAILED -> {
                    trySend(InstallEvent.Failed(state.installErrorCode()))
                }

                else -> Unit
            }
        }
        appUpdateManager.registerListener(listener)
        awaitClose { appUpdateManager.unregisterListener(listener) }
    }

    override suspend fun completeFlexibleUpdate() {
        try {
            appUpdateManager.completeUpdate().await()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}