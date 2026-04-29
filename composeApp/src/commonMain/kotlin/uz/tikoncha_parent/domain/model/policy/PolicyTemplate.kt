package uz.tikoncha_parent.domain.model.policy

enum class PolicyTemplate {
    SLEEP,        // Uyqu vaqti rejimi
    APP_TIMER,    // Ilova taymerlari
    CONTENT;      // Kontent cheklovlari

    companion object {
        fun fromString(value: String?): PolicyTemplate? =
            entries.firstOrNull { it.name == value }
    }
}