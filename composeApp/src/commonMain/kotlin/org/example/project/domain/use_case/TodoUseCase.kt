package org.example.project.domain.use_case

import org.example.project.data.remote.model.TodoDto
import org.example.project.data.remote.model.TodoRequest
import org.example.project.domain.model.Resource
import org.example.project.domain.repository.TodoRepository


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