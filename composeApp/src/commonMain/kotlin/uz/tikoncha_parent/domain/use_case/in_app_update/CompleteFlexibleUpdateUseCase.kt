package uz.tikoncha_parent.domain.use_case.in_app_update

import uz.tikoncha_parent.domain.repository.UpdateRepository

class CompleteFlexibleUpdateUseCase (
    private val repo: UpdateRepository
) {
    suspend operator fun invoke() = repo.completeFlexibleUpdate()
}