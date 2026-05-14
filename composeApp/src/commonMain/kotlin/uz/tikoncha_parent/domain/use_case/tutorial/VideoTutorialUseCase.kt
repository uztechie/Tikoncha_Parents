package uz.tikoncha_parent.domain.use_case.tutorial

import okio.IOException
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.iltimos_internetga_ulang
import tikoncha_parents.composeapp.generated.resources.kutilmagan_xatolik_qayta_urining
import tikoncha_parents.composeapp.generated.resources.server_connection_error
import uz.tikoncha_parent.data.remote.model.tutorial.VideoTutorialData
import uz.tikoncha_parent.domain.model.Resource
import uz.tikoncha_parent.domain.repository.TutorialRepository

class VideoTutorialUseCase(private val repository: TutorialRepository) {

    suspend operator fun invoke(): Resource<VideoTutorialData>{
        return try {
            val response = repository.videoTutorials()
            if (response.success && response.data != null){
                Resource.Success(response.data)
            }
            else{
                Resource.Error(
                    message = response.error,
                    resId = Res.string.server_connection_error
                )
            }
        }catch (e: IOException) {
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