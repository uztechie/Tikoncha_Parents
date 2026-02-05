package uz.tikoncha_parent.domain.use_case

import okio.IOException
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.iltimos_internetga_ulang
import tikoncha_parents.composeapp.generated.resources.kutilmagan_xatolik_qayta_urining
import tikoncha_parents.composeapp.generated.resources.server_connection_error
import uz.tikoncha_parent.data.mapper.toLogoutUi
import uz.tikoncha_parent.domain.model.Resource
import uz.tikoncha_parent.domain.repository.ParentRequestsRepository
import uz.tikoncha_parent.presentation.new_home.logout.ParentRequestUi

class UpdateParentRequestStatusUseCase(
    private val repository: ParentRequestsRepository
) {
    suspend operator fun invoke(
        requestId: String,
        status: String
    ): Resource<ParentRequestUi> {
        return try {
            val response = repository.updateRequest(requestId, status)
            if (!response.success || response.data == null){
                Resource.Error(
                    message = response.error,
                    resId = Res.string.server_connection_error
                )
            } else {
                Resource.Success(response.data.toLogoutUi())
            }
        } catch (e: IOException) {
            Resource.Error(
                resId = Res.string.iltimos_internetga_ulang,
                cause = e
            )
        } catch (e: Exception) {
            Resource.Error(
                message = e.message ?: "Unknown error",
                resId = Res.string.kutilmagan_xatolik_qayta_urining
            )
        }
    }
}