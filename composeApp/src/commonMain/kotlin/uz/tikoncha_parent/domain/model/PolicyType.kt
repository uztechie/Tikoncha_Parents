package uz.tikoncha_parent.domain.model

import kotlinx.serialization.Serializable


@Serializable
enum class PolicyType (val order:Int){
    SCHOOL(3), PARENT_CHILD(2), STUDENT(1);

    companion object {
        fun from(raw: String): Int = when (raw.uppercase()) {
            "SCHOOL" -> PolicyType.SCHOOL.order
            "STUDENT" -> PolicyType.STUDENT.order
            "PARENT_CHILD" -> PolicyType.PARENT_CHILD.order
            else -> 0
        }

        fun getPolicyType(raw: String): PolicyType{
            return when (raw.uppercase()) {
                "SCHOOL" -> PolicyType.SCHOOL
                "STUDENT" -> PolicyType.STUDENT
                "PARENT_CHILD" -> PolicyType.PARENT_CHILD
                else -> PolicyType.SCHOOL
            }
        }
    }
}