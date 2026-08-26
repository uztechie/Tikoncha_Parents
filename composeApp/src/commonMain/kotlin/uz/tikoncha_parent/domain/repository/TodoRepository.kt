package uz.tikoncha_parent.domain.repository

import uz.tikoncha_parent.domain.model.app_error.Outcome
import uz.tikoncha_parent.domain.model.todo.PagedResult
import uz.tikoncha_parent.domain.model.todo.Todo
import uz.tikoncha_parent.domain.model.todo.TodosQuery
import uz.tikoncha_parent.domain.use_case.todo.CreateTodoParams
import uz.tikoncha_parent.domain.use_case.todo.UpdateTodoParams

interface TodoRepository {
    suspend fun getTodos(query: TodosQuery): Outcome<PagedResult<Todo>>
    suspend fun create(params: CreateTodoParams): Outcome<Todo>
    suspend fun update(params: UpdateTodoParams): Outcome<Todo>
    suspend fun delete(id: String): Outcome<Unit>
    suspend fun complete(id: String): Outcome<Todo>
}