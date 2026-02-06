package uz.tikoncha_parent.domain.use_case

import okio.IOException
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.iltimos_internetga_ulang
import tikoncha_parents.composeapp.generated.resources.kutilmagan_xatolik_qayta_urining
import tikoncha_parents.composeapp.generated.resources.server_connection_error
import uz.tikoncha_parent.data.remote.model.UserInfoDto
import uz.tikoncha_parent.domain.model.Resource
import uz.tikoncha_parent.domain.repository.ChildRepository


class ChildrenUseCase(
    private val repository: ChildRepository,
) {
    suspend operator fun invoke(): Resource<List<UserInfoDto>> {
        return try {
            val response = repository.children()
            if (response.success && response.data != null){
                Resource.Success(response.data.children)
            }
            else {
                Resource.Error(
                    message = response.error,
                    resId = Res.string.server_connection_error
                )
            }
        } catch (e: IOException) {
            Resource.Error(
                resId = Res.string.iltimos_internetga_ulang,
                cause = e
            )
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(
                resId = Res.string.kutilmagan_xatolik_qayta_urining,
                cause = e
            )
        }
    }
}