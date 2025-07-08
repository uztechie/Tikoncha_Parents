package org.example.project.domain.model

data class Chat(
    val isAI: Boolean = false,
    val id:Int,
    val avatar: String? = null,
    val title: String,
    val lastMessage: String,
    val lastSender: User? = null,
    val time: String,
    val unReadCount:Int,
    val isRead: Boolean
)
