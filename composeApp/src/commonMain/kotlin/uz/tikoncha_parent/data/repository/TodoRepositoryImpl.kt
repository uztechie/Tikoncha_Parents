package uz.tikoncha_parent.data.repository

import uz.tikoncha_parent.data.mapper.todo.toDomain
import uz.tikoncha_parent.data.mapper.todo.toRequest
import uz.tikoncha_parent.data.remote.TodoApiService
import uz.tikoncha_parent.data.remote.app_error.ApiErrorMapper
import uz.tikoncha_parent.domain.model.app_error.ErrorCause
import uz.tikoncha_parent.domain.model.app_error.Outcome
import uz.tikoncha_parent.domain.model.todo.PagedResult
import uz.tikoncha_parent.domain.model.todo.Todo
import uz.tikoncha_parent.domain.model.todo.TodosQuery
import uz.tikoncha_parent.domain.repository.TodoRepository
import uz.tikoncha_parent.domain.use_case.todo.CreateTodoParams
import uz.tikoncha_parent.domain.use_case.todo.UpdateTodoParams

class TodoRepositoryImpl(
    private val api: TodoApiService
) : TodoRepository {

    override suspend fun getTodos(query: TodosQuery): Outcome<PagedResult<Todo>> =
        apiCall(TAG) {
            val r = api.listTodos(
                targetUserId = query.targetUserId,
                isCompleted = query.filter.isCompleted,
                isChildDone = query.filter.isChildDone,
                isExpired = query.filter.isExpired,
                importance = query.filter.importance?.apiValue,
                createdByRole = query.filter.createdByRole.apiValue,
                limit = query.limit,
                offset = query.offset
            )
            val body = r.data
            when {
                r.success && body != null -> Outcome.Success(body.toDomain())
                r.success -> Outcome.Failure(ErrorCause.InvalidResponse)
                else -> Outcome.Failure(ApiErrorMapper.fromCode(r.code), r.error)
            }
        }

    override suspend fun create(params: CreateTodoParams): Outcome<Todo> =
        apiCall(TAG) {
            val r = api.createTodo(params.toRequest())
            val body = r.data
            when {
                r.success && body != null -> Outcome.Success(body.toDomain())
                r.success -> Outcome.Failure(ErrorCause.InvalidResponse)
                else -> Outcome.Failure(ApiErrorMapper.fromCode(r.code), r.error)
            }
        }

    override suspend fun update(params: UpdateTodoParams): Outcome<Todo> =
        apiCall(TAG) {
            val r = api.updateTodo(params.id, params.toRequest())
            val body = r.data
            when {
                r.success && body != null -> Outcome.Success(body.toDomain())
                r.success -> Outcome.Failure(ErrorCause.InvalidResponse)
                else -> Outcome.Failure(ApiErrorMapper.fromCode(r.code), r.error)
            }
        }

    override suspend fun delete(id: String): Outcome<Unit> =
        apiCall(TAG) {
            val r = api.deleteTodo(id)
            if (r.success) Outcome.Success(Unit)
            else Outcome.Failure(ApiErrorMapper.fromCode(r.code), r.error)
        }

    override suspend fun complete(id: String): Outcome<Todo> =
        apiCall(TAG) {
            val r = api.completeTodo(id)
            val body = r.data
            when {
                r.success && body != null -> Outcome.Success(body.toDomain())
                r.success -> Outcome.Failure(ErrorCause.InvalidResponse)
                else -> Outcome.Failure(ApiErrorMapper.fromCode(r.code), r.error)
            }
        }

    private companion object { const val TAG = "TodoRepository" }
}