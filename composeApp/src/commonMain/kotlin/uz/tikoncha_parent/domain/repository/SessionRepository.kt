package uz.tikoncha_parent.domain.repository

import uz.tikoncha_parent.domain.model.Session
import uz.tikoncha_parent.domain.model.UserInfo

interface SessionRepository {
    fun save(session: Session, phone: String? = null)
    fun clear()
    val isLoggedIn: Boolean

    val currentUserId: String
    fun saveUserInfo(userInfo: UserInfo)
}