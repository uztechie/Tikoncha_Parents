package uz.tikoncha_parent.domain.model.protection

import uz.tikoncha_parent.data.remote.model.protection.ProtectionStatusData

/** Javob kutayotgan so'rovlar soni: strict pending + logout process + delete process */
fun ProtectionStatusData.pendingRequestCount(): Int {
    val strictPending = strictDisableRequest?.status.equals("pending", ignoreCase = true)
    val logoutPending = logoutRequest?.status.equals("process", ignoreCase = true)
    val deletePending = deleteRequest?.status.equals("process", ignoreCase = true)
    return listOf(strictPending, logoutPending, deletePending).count { it }
}

/** Joriy rejim uchun zarur bo'lib turib o'chiq qolgan ruxsatlar soni */
fun ProtectionStatusData.missingRequiredPermissionCount(): Int {
    val mode = ChildMode.from(modeStatus?.currentMode)
    if (mode == ChildMode.UNKNOWN) return 0
    return modeStatus?.disabled.orEmpty()
        .mapNotNull { ChildPermission.from(it) }
        .count { it.minMode.ordinal <= mode.ordinal }
}