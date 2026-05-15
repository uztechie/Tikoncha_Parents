package uz.tikoncha_parent.domain.use_case.todo

import uz.tikoncha_parent.domain.model.todo.Importance

class CreateTodoParams(
    val targetUserId: String?,
    val title: String,
    val description: String?,
    val dueAt: Long?,
    val importance: Importance,
    val coin: Int
)

class UpdateTodoParams(
    val id: String,
    val title: String,
    val description: String?,
    val dueAt: Long?,
    val importance: Importance,
    val coin: Int
)