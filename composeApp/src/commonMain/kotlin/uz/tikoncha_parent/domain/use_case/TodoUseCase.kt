package uz.tikoncha_parent.domain.use_case

import uz.tikoncha_parent.data.remote.model.TodoDto
import uz.tikoncha_parent.data.remote.model.TodoRequest
import uz.tikoncha_parent.domain.model.Resource
import uz.tikoncha_parent.domain.repository.TodoRepository


class TodoUseCase(
    private val repository: TodoRepository,
) {
    suspend operator fun invoke(request: TodoRequest): Resource<TodoDto> {
        return try {
            val response = repository.registerTodo(request)
            if (response.success && response.data != null){
                Resource.Success(response.data)
            }
            else{
                Resource.Error(response.error?: "")
            }


        }
        catch (e: Exception){
            Resource.Error("Xatolik")

        }
        catch (e: Exception){
            e.printStackTrace()
            Resource.Error("Xatolik")
        }

    }
}