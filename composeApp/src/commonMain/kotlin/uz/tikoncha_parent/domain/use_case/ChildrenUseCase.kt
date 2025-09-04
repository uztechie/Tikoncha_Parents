package uz.tikoncha_parent.domain.use_case

import uz.tikoncha_parent.data.remote.model.UserInfoDto
import uz.tikoncha_parent.domain.model.Resource
import uz.tikoncha_parent.domain.repository.ChildRepository
import uz.tikoncha_parent.platform.Logger


class ChildrenUseCase(
    private val repository: ChildRepository,
) {
    suspend operator fun invoke(): Resource<List<UserInfoDto>> {
        return try {
            val response = repository.children()
            if (response.success && response.data != null){
                Resource.Success(response.data.children)
            }
            else{
                Resource.Error(response.error?: "")
            }


        }

        catch (e: Exception){
            e.printStackTrace()
            Logger.e("XATOOOO", "message: ",e)
            Resource.Error("Xatolik")
        }

    }
}