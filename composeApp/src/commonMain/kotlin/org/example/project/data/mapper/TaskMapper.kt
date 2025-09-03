@file:OptIn(ExperimentalTime::class)

package org.example.project.data.mapper

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.number
import kotlinx.datetime.toLocalDateTime
import org.example.project.common.DateTimeUtil
import org.example.project.common.DateTimeUtil.fromServerToLocalDateTime
import org.example.project.common.DateTimeUtil.serverDateTimeToMillis
import org.example.project.data.local.AppSettings
import org.example.project.data.remote.model.TodoDto
import org.example.project.presentation.task.Task
import kotlin.time.ExperimentalTime


fun TodoDto.toTask(): Task {
    val due = due_date.fromServerToLocalDateTime()

    return Task(
        title = title,
        description = description,
        date = due.toUIData(),
        time = due.toUiTime(),
        dateTime = due_date.serverDateTimeToMillis(),
        importance = importance.toImportanceType(),
        isCompleted = is_completed,
        progress = 0,
        createdAt = created_at.serverDateTimeToMillis(),
        id = this.id ?:"",
        targetUserId = this.target_user_id?:"",
        authorId = this.author_id?:"",
        isMine = author_id == AppSettings.userId
    )
}

fun Task.toTodoDto(): TodoDto{
    return TodoDto(
        id = id,
        author_id = authorId,
        target_user_id = targetUserId,
        title = title,
        description = description,
        due_date = DateTimeUtil.formatToIsoString(dateTime),
        importance = importance.toServerType(),
        is_completed = isCompleted,
        created_at = DateTimeUtil.formatToIsoString(createdAt),
        modified_at = null
    )
}




private fun LocalDateTime.toUIData(): String {
    val d = date
    return "${d.day.toString().padStart(2,'0')}." +
            "${d.month.number.toString().padStart(2,'0')}." +
            d.year.toString().padStart(4,'0')
}

private fun LocalDateTime.toUiTime(): String =
    "${hour.toString().padStart(2,'0')}:${minute.toString().padStart(2,'0')}"



