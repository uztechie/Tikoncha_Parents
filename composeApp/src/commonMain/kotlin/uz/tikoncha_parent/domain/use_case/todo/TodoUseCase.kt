package uz.tikoncha_parent.domain.use_case.todo

import kotlinx.io.IOException
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.iltimos_internetga_ulang
import tikoncha_parents.composeapp.generated.resources.kutilmagan_xatolik_qayta_urining
import tikoncha_parents.composeapp.generated.resources.server_connection_error
import uz.tikoncha_parent.data.mapper.todo.toCreateParams
import uz.tikoncha_parent.data.mapper.todo.toDto
import uz.tikoncha_parent.data.remote.model.todo.TodoDto
import uz.tikoncha_parent.data.remote.model.todo.TodoRequest
import uz.tikoncha_parent.domain.model.Resource

class TodoUseCase(
    private val createTodoUseCase: CreateTodoUseCase
) {
    suspend operator fun invoke(request: TodoRequest): Resource<TodoDto> = try {
        createTodoUseCase(request.toCreateParams()).fold(
            onSuccess = { todo -> Resource.Success(todo.toDto()) },
            onFailure = { e ->
                Resource.Error(message = e.message, resId = Res.string.server_connection_error)
            }
        )
    } catch (e: IOException) {
        Resource.Error(resId = Res.string.iltimos_internetga_ulang, cause = e)
    } catch (e: Exception) {
        e.printStackTrace()
        Resource.Error(resId = Res.string.kutilmagan_xatolik_qayta_urining, cause = e)
    }
}