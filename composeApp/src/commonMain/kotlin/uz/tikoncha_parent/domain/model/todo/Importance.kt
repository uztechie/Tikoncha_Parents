package uz.tikoncha_parent.domain.model.todo

enum class Importance(val apiValue: String) {
    LOW("LOW"),
    MEDIUM("MEDIUM"),
    HIGH("HIGH"),
    CRITICAL("CRITICAL");

    companion object {
        fun fromApi(value: String?): Importance =
            entries.firstOrNull { it.apiValue.equals(value, ignoreCase = true) } ?: MEDIUM
    }
}