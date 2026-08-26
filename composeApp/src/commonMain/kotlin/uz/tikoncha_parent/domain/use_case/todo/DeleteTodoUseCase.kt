package uz.tikoncha_parent.domain.use_case.todo

import uz.tikoncha_parent.domain.model.app_error.Outcome
import uz.tikoncha_parent.domain.repository.TodoRepository

class DeleteTodoUseCase(
    private val repository: TodoRepository
) {
    suspend operator fun invoke(id: String): Outcome<Unit> =
        repository.delete(id)
}