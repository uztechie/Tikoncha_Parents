package uz.tikoncha_parent.domain.use_case.todo

import uz.tikoncha_parent.domain.model.todo.Todo
import uz.tikoncha_parent.domain.repository.TodoRepository

class UpdateTodoUseCase(
    private val repository: TodoRepository
) {
    suspend operator fun invoke(params: UpdateTodoParams): Result<Todo> {
        if (params.title.isBlank()) {
            return Result.failure(IllegalArgumentException("Sarlavha bo'sh"))
        }
        if (params.coin < 0) {
            return Result.failure(IllegalArgumentException("Tanga manfiy bo'lmasin"))
        }
        return repository.update(params)
    }
}