package uz.tikoncha_parent.presentation.task.create_task

sealed interface CreateTaskEffect {
    data object NavigateToSuccess : CreateTaskEffect
    data object NavigateBack : CreateTaskEffect
    data class ShowError(val message: String) : CreateTaskEffect
    data class ShowSuccessDialog(val message: String) : CreateTaskEffect
}