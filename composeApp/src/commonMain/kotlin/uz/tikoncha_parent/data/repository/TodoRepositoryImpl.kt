package uz.tikoncha_parent.data.repository

import uz.tikoncha_parent.data.mapper.todo.toDomain
import uz.tikoncha_parent.data.mapper.todo.toRequest
import uz.tikoncha_parent.data.remote.TodoApiService
import uz.tikoncha_parent.domain.model.todo.PagedResult
import uz.tikoncha_parent.domain.model.todo.Todo
import uz.tikoncha_parent.domain.model.todo.TodosQuery
import uz.tikoncha_parent.domain.repository.TodoRepository
import uz.tikoncha_parent.domain.use_case.todo.CreateTodoParams
import uz.tikoncha_parent.domain.use_case.todo.UpdateTodoParams

class TodoRepositoryImpl(
    private val api: TodoApiService
) : TodoRepository {

    override suspend fun getTodos(query: TodosQuery): Result<PagedResult<Todo>> = runCatching {
        val response = api.listTodos(
            targetUserId = query.targetUserId,
            isCompleted = query.filter.isCompleted,
            isChildDone = query.filter.isChildDone,
            isExpired = query.filter.isExpired,
            importance = query.filter.importance?.apiValue,
            createdByRole = query.filter.createdByRole.apiValue,
            limit = query.limit,
            offset = query.offset
        )
        if (!response.success || response.data == null) {
            error(response.error ?: "Vazifalar yuklanmadi")
        }
        response.data.toDomain()
    }

    override suspend fun getById(id: String): Result<Todo> = runCatching {
        val response = api.getTodoById(id)
        if (!response.success || response.data == null) {
            error(response.error ?: "Vazifa topilmadi")
        }
        response.data.toDomain()
    }

    override suspend fun create(params: CreateTodoParams): Result<Todo> = runCatching {
        val response = api.createTodo(params.toRequest())
        if (!response.success || response.data == null) {
            error(response.error ?: "Vazifa yaratilmadi")
        }
        response.data.toDomain()
    }

    override suspend fun update(params: UpdateTodoParams): Result<Todo> = runCatching {
        val response = api.updateTodo(params.id, params.toRequest())
        if (!response.success || response.data == null) {
            error(response.error ?: "Vazifa yangilanmadi")
        }
        response.data.toDomain()
    }

    override suspend fun delete(id: String): Result<Unit> = runCatching {
        val response = api.deleteTodo(id)
        if (!response.success) error(response.error ?: "O'chirib bo'lmadi")
    }

    override suspend fun complete(id: String): Result<Todo> = runCatching {
        val response = api.completeTodo(id)
        if (!response.success || response.data == null) {
            error(response.error ?: "Tasdiqlab bo'lmadi")
        }
        response.data.toDomain()
    }
}