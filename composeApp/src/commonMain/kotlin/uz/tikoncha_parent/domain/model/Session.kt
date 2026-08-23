package uz.tikoncha_parent.domain.model

data class Session(
    val accessToken: String,
    val refreshToken: String,
    val userId: String,
    /** null — foydalanuvchi hali ro'yxatdan o'tmagan. */
    val userInfo: UserInfo?,
) {
    val needsRegistration: Boolean get() = userInfo == null
}