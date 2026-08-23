package uz.tikoncha_parent.data.repository

import uz.tikoncha_parent.data.local.AppSettings
import uz.tikoncha_parent.domain.model.Session
import uz.tikoncha_parent.domain.model.UserInfo
import uz.tikoncha_parent.domain.repository.SessionRepository

class SessionRepositoryImpl : SessionRepository {
    override fun save(session: Session, phone: String?) {
        val src = session.userInfo?.phoneNumber ?: phone

        AppSettings.refreshToken  = session.refreshToken
        AppSettings.accessToken   = session.accessToken
        AppSettings.hasUserLogin  = session.userInfo != null
        AppSettings.userId        = session.userId
        AppSettings.userInfo      = session.userInfo
        AppSettings.isTestAccount = src?.startsWith("+99811") == true
    }
    override fun clear() = AppSettings.clearSession()
    override val isLoggedIn: Boolean get() = AppSettings.hasUserLogin

    override val currentUserId: String get() = AppSettings.userId
    override fun saveUserInfo(userInfo: UserInfo) {
        AppSettings.userInfo = userInfo
        AppSettings.hasUserLogin = true
    }
}