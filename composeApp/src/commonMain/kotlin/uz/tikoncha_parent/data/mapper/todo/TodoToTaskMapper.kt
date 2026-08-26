package uz.tikoncha_parent.data.mapper.todo

import uz.tikoncha_parent.data.remote.model.todo.CreateTodoRequest
import uz.tikoncha_parent.data.remote.model.todo.TodoDto
import uz.tikoncha_parent.data.remote.model.todo.TodoListData
import uz.tikoncha_parent.data.remote.model.todo.UpdateTodoRequest
import uz.tikoncha_parent.domain.model.todo.Importance
import uz.tikoncha_parent.domain.model.todo.PagedResult
import uz.tikoncha_parent.domain.model.todo.Todo
import uz.tikoncha_parent.domain.use_case.todo.CreateTodoParams
import uz.tikoncha_parent.domain.use_case.todo.UpdateTodoParams
import uz.tikoncha_parent.presentation.task.model.Task

// TodoDto → Todo
fun TodoDto.toDomain(): Todo = Todo(
    id = id,
    authorId = author_id,
    targetUserId = target_user_id,
    title = title,
    description = description ?: "",
    dueAt = due_date,
    importance = Importance.fromApi(importance),
    isChildDone = is_child_done,
    isCompleted = is_completed,
    isExpired = is_expired,
    createdAt = created_at,
    modifiedAt = modified_at,
    canUpdate = can_update?:false,
    coin = coin
)

// ✅ MANA SHU YO'Q EDI
fun TodoListData.toDomain(): PagedResult<Todo> = PagedResult(
    items = items.map { it.toDomain() },
    total = total,
    hasMore = has_more,
    limit = limit,
    offset = offset
)

// CreateTodoParams → CreateTodoRequest
fun CreateTodoParams.toRequest(): CreateTodoRequest = CreateTodoRequest(
    title = title,
    description = description,
    due_date = dueAt,
    importance = importance.apiValue,
    target_user_id = targetUserId,
    coin = coin
)

// UpdateTodoParams → UpdateTodoRequest
fun UpdateTodoParams.toRequest(): UpdateTodoRequest = UpdateTodoRequest(
    title = title,
    description = description,
    due_date = dueAt,
    importance = importance.apiValue,
    coin = coin
)

fun Todo.toTask(): Task = Task(
    id = id,
    title = title,
    description = description,
    importance = importance.toImportanceType(),
    isCompleted = isCompleted,
    isChildDone = isChildDone,
    isExpired = isExpired,
    dateTime = dueAt ?: 0L,
    createdAt = createdAt ?: 0L,
    targetUserId = targetUserId.orEmpty(),
    authorId = authorId.orEmpty(),
    canUpdate = canUpdate,
    coin = coin
)