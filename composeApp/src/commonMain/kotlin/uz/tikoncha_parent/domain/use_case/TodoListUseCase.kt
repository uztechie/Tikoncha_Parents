package uz.tikoncha_parent.domain.use_case

import okio.IOException
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.iltimos_internetga_ulang
import tikoncha_parents.composeapp.generated.resources.kutilmagan_xatolik_qayta_urining
import tikoncha_parents.composeapp.generated.resources.server_connection_error
import uz.tikoncha_parent.data.remote.model.TodoDto
import uz.tikoncha_parent.domain.model.Resource
import uz.tikoncha_parent.domain.repository.TodoRepository


class TodoListUseCase(
    private val repository: TodoRepository,
) {
    suspend operator fun invoke(userId: String): Resource<List<TodoDto>> =
        try {
            val response = repository.getTodoList(userId)
            if (response.success && response.data != null) {
                Resource.Success(response.data.items)
            } else {
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