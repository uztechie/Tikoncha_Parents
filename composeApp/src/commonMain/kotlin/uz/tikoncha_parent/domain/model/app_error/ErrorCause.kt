package uz.tikoncha_parent.domain.model.app_error

/**
 * Xatoning SABABI. Matn emas.
 *
 * Matn faqat UI chekkasida, @Composable kontekstda hosil qilinadi —
 * shunda til almashtirilganda rekompozitsiya orqali o'zi yangilanadi.
 */
sealed interface ErrorCause {

    /** So'rov serverga yetib bormadi. */
    data object NoInternet : ErrorCause

    /** So'rov ketdi, lekin javob vaqtida kelmadi. */
    data object Timeout : ErrorCause

    /** 401 — token o'lgan yoki bekor qilingan. Qayta login kerak. */
    data object SessionExpired : ErrorCause

    /** 403 — token to'g'ri, lekin ruxsat yo'q. */
    data object Forbidden : ErrorCause

    /** Server xato qaytardi. [code] null bo'lishi mumkin. */
    data class Server(val code: Int? = null) : ErrorCause

    /** Server hisobni o'chirishni talab qildi; [url] — o'chirish sahifasi. */
    data class AccountDeletionRequired(val url: String) : ErrorCause

    /** Javob keldi, lekin parse qilib bo'lmadi yoki payload kutilganidek emas. */
    data object InvalidResponse : ErrorCause

    /** Vazifa sarlavhasi bo'sh. */
    data object EmptyTitle : ErrorCause

    /** Tanga miqdori manfiy. */
    data object NegativeCoin : ErrorCause

    /** Muhimlilik darajasi tanlanmagan. */
    data object ImportanceNotSelected : ErrorCause

    /** Tugatish sanasi tanlanmagan. */
    data object DueDateNotSelected : ErrorCause

    /** Tugatish vaqti tanlanmagan. */
    data object DueTimeNotSelected : ErrorCause

    /** Ota-onaning tanga balansi vazifa mukofotidan kam. */
    data object InsufficientCoins : ErrorCause

    /** Vazifa kimga berilishi tanlanmagan. */
    data object ChildNotSelected : ErrorCause

    /** Tahrirlashda vazifa id'si yo'qolgan — bo'lmasligi kerak bo'lgan holat. */
    data object TaskIdMissing : ErrorCause

    data object Unknown : ErrorCause

    /**
     * [SessionExpired] qayta urinilmaydi — token yo'q, har urinish yana 401 oladi.
     * [InvalidResponse] ham — server bir xil buzuq javob qaytaradi.
     */
    val isRetryable: Boolean
        get() = this == NoInternet || this == Timeout || this is Server
}