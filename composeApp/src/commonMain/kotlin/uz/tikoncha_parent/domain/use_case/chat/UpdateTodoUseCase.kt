package uz.tikoncha_parent.domain.use_case.chat

import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.server_connection_error
import uz.tikoncha_parent.data.remote.model.TodoDto
import uz.tikoncha_parent.data.remote.model.TodoRequest
import uz.tikoncha_parent.domain.model.Resource
import uz.tikoncha_parent.domain.repository.TodoRepository

class UpdateTodoUseCase(
    private val repository: TodoRepository,
) {
    suspend operator fun invoke(request: TodoRequest): Resource<TodoDto> {
        return try {
            val response = repository.updateTodo(request)

            if (response.success && response.data != null) {
                Resource.Success(response.data)
            } else {
                Resource.Error(
                    message = response.error,
                    resId = Res.string.server_connection_error
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(
                resId = Res.string.server_connection_error,
                cause = e
            )
        }
    }
}
