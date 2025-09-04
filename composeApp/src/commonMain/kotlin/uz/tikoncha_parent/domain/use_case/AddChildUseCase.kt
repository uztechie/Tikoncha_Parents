package uz.tikoncha_parent.domain.use_case

import uz.tikoncha_parent.data.remote.model.AddChildRequest
import uz.tikoncha_parent.domain.model.Resource
import uz.tikoncha_parent.domain.repository.ChildRepository
import uz.tikoncha_parent.platform.Logger
import kotlin.code


class AddChildUseCase(
    private val repository: ChildRepository,
) {
    suspend operator fun invoke(request: AddChildRequest): Resource<String> {
        return try {
            val response = repository.addChild(request)
            if (response.success && response.data != null){
                Resource.Success(response.data.code)
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