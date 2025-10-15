package uz.tikoncha_parent.domain.use_case.chat

import uz.tikoncha_parent.domain.model.DeepLink
import uz.tikoncha_parent.domain.model.FcmPayload
import uz.tikoncha_parent.domain.model.PayloadType

class MapPayloadToDeepLinkUseCase {
    operator fun invoke(
        payload: FcmPayload,
        fallbackTitle: String?,
        fallbackBody: String?
    ): DeepLink? {
        val title = payload.title.ifBlank { fallbackTitle }
        val message = payload.message.ifBlank { fallbackBody }

        return when (payload.type) {
            PayloadType.CHAT -> {
                val m = payload.body?.message ?: return DeepLink.General(title, message)
                val id = m.chat_id ?: return DeepLink.General(title, message)
                DeepLink.Chat(
                    chatId = id,
                    chatTitle = m.chat_title,
                    text = m.text
                )
            }
            PayloadType.NEWS -> {
                val id = payload.body?.news?.id
                    ?: return DeepLink.General(title, message)
                DeepLink.News(id)
            }
            PayloadType.TODO -> {
                val id = payload.body?.todo?.id ?: return DeepLink.General(title, message)
                DeepLink.Todo(id)
            }
            PayloadType.GENERAL -> DeepLink.General(title, message)
        }
    }
}