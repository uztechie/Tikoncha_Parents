package uz.tikoncha_parent.presentation.protection

import kotlinx.datetime.Instant
import uz.tikoncha_parent.data.remote.model.protection.AccountRequestDto
import uz.tikoncha_parent.data.remote.model.protection.ChildRequestDto
import uz.tikoncha_parent.domain.model.protection.ChildMode
import uz.tikoncha_parent.domain.model.protection.ChildPermission
import uz.tikoncha_parent.domain.model.protection.StrictMethod
import uz.tikoncha_parent.presentation.ui_state.ResponseState

data class ProtectionState(
    val responseState: ResponseState<Nothing> = ResponseState.Idle,
    val isRefreshing: Boolean = false,

    // hero karta
    val mode: ChildMode = ChildMode.UNKNOWN,
    val strictMethod: StrictMethod? = null,          // faqat mode == STRICT
    val unlockData: String? = null,                  // faqat SECRET_CODE
    val isCodeVisible: Boolean = false,
    val lastSyncAt: Instant? = null,
    val isOnline: Boolean = false,

    // ruxsatlar
    val enabledPermissions: Set<ChildPermission> = emptySet(),
    val disabledPermissions: Set<ChildPermission> = emptySet(),

    // farzand so'rovlari
    val strictDisableRequest: ChildRequestDto? = null,
    val logoutRequest: AccountRequestDto? = null,
    val deleteRequest: AccountRequestDto? = null,
    val remainingSeconds: Int = 0,
    val actionInProgressId: String? = null,
    val actionResponseState: ResponseState<Nothing> = ResponseState.Idle,
) {
    /** Qurilma haqiqatda xabar bergan ruxsatlar (Xiaomi OVERLAY_POPUP faqat Xiaomi'da keladi) */
    private val reportedPermissions: Set<ChildPermission>
        get() = enabledPermissions + disabledPermissions

    /** Joriy rejim uchun zarur ruxsatlar */
    val requiredPermissions: List<ChildPermission>
        get() = if (mode == ChildMode.UNKNOWN) emptyList()
        else ChildPermission.entries.filter {
            it.minMode.ordinal <= mode.ordinal && it in reportedPermissions
        }

    /** Yuqoriroq rejimlar uchun — hozircha shart emas (xira qatorlar) */
    val higherTierPermissions: List<ChildPermission>
        get() = ChildPermission.entries.filter {
            (mode == ChildMode.UNKNOWN || it.minMode.ordinal > mode.ordinal) &&
                it in reportedPermissions
        }

    /** Zarur bo'lib turib berilmaganlar — warning banner */
    val missingRequiredPermissions: List<ChildPermission>
        get() = requiredPermissions.filter { it in disabledPermissions }

    val grantedRequiredCount: Int
        get() = requiredPermissions.count { it in enabledPermissions }

    val isStrictRequestPending: Boolean
        get() = strictDisableRequest?.status.equals("pending", ignoreCase = true)

    val isLogoutRequestPending: Boolean
        get() = logoutRequest?.status.equals("process", ignoreCase = true)

    val isDeleteRequestPending: Boolean
        get() = deleteRequest?.status.equals("process", ignoreCase = true)

    /** Kartadagi "N ta yangi" pill */
    val pendingRequestCount: Int
        get() = listOf(isStrictRequestPending, isLogoutRequestPending, isDeleteRequestPending)
            .count { it }

    /** So'rovlar kartasi ko'rinsinmi */
    val showRequestsCard: Boolean
        get() = strictDisableRequest != null || isLogoutRequestPending || isDeleteRequestPending
}