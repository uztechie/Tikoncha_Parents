package uz.tikoncha_parent.domain.model.todo

data class Todo(
    val id: String,
    val authorId: String?,
    val targetUserId: String?,
    val title: String,
    val description: String,
    val dueAt: Long?,             // epoch milliseconds
    val importance: Importance,
    val isChildDone: Boolean,     // farzand bajargan
    val isCompleted: Boolean,     // ota-ona tasdiqlagan (verified)
    val isExpired: Boolean,       // server-side
    val createdAt: Long?,
    val modifiedAt: Long?,
    val coin: Int
)

data class ChildUser(
    val id: String,
    val fullName: String,
    val avatarUrl: String?,
    val coin: Int
)

data class PagedResult<T>(
    val items: List<T>,
    val total: Int,
    val hasMore: Boolean,
    val limit: Int,
    val offset: Int
)