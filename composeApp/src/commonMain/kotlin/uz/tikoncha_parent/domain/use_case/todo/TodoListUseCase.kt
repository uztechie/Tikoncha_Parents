package uz.tikoncha_parent.domain.use_case.todo

import kotlinx.io.IOException
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.iltimos_internetga_ulang
import tikoncha_parents.composeapp.generated.resources.kutilmagan_xatolik_qayta_urining
import tikoncha_parents.composeapp.generated.resources.server_connection_error
import uz.tikoncha_parent.data.mapper.todo.toDto
import uz.tikoncha_parent.data.remote.model.todo.TodoDto
import uz.tikoncha_parent.domain.model.Resource
import uz.tikoncha_parent.domain.model.todo.TodoFilter
import uz.tikoncha_parent.domain.model.todo.TodosQuery

class TodoListUseCase(
    private val getTodosUseCase: GetTodosUseCase
) {
    suspend operator fun invoke(userId: String): Resource<List<TodoDto>> = try {
        val query = TodosQuery(
            targetUserId = userId,
            filter = TodoFilter(),
            limit = 500,
            offset = 0
        )
        getTodosUseCase(query).fold(
            onSuccess = { page -> Resource.Success(page.items.map { it.toDto() }) },
            onFailure = { e ->
                Resource.Error(
                    message = e.message,
                    resId = Res.string.server_connection_error
                )
            }
        )
    } catch (e: IOException) {
        Resource.Error(resId = Res.string.iltimos_internetga_ulang, cause = e)
    } catch (e: Exception) {
        e.printStackTrace()
        Resource.Error(resId = Res.string.kutilmagan_xatolik_qayta_urining, cause = e)
    }
}