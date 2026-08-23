import uz.tikoncha_parent.data.mapper.toPermissionIssue
import uz.tikoncha_parent.data.remote.PermissionStatusApiService
import uz.tikoncha_parent.data.remote.app_error.ApiErrorMapper
import uz.tikoncha_parent.data.remote.model.permission_status.PermissionStatusRequest
import uz.tikoncha_parent.data.repository.apiCall
import uz.tikoncha_parent.domain.model.app_error.ErrorCause
import uz.tikoncha_parent.domain.model.app_error.Outcome
import uz.tikoncha_parent.domain.model.permission_status.PermissionIssue
import uz.tikoncha_parent.domain.model.permission_status.PermissionStatusType
import uz.tikoncha_parent.domain.repository.PermissionStatusRepository

class PermissionStatusRepositoryImpl(val api: PermissionStatusApiService) :
    PermissionStatusRepository {

    override suspend fun permissionStatus(
        childId: String,
        state: PermissionStatusType,
    ): Outcome<List<PermissionIssue>> =
        apiCall(TAG) {
            val r = api.permissionStatus(
                PermissionStatusRequest(userId = childId, state = state.name)
            )
            val body = r.data
            when {
                r.success && body != null ->
                    Outcome.Success(body.issues.map { it.toPermissionIssue() })
                r.success -> Outcome.Failure(ErrorCause.InvalidResponse)
                else -> Outcome.Failure(ApiErrorMapper.fromCode(r.code), r.error)
            }
        }

    private companion object { const val TAG = "PermissionStatusRepository" }
}