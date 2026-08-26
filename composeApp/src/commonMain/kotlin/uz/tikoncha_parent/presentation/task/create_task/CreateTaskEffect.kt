package uz.tikoncha_parent.presentation.task.create_task

import uz.tikoncha_parent.domain.model.app_error.Outcome

sealed interface CreateTaskEffect {
    data object NavigateToSuccess : CreateTaskEffect
    data object NavigateBack : CreateTaskEffect
    data class ShowFailure(val failure: Outcome.Failure) : CreateTaskEffect
    data class ShowSuccessDialog(val message: String) : CreateTaskEffect
}