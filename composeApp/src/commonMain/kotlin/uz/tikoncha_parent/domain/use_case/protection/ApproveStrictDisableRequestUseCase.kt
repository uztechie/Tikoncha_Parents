package uz.tikoncha_parent.domain.use_case.protection

import okio.IOException
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.iltimos_internetga_ulang
import tikoncha_parents.composeapp.generated.resources.kutilmagan_xatolik_qayta_urining
import tikoncha_parents.composeapp.generated.resources.server_connection_error
import uz.tikoncha_parent.data.remote.model.protection.ChildRequestDto
import uz.tikoncha_parent.domain.model.Resource
import uz.tikoncha_parent.domain.repository.ProtectionRepository

class ApproveStrictDisableRequestUseCase(
    private val repository: ProtectionRepository,
) {
    suspend operator fun invoke(requestId: String): Resource<ChildRequestDto> {
        return try {
            val response = repository.approveStrictDisableRequest(requestId)
            if (response.success && response.data != null) {
                Resource.Success(response.data)
            } else {
                Resource.Error(
                    message = response.error,
                    resId = Res.string.server_connection_error,
                )
            }
        } catch (e: IOException) {
            Resource.Error(
                resId = Res.string.iltimos_internetga_ulang,
                cause = e,
            )
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(
                resId = Res.string.kutilmagan_xatolik_qayta_urining,
                cause = e,
            )
        }
    }
}

