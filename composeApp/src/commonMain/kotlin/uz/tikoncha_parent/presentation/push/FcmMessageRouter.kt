package uz.tikoncha_parent.presentation.push

import uz.tikoncha_parent.domain.model.DeepLink
import uz.tikoncha_parent.domain.model.PayloadType

object FcmMessageRouter {


    fun handle(rawPayload: String?, fallbackTitle: String?, fallbackBody: String?) {
        val payload = parseFcmPayload(rawPayload)
        if (payload == null) {
            // payload yo'q yoki noto'g'ri — umumiy xabar sifatida yuboramiz
            FcmEventBus.emitGeneral(fallbackTitle, fallbackBody)
            DeepLinkBus.open(DeepLink.General(fallbackTitle, fallbackBody))
            return
        }

        val title = payload.title.ifBlank { fallbackTitle }
        val message = payload.message.ifBlank { fallbackBody }
        var deepLink: DeepLink = DeepLink.General(title, message)

        when (payload.type) {

            PayloadType.TODO -> {
                payload.body?.todo?.let { FcmEventBus.emitTodo(it, title, message) } ?: FcmEventBus.emitGeneral(title, message)
                deepLink = DeepLink.Todo(payload.body?.todo?.id ?: "")
            }
            PayloadType.NEWS -> {
                payload.body?.news?.let { FcmEventBus.emitNews(it, title, message) } ?: FcmEventBus.emitGeneral(title, message)
                val id = payload.body?.news?.id?:""
                deepLink = DeepLink.News(id)
            }
            PayloadType.CHAT -> {
                payload.body?.message?.let { FcmEventBus.emitChat(it, title, message) } ?: FcmEventBus.emitGeneral(title, message)
                deepLink = DeepLink.Chat(payload.body?.message?.chat_id ?: "", payload.body?.message?.chat_title, payload.body?.message?.text)
            }
            PayloadType.GENERAL -> {
                FcmEventBus.emitGeneral(title, message)
                deepLink = DeepLink.General(title, message)
            }
        }

        DeepLinkBus.open(deepLink)


    }
}