//@file:OptIn(ExperimentalTime::class)
//
//package uz.tikoncha_parent.data.mapper
//
//import kotlinx.datetime.TimeZone
//import kotlinx.datetime.toLocalDateTime
//import uz.tikoncha_parent.common.DateTimeUtil.toUIData
//import uz.tikoncha_parent.common.DateTimeUtil.toUiTime
//import uz.tikoncha_parent.data.local.AppSettings
//import uz.tikoncha_parent.data.remote.model.todo.TodoDto
//import uz.tikoncha_parent.presentation.task.model.Task
//import kotlin.time.ExperimentalTime
//import kotlin.time.Instant
//
//fun TodoDto.toTask(): Task {
//    val tz = TimeZone.currentSystemDefault()
//    val dueLocal = Instant.fromEpochMilliseconds(due_date).toLocalDateTime(tz)
//
//    return Task(
//        id = id,
//        title = title,
//        description = description,
//        date = dueLocal.toUIData(),
//        time = dueLocal.toUiTime(),
//        dateTime = due_date,
//        importance = importance.toImportanceType(),
//        isCompleted = is_completed,
//        isChildDone = is_child_done,
//        isExpired = is_expired,
//        isMine = author_id == AppSettings.userId,
//        authorId = author_id ?: "",
//        targetUserId = target_user_id ?: "",
//        progress = 0,
//        createdAt = created_at ?: 0L,
//        coin = coin
//    )
//}
//
//fun Task.toTodoDto(): TodoDto {
//    return TodoDto(
//        id = id,
//        author_id = authorId,
//        target_user_id = targetUserId,
//        title = title,
//        description = description,
//        due_date = dateTime,                  // ← endi to'g'ridan-to'g'ri millis
//        importance = importance.toServerType(),
//        is_completed = isCompleted,
//        is_child_done = isChildDone,
//        is_expired = isExpired,
//        created_at = createdAt.takeIf { it > 0L },
//        modified_at = null,
//        coin = coin
//    )
//}