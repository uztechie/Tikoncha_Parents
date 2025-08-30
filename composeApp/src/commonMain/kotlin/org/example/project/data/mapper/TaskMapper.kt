@file:OptIn(ExperimentalTime::class)

package org.example.project.data.mapper

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.example.project.data.remote.model.TodoDto
import org.example.project.presentation.task.Task
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.time.Instant


fun TodoDto.toTask(): Task {
    val due = due_date.safeToLocalDateTime()
    val createdMillis = created_at?.safeToEpochMillis()?:0

    return Task(
        title = title,
        description = description,
        date = due.toUIData(),
        time = due.toUiTime(),
        dateTime = due_date.safeToEpochMillis(),
        importance = importance.toImportanceType(),
        is_completed = is_completed,
        progress = 0,
        created_at = createdMillis
    )
}





private fun LocalDateTime.toUIData(): String {
    val d = date
    return "${d.day.toString().padStart(2,'0')}." +
            "${d.month.toString().padStart(2,'0')}." +
            d.year.toString().padStart(4,'0')
}

private fun LocalDateTime.toUiTime(): String =
    "${hour.toString().padStart(2,'0')}:${minute.toString().padStart(2,'0')}"

private fun String.safeToEpochMillis(): Long =
    runCatching { Instant.parse(this).toEpochMilliseconds() }.getOrElse { 0L }

private fun String.safeToLocalDateTime(): LocalDateTime =
    runCatching { Instant.parse(this).toLocalDateTime(TimeZone.currentSystemDefault()) }
        .getOrElse { Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()) }
