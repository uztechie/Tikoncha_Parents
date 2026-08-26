package uz.tikoncha_parent.domain.use_case.todo

import uz.tikoncha_parent.domain.model.app_error.ErrorCause
import uz.tikoncha_parent.domain.model.app_error.Outcome
import uz.tikoncha_parent.domain.model.todo.Todo
import uz.tikoncha_parent.domain.repository.TodoRepository

class CreateTodoUseCase(
    private val repository: TodoRepository
) {
    suspend operator fun invoke(params: CreateTodoParams): Outcome<Todo> {
        // domain-level validation
        if (params.title.isBlank()) {
            return Outcome.Failure(ErrorCause.EmptyTitle)
        }
        if (params.coin < 0) {
            return Outcome.Failure(ErrorCause.NegativeCoin)
        }
        return repository.create(params)
    }
}