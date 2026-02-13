@file:OptIn(ExperimentalTime::class)

package uz.tikoncha_parent.presentation.chat.chat_room

import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import uz.tikoncha_parent.common.DateTimeUtil
import uz.tikoncha_parent.domain.model.ChatMessageItem
import uz.tikoncha_parent.presentation.chat.ChatDateTimeUtil
import uz.tikoncha_parent.presentation.chat.model.ChatDateLabel
import uz.tikoncha_parent.presentation.domain.model.LanguageType
import uz.tikoncha_parent.presentation.model.ChatMessageUi
import uz.tikoncha_parent.presentation.profile.language.LanguagePrefs
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

object ChatGrouper {

    /**
     * allDesc: newest -> oldest
     * reverseLayout=true uchun mos:
     *  [today msgs..., DateHeader(today), yesterday msgs..., DateHeader(yesterday), ...]
     */
    fun groupDescForReverseLayout(
        allDesc: List<ChatMessageUi>,
        langType: LanguageType
    ): List<ChatMessageItem> {
        if (allDesc.isEmpty()) return emptyList()

        val tz = TimeZone.currentSystemDefault()
        val out = ArrayList<ChatMessageItem>(allDesc.size * 2)

        var currentEpochDay: Long? = null

        fun emitHeader(epochDay: Long) {
            out += ChatMessageItem.DateHeader(
                epochDay = epochDay,
                dateLabel = ChatDateTimeUtil.formatChatRoomDateHeader(
                    epochDay = epochDay,
                    langType = langType,
                    timeZone = tz
                )
            )
        }

        for (m in allDesc) {
            val epochDay = Instant.fromEpochMilliseconds(m.createdAt)
                .toLocalDateTime(tz)
                .date
                .toEpochDays()
                .toLong()

            if (currentEpochDay == null) {
                currentEpochDay = epochDay
            } else if (epochDay != currentEpochDay) {
                // oldingi kun tugadi -> header chiqaramiz
                emitHeader(currentEpochDay)
                currentEpochDay = epochDay
            }

            out += ChatMessageItem.Message(m)
        }

        // oxirgi guruh header’i
        emitHeader(currentEpochDay!!)

        return out
    }
}
