package uz.tikoncha_parent.domain.use_case

import okio.IOException
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.no_internet_connection
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
        }
        catch (e: IOException){
            Resource.Error(
                resId = Res.string.no_internet_connection,
                cause = e
            )
        }
        catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(
                resId = Res.string.server_connection_error,
                cause = e
            )
        }
}