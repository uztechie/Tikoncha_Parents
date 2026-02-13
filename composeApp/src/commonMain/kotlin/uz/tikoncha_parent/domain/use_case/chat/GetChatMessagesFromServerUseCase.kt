package uz.tikoncha_parent.domain.use_case.chat

import okio.IOException
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.iltimos_internetga_ulang
import tikoncha_parents.composeapp.generated.resources.kutilmagan_xatolik_qayta_urining
import tikoncha_parents.composeapp.generated.resources.server_connection_error
import uz.tikoncha_parent.common.DateTimeUtil
import uz.tikoncha_parent.data.remote.model.ChatMessageDto
import uz.tikoncha_parent.domain.model.Resource
import uz.tikoncha_parent.domain.model.chat.MessagesPage
import uz.tikoncha_parent.domain.repository.ChatRepository
import kotlin.compareTo

class GetChatMessagesFromServerUseCase(
    private val repository: ChatRepository
) {
    suspend operator fun invoke(
        chatId: String,
        sinceId: String? = null,
        sinceTs: String? = null,
        limit: Int = 20
    ): Resource<MessagesPage> {
        return try {

            val params = HashMap<String, Any>().apply {
                put("chat_id", chatId)
                put("limit", limit)
                sinceId?.let { put("since_id", it) }
                sinceTs?.let { put("since_ts", it) }
            }

            val response = repository.chatMessages(params)
            if (response.success && response.data != null) {
                val items = response.data.items

                // pagination uchun cursor: eng eski message
                val oldest = items.minByOrNull { DateTimeUtil.toMillisUtc(it.created_at) }

                val nextSinceId = oldest?.id
                // sinceTs backend qanday kutadi? ko‘p hollarda created_at string ham ishlaydi
                val nextSinceTs = oldest?.created_at

                val hasMore = items.size >= limit && oldest != null

                Resource.Success(
                    MessagesPage(
                        items = items,
                        sinceId = nextSinceId,
                        sinceTs = nextSinceTs,
                        hasMore = hasMore
                    )
                )
            } else {
                Resource.Error(Res.string.server_connection_error)
            }


        } catch (e: IOException) {
            Resource.Error(
                resId = Res.string.iltimos_internetga_ulang,
                cause = e
            )
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(
                resId = Res.string.kutilmagan_xatolik_qayta_urining,
                cause = e
            )
        }
    }
}