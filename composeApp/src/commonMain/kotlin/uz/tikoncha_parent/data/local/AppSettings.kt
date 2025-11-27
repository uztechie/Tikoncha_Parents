package uz.tikoncha_parent.data.local

import com.russhwolf.settings.Settings
import com.russhwolf.settings.get
import com.russhwolf.settings.set
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import uz.tikoncha_parent.domain.model.SubscriptionLimit
import uz.tikoncha_parent.domain.model.UserInfo

object AppSettings {
    private val settings = Settings()


    var fcmToken: String
        get() = settings.get("fcmToken") ?: ""
        set(value) = settings.set("fcmToken", value)


    var refreshToken: String
        get() = settings.get("refreshToken") ?: ""
        set(value) = settings.set("refreshToken", value)

    var accessToken: String
        get() = settings.get("accessToken") ?: ""
        set(value) = settings.set("accessToken", value)

    var profileImageUrl: String
        get() = settings.get("profileImageUrl") ?: ""
        set(value) = settings.set("profileImageUrl", value)

    var userId: String
        get() = settings.get("userId") ?: ""
        set(value) = settings.set("userId", value)


    var selectedChildId: String
        get() = settings.get("selectedChildId") ?: ""
        set(value) = settings.set("selectedChildId", value)


    var policyId: String
        get() = settings.get("policyId") ?: ""
        set(value) = settings.set("policyId", value)

    var hasUserLogin: Boolean
        get() = settings.get("hasUserLogin") ?: false
        set(value) = settings.set("hasUserLogin", value)


    var isFirstLaunch: Boolean
        get() = settings.get("isFirstLaunch") ?: true
        set(value) = settings.set("isFirstLaunch", value)


    var userInfo: UserInfo?
        get() {
            val json = settings.getStringOrNull("userInfo") ?: ""
            return try {
                Json.decodeFromString<UserInfo>(json)
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
        set(value){
            if (value == null){
                settings.remove("userInfo")
            }
            else{
                val json = Json.encodeToString(value)
                settings.putString("userInfo", json)
            }
        }


    var children: List<UserInfo>
        get() {
            val json = settings.getStringOrNull("children") ?: return emptyList()
            return try {
                Json.decodeFromString(json)
            } catch (e: Exception) {
                emptyList()
            }
        }
        set(value) {
            val json = Json.encodeToString(value)
            settings.putString("children", json)
        }


    var subscriptionLimit: SubscriptionLimit
        get() {
            val json = settings.getStringOrNull("SubscriptionLimit") ?: return SubscriptionLimit()
            return try {
                Json.decodeFromString(json)
            } catch (e: Exception) {
                SubscriptionLimit()
            }
        }
        set(value) {
            val json = Json.encodeToString(value)
            settings.putString("SubscriptionLimit", json)
        }

}