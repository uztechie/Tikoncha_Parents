package uz.tikoncha_parent.domain.use_case.todo

import uz.tikoncha_parent.domain.model.todo.Todo
import uz.tikoncha_parent.domain.repository.TodoRepository

class GetTodoByIdUseCase(
    private val repository: TodoRepository
) {
    suspend operator fun invoke(id: String): Result<Todo> =
        repository.getById(id)
}