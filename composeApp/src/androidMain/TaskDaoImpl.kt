package uz.saidburxon.newedu.data.local

import kotlinx.coroutines.flow.Flow
import uz.saidburxon.newedu.data.local.entity.AssignmentEntity

class TaskDaoImpl(
    private val database: AppDatabase
) : TaskDao {
    override suspend fun insertTask(task: TaskEntity) {
        database.taskDao().insert(task)
    }

    override fun getUpcomingTasks(): Flow<List<TaskEntity>> {
        return database.taskDao().getUpcoming()
    }

    override fun getCompletedTasks(): Flow<List<TaskEntity>> {
        return database.taskDao().getCompleted()
    }
}