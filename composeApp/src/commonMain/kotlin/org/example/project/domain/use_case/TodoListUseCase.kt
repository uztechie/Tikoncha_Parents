package org.example.project.domain.use_case

import org.example.project.data.remote.model.TodoDto
import org.example.project.domain.model.Resource
import org.example.project.domain.repository.TodoRepository


class TodoListUseCase(
    private val repository: TodoRepository,
) {
    suspend operator fun invoke(userId: String): Resource<List<TodoDto>> =
        try {
            val response = repository.getTodoList(userId)
            if (response.success && response.data != null) {
                Resource.Success(response.data.items)
            } else {
                Resource.Error(response.error ?: "")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Xatolik")

        }
        catch (e: Exception){
            e.printStackTrace()
            Resource.Error("Xatolik")
        }
}