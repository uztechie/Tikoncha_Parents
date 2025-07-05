package uz.saidburxon.newedu.data.local

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import uz.saidburxon.newedu.data.local.entity.AssignmentEntity
import uz.saidburxon.newedu.sqldelight.database.NewEduDatabase

class TaskDaoImpl(
    private val database: NewEduDatabase
) : TaskDao {
    override suspend fun insertTask(task: TaskEntity) {
        database.taskEntityQueries.insertTask(
            id = task.id,
            title = task.title,
            dueDate = task.dueDate,
            completed = task.completed
        )
    }

    override fun getUpcomingTasks(): Flow<List<TaskEntity>> = flow {
        val result = database.taskEntityQueries.selectUpcoming().executeAsList()
        emit(result.map { it.toTaskEntity() })
    }

    override fun getCompletedTasks(): Flow<List<TaskEntity>> = flow {
        val result = database.taskEntityQueries.selectCompleted().executeAsList()
        emit(result.map { it.toTaskEntity() })
    }
}