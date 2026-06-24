package uz.tikoncha_parent.domain.use_case

import okio.IOException
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.kutilmagan_xatolik_qayta_urining
import tikoncha_parents.composeapp.generated.resources.server_connection_error
import uz.tikoncha_parent.data.remote.model.UnlinkChildRequest
import uz.tikoncha_parent.domain.model.Resource
import uz.tikoncha_parent.domain.repository.ChildRepository

class UnlinkChildUseCase(
    private val repository: ChildRepository
) {
    suspend operator fun invoke(
        childUserId: String,
        parentUserId: String
    ): Resource<String> {
        return try {
            val response = repository.unlinkChild(
                UnlinkChildRequest(
                    child_user_id = childUserId,
                    parent_user_id = parentUserId
                )
            )
            if (response.success) {
                Resource.Success(response.data?.message?:"")
            } else {
                Resource.Error(
                    message = response.error,
                    resId = Res.string.server_connection_error
                )
            }
        } catch (e: IOException) {
            e.printStackTrace()
            Resource.Error(
                Res.string.kutilmagan_xatolik_qayta_urining,
                cause = e
            )
        }
    }
}