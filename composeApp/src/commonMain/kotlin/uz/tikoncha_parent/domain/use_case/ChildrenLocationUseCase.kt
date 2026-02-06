package uz.tikoncha_parent.domain.use_case

import okio.IOException
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.iltimos_internetga_ulang
import tikoncha_parents.composeapp.generated.resources.kutilmagan_xatolik_qayta_urining
import tikoncha_parents.composeapp.generated.resources.server_connection_error
import uz.tikoncha_parent.data.remote.model.ChildrenLocationItemDto
import uz.tikoncha_parent.domain.model.Resource
import uz.tikoncha_parent.domain.repository.ChildRepository
import uz.tikoncha_parent.platform.Logger


class ChildrenLocationUseCase(
    private val repository: ChildRepository,
) {
    suspend operator fun invoke(): Resource<List<ChildrenLocationItemDto>> {
        return try {
            val response = repository.childrenLocation()
            if (response.success && response.data != null){
                Resource.Success(response.data.items)
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