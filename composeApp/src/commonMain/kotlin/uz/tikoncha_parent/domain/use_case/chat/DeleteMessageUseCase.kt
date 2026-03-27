package uz.tikoncha_parent.domain.use_case.chat

import okio.IOException
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.iltimos_internetga_ulang
import tikoncha_parents.composeapp.generated.resources.kutilmagan_xatolik_qayta_urining
import uz.tikoncha_parent.domain.model.Resource
import uz.tikoncha_parent.domain.repository.ChatRepository

class DeleteMessageUseCase(
    private val repository: ChatRepository
) {
    suspend operator fun invoke(messageId: String): Resource<Boolean> {
        return try {
            val response = repository.deleteMessage(messageId)
            if (response.success) {
                Resource.Success(true)
            } else {
                Resource.Error(
                    resId = Res.string.iltimos_internetga_ulang,
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