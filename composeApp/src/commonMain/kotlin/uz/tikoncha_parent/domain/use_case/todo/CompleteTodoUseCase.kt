package uz.tikoncha_parent.domain.use_case.todo

import uz.tikoncha_parent.domain.model.app_error.Outcome
import uz.tikoncha_parent.domain.model.todo.Todo
import uz.tikoncha_parent.domain.repository.TodoRepository

class CompleteTodoUseCase(
    private val repository: TodoRepository
) {
    suspend operator fun invoke(id: String): Outcome<Todo> =
        repository.complete(id)
}