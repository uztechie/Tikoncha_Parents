package uz.tikoncha_parent.domain.use_case.todo

import uz.tikoncha_parent.domain.model.app_error.Outcome
import uz.tikoncha_parent.domain.model.todo.PagedResult
import uz.tikoncha_parent.domain.model.todo.Todo
import uz.tikoncha_parent.domain.model.todo.TodosQuery
import uz.tikoncha_parent.domain.repository.TodoRepository

class GetTodosUseCase(
    private val repository: TodoRepository
) {
    suspend operator fun invoke(query: TodosQuery): Outcome<PagedResult<Todo>> =
        repository.getTodos(query)
}