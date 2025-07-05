package uz.saidburxon.newedu.data.local.entity

import uz.saidburxon.newedu.sqldelight.AssignmentEntity as AssignmentEntityDb

fun TaskEntityDb.toTAskEntity(): TaskEntity {
    return TaskEntity(
        id = id,
        title = title,
        dueDate = dueDate,
        completed = completed
    )
}