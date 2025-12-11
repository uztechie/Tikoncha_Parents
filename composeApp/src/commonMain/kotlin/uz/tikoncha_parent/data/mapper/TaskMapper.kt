@file:OptIn(ExperimentalTime::class)

package uz.tikoncha_parent.data.mapper

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.number
import uz.tikoncha_parent.common.DateTimeUtil
import uz.tikoncha_parent.common.DateTimeUtil.fromServerToLocalDateTime
import uz.tikoncha_parent.common.DateTimeUtil.serverDateTimeToMillis
import uz.tikoncha_parent.common.DateTimeUtil.toUIData
import uz.tikoncha_parent.common.DateTimeUtil.toUiTime
import uz.tikoncha_parent.data.local.AppSettings
import uz.tikoncha_parent.data.remote.model.TodoDto
import uz.tikoncha_parent.presentation.task.Task
import kotlin.time.ExperimentalTime


fun TodoDto.toTask(): Task {
    val due = due_date.fromServerToLocalDateTime()

    return Task(
        title = title,
        description = description,
        date = due.toUIData(),
        time = due.toUiTime(),
        dateTime = DateTimeUtil.toMillisUtc(due_date),
        importance = importance.toImportanceType(),
        isCompleted = is_completed,
        progress = 0,
        createdAt =  DateTimeUtil.toMillisUtc(created_at),
        id = this.id ?:"",
        targetUserId = this.target_user_id?:"",
        authorId = this.author_id?:"",
        isMine = author_id == AppSettings.userId,
        coin = coin
    )
}

fun Task.toTodoDto(): TodoDto{
    return TodoDto(
        id = id,
        author_id = authorId,
        target_user_id = targetUserId,
        title = title,
        description = description,
        due_date = DateTimeUtil.formatToIsoString(dateTime, timeZone = TimeZone.currentSystemDefault()),
        importance = importance.toServerType(),
        is_completed = isCompleted,
        created_at = DateTimeUtil.formatToIsoString(createdAt, timeZone = TimeZone.currentSystemDefault()),
        modified_at = null,
        coin = coin
    )
}







