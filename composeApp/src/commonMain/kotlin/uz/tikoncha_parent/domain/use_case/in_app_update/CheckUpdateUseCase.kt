package uz.tikoncha_parent.domain.use_case.in_app_update

import uz.tikoncha_parent.domain.model.in_app_update.UpdateStatus
import uz.tikoncha_parent.domain.repository.UpdateRepository


class CheckUpdateUseCase (
    private val repo: UpdateRepository
) {
    suspend operator fun invoke(): UpdateStatus = repo.checkUpdate()
}