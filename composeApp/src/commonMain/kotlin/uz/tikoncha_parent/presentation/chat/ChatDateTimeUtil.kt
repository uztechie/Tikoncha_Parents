@file:OptIn(ExperimentalTime::class)

package uz.tikoncha_parent.presentation.chat
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.toLocalDateTime
import uz.tikoncha_parent.common.DateTimeUtil.localized
import uz.tikoncha_parent.data.local.AppSettings
import uz.tikoncha_parent.platform.Logger
import uz.tikoncha_parent.presentation.chat.model.ChatDateLabel
import uz.tikoncha_parent.presentation.domain.model.LanguageType
import uz.tikoncha_parent.presentation.profile.language.LanguageController
import uz.tikoncha_parent.presentation.profile.language.LanguagePrefs
import kotlin.time.Clock
import kotlin.time.Instant
import kotlin.time.ExperimentalTime


object ChatDateTimeUtil {


    private fun Int.pad2(): String = this.toString().padStart(2, '0')

    fun formatChatDate(
        millis: Long,
        timeZone: TimeZone = TimeZone.currentSystemDefault(),
        langType : LanguageType
    ): ChatDateLabel {
        if (millis == 0L) return ChatDateLabel.Unknown
        val clock: Clock = Clock.System

        val instant = Instant.fromEpochMilliseconds(millis)
        val ldt = instant.toLocalDateTime(timeZone)
        val date = ldt.date

        val today = clock.now().toLocalDateTime(timeZone).date
        val yesterday = today.minus(DatePeriod(days = 1))

        Logger.d("formatChatDate", "millis=$millis,  date=$date")

        return when (date) {
            today -> ChatDateLabel.Time("${ldt.hour.pad2()}:${ldt.minute.pad2()}")
            yesterday -> ChatDateLabel.Yesterday
            else -> {
                ChatDateLabel.Date("${date.day} ${date.month.localized(langType)}")
            }
        }
    }

    fun formatChatRoomDateHeader(
        epochDay: Long,
        langType : LanguageType,
        timeZone: TimeZone = TimeZone.currentSystemDefault(),
    ): ChatDateLabel {
        val clock: Clock = Clock.System
        val date = LocalDate.fromEpochDays(epochDay.toInt())

        val today = clock.now().toLocalDateTime(timeZone).date
        val yesterday = today.minus(DatePeriod(days = 1))

        return when (date) {
            today -> ChatDateLabel.Today        // yoki o‘zingizdagi label
            yesterday -> ChatDateLabel.Yesterday
            else -> ChatDateLabel.Date(
                "${date.day} ${date.month.localized(langType)}"
            )
        }
    }

    fun millisToHHmm(
        millis: Long,
        timeZone: TimeZone = TimeZone.currentSystemDefault()
    ): String {
        val ldt = Instant.fromEpochMilliseconds(millis)
            .toLocalDateTime(timeZone)

        return "${ldt.hour.pad2()}:${ldt.minute.pad2()}"
    }

}