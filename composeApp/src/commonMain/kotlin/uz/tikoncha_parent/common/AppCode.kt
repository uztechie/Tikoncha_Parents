package uz.tikoncha_parent.common

enum class AppCode(private val code:Int) {
    STUDENT(1),
    PARENT(2),
    TEACHER(3);

    companion object{
        val currentAppCode = PARENT.code
    }
}