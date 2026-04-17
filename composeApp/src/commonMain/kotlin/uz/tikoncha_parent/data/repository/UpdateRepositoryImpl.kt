package uz.tikoncha_parent.data.repository

import kotlinx.coroutines.flow.Flow
import uz.tikoncha_parent.data.in_app_update.InAppUpdateDataSource
import uz.tikoncha_parent.domain.model.in_app_update.InstallEvent
import uz.tikoncha_parent.domain.model.in_app_update.UpdateStatus
import uz.tikoncha_parent.domain.repository.UpdateRepository

class UpdateRepositoryImpl (
    private val ds: InAppUpdateDataSource
) : UpdateRepository {
    override suspend fun checkUpdate(): UpdateStatus = ds.checkUpdate()

    override fun observeInstallEvents(): Flow<InstallEvent> = ds.observeInstallEvents()

    override suspend fun completeFlexibleUpdate() = ds.completeFlexibleUpdate()

}