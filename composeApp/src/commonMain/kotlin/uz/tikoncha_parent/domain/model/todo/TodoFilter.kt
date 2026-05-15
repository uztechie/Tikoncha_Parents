package uz.tikoncha_parent.domain.model.todo

data class TodoFilter(
    val isCompleted: Boolean? = null,
    val isChildDone: Boolean? = null,
    val isExpired: Boolean? = null,
    val importance: Importance? = null
)

data class TodosQuery(
    val targetUserId: String?,
    val filter: TodoFilter,
    val limit: Int,
    val offset: Int
)

enum class TaskOwnerTab { MINE, CHILDREN }

enum class TaskFilterChip { IN_PROGRESS, DONE_BY_CHILD, OVERDUE }