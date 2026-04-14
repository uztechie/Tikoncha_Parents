package uz.tikoncha_parent.domain.use_case.in_app_update

import kotlinx.coroutines.flow.Flow
import uz.tikoncha_parent.domain.model.in_app_update.InstallEvent
import uz.tikoncha_parent.domain.repository.UpdateRepository

class ObserveInstallEventsUseCase (
    private val repo: UpdateRepository
) {
    operator fun invoke(): Flow<InstallEvent> = repo.observeInstallEvents()
}