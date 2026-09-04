package uz.tikoncha_parent.domain.repository

import uz.tikoncha_parent.domain.model.app_error.Outcome
import uz.tikoncha_parent.domain.model.protection.AccountRequest
import uz.tikoncha_parent.domain.model.protection.AccountRequestStatus
import uz.tikoncha_parent.domain.model.protection.ChildRequest
import uz.tikoncha_parent.domain.model.protection.ProtectionStatus

interface ProtectionRepository {
    suspend fun protectionStatus(childId: String): Outcome<ProtectionStatus>

    suspend fun approveStrictDisableRequest(requestId: String): Outcome<ChildRequest>

    suspend fun rejectStrictDisableRequest(requestId: String): Outcome<ChildRequest>

    suspend fun updateAccountRequestStatus(
        requestId: String,
        status: AccountRequestStatus,
    ): Outcome<AccountRequest>
}