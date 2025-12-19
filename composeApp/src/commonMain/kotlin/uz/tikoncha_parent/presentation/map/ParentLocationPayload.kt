package uz.tikoncha_parent.presentation.map

import kotlinx.serialization.Serializable
import uz.tikoncha_parent.common.DateTimeUtil
import uz.tikoncha_parent.data.local.AppSettings
import uz.tikoncha_parent.data.remote.model.ChildrenLocationItemDto
import uz.tikoncha_parent.domain.model.SubscriptionLimit
import uz.tikoncha_parent.domain.model.SubscriptionType
import uz.tikoncha_parent.presentation.domain.model.LanguageType
import uz.tikoncha_parent.presentation.profile.language.LanguagePrefs

@Serializable
data class ParentLocationPayload(
    val name: String,
    val lat: Double?,
    val lng: Double?,
    val children: List<ChildLocationPayload>
)

@Serializable
data class ChildLocationPayload(
    val name: String,
    val updated_at: String,
    val lat: Double?,
    val lng: Double?,
    val child_user_id: String?,
    val subscription: String?

)

fun List<SubscriptionLimit>.asSubscriptionMap(): Map<String, SubscriptionType> =
    associate { it.childId to it.subscriptionType }

fun ChildrenLocationItemDto.toPayload(
    subMap: Map<String, SubscriptionType>
): ChildLocationPayload {
    val subType = child_user_id?.let(subMap::get)

    val languageCode = LanguageType.getLangType(LanguagePrefs.loadOrDefault().languageCode)
    val dateMillis = DateTimeUtil.toMillisUtc(updated_at)
    val date = DateTimeUtil.formatDateTimeMonthlyForMap(dateMillis, languageCode)

    return ChildLocationPayload(
        name = first_name.orEmpty(),
        updated_at = date,
        lat = lat,
        lng = lng,
        child_user_id = child_user_id,
        subscription = subType?.name
    )
}

fun List<ChildrenLocationItemDto>.toPayloads(): List<ChildLocationPayload> {
    val subMap = AppSettings.subscriptionLimitList.asSubscriptionMap()
    return map { it.toPayload(subMap) }
}