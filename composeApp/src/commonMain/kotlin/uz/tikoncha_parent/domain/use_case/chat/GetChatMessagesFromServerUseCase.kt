package uz.tikoncha_parent.domain.use_case.chat

import uz.tikoncha_parent.common.DateTimeUtil
import uz.tikoncha_parent.domain.model.app_error.Outcome
import uz.tikoncha_parent.domain.model.chat.MessagesPage
import uz.tikoncha_parent.domain.repository.ChatRepository

class GetChatMessagesFromServerUseCase(
    private val repository: ChatRepository
) {
    suspend operator fun invoke(
        chatId: String,
        sinceId: String? = null,
        sinceTs: String? = null,
        limit: Int = 20
    ): Outcome<MessagesPage> {
        val params = HashMap<String, Any>().apply {
            put("chat_id", chatId)
            put("limit", limit)
            sinceId?.let { put("since_id", it) }
            sinceTs?.let { put("since_ts", it) }
        }

        return when (val res = repository.chatMessages(params)) {
            is Outcome.Failure -> res
            is Outcome.Success -> {
                val items = res.data.items

                // pagination uchun cursor: eng eski message
                val oldest = items.minByOrNull { DateTimeUtil.toMillisUtc(it.created_at) }

                Outcome.Success(
                    MessagesPage(
                        items = items,
                        sinceId = oldest?.id,
                        sinceTs = oldest?.created_at,
                        hasMore = items.size >= limit && oldest != null
                    )
                )
            }
        }
    }
}