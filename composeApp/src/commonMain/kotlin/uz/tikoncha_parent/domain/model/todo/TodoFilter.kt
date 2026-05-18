package uz.tikoncha_parent.domain.model.todo

data class TodoFilter(
    val isCompleted: Boolean? = null,
    val isChildDone: Boolean? = null,
    val isExpired: Boolean? = null,
    val createdByRole: CreatedByRole = CreatedByRole.PARENT,
    val importance: Importance? = null
)

data class TodosQuery(
    val targetUserId: String?,
    val filter: TodoFilter,
    val limit: Int,
    val offset: Int
)

enum class CreatedByRole(val apiValue: String) {
    PARENT("PARENT"),
    CHILD("CHILD");

    companion object {
        fun fromApi(value: String?): CreatedByRole? =
            entries.firstOrNull { it.apiValue.equals(value, ignoreCase = true) }
    }
}


enum class TaskFilterChip { IN_PROGRESS, DONE_BY_CHILD, OVERDUE }