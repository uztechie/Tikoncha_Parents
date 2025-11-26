package uz.tikoncha_parent.data.mapper

import uz.tikoncha_parent.presentation.model.ChatMemberUi
import uz.tikoncha_parent.common.DateTimeUtil
import uz.tikoncha_parent.data.local.AppSettings
import uz.tikoncha_parent.data.remote.model.ChatDto
import uz.tikoncha_parent.data.remote.model.ChatMemberDto
import uz.tikoncha_parent.data.remote.model.ChatMessageDto
import uz.tikoncha_parent.presentation.domain.model.LanguageType
import uz.tikoncha_parent.presentation.model.ChatMessageUi
import uz.tikoncha_parent.presentation.model.ChatType
import uz.tikoncha_parent.presentation.model.ChatUi
import uz.tikoncha_parent.presentation.profile.language.LanguagePrefs


fun ChatDto.toChatUi(): ChatUi {
    val lanCode = LanguagePrefs.loadOrDefault().languageCode
    val lang = LanguageType.getLangType(lanCode)
    val date = last_message?.created_at
    val millis = DateTimeUtil.toMillisUtc(date)
    val dateTime = DateTimeUtil.formatDateTimeMonthlyForChat(millis, lang)

    val type = when(type){
        "BOT" -> ChatType.BOT
        "PARENT_CHILD" -> ChatType.PARENT_CHILD
        "CLASS" -> ChatType.CLASS
        "SIBLINGS" -> ChatType.SIBLINGS
        else -> ChatType.NONE
    }

    return ChatUi(
        title = title,
        chatId = id,
        type = type,
        lastMessage = last_message?.text?:"",
        unreadCount = unread_count,
        dateTime = dateTime,
        lastMessageIsMine = last_message?.is_mine?:false,
        lastMessageIsRead = last_message?.is_read?:false,
        avatar = avatar.prepareAvatar()

    )
}

fun ChatMessageDto.toChatMessageUi(): ChatMessageUi {
    val dateMillis = DateTimeUtil.toMillisUtc(created_at)
    return ChatMessageUi(
        id = id,
        isMine = is_mine?:false,
        message = text,
        createdAt = dateMillis,
        time = DateTimeUtil.formatTime(dateMillis),
        isRead = is_read?:false,
        senderName = sender_name,
        senderAvatar = sender_avatar.prepareAvatar()
    )
}

fun ChatMemberDto.toChatMemberUi(): ChatMemberUi{
    return ChatMemberUi(
        id = user_id,
        name = first_name,
        avatar = avatar.prepareAvatar(),
        isOnline = online?:false,
        lastSeen = ""

    )
}