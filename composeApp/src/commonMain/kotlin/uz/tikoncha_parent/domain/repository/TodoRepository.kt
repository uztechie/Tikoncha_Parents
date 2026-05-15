package uz.tikoncha_parent.domain.repository

import uz.tikoncha_parent.domain.model.todo.PagedResult
import uz.tikoncha_parent.domain.model.todo.Todo
import uz.tikoncha_parent.domain.model.todo.TodosQuery
import uz.tikoncha_parent.domain.use_case.todo.CreateTodoParams
import uz.tikoncha_parent.domain.use_case.todo.UpdateTodoParams

interface TodoRepository {
    suspend fun getTodos(query: TodosQuery): Result<PagedResult<Todo>>
    suspend fun getById(id: String): Result<Todo>
    suspend fun create(params: CreateTodoParams): Result<Todo>
    suspend fun update(params: UpdateTodoParams): Result<Todo>
    suspend fun delete(id: String): Result<Unit>
    suspend fun complete(id: String): Result<Todo>
}