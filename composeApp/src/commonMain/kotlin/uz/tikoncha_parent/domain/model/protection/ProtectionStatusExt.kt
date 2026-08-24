package uz.tikoncha_parent.domain.model.protection

/** Javob kutayotgan so'rovlar soni: strict pending + logout process + delete process */
fun ProtectionStatus.pendingRequestCount(): Int {
    val strictPending = strictDisableRequest?.status.equals("pending", ignoreCase = true)
    val logoutPending = logoutRequest?.status.equals("process", ignoreCase = true)
    val deletePending = deleteRequest?.status.equals("process", ignoreCase = true)
    return listOf(strictPending, logoutPending, deletePending).count { it }
}

/** Joriy rejim uchun zarur bo'lib turib o'chiq qolgan ruxsatlar soni */
fun ProtectionStatus.missingRequiredPermissionCount(): Int {
    val mode = ChildMode.from(currentMode)
    if (mode == ChildMode.UNKNOWN) return 0
    return disabledKeys
        .mapNotNull { ChildPermission.from(it) }
        .count { it.minMode.ordinal <= mode.ordinal }
}