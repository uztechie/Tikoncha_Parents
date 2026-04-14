package uz.tikoncha_parent.domain.repository

import kotlinx.coroutines.flow.Flow
import uz.tikoncha_parent.domain.model.in_app_update.InstallEvent
import uz.tikoncha_parent.domain.model.in_app_update.UpdateStatus

interface UpdateRepository {
    suspend fun checkUpdate(): UpdateStatus
    fun observeInstallEvents(): Flow<InstallEvent>
    suspend fun completeFlexibleUpdate()
}