package uz.tikoncha_parent.domain.use_case

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