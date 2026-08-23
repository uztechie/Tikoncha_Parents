package uz.tikoncha_parent.domain.model.app_error

/**
 * Repository va use case natijasi.
 *
 * `Loading` holati ATAYLAB yo'q — u UI holati, natija emas.
 * Yuklanish belgisi ViewModel'ning state klassida turadi.
 */
sealed interface Outcome<out T> {

    data class Success<out T>(val data: T) : Outcome<T>

    /**
     * @param cause xatoning turi — kod shu asosda qaror qabul qiladi
     * @param serverMessage server yuborgan matn (backend LANG-CODE ni hurmat qilsa afzal)
     */
    data class Failure(
        val cause: ErrorCause,
        val serverMessage: String? = null,
    ) : Outcome<Nothing>
}

inline fun <T, R> Outcome<T>.map(transform: (T) -> R): Outcome<R> = when (this) {
    is Outcome.Success -> Outcome.Success(transform(data))
    is Outcome.Failure -> this
}

fun <T> Outcome<T>.getOrNull(): T? = (this as? Outcome.Success)?.data

fun <T> Outcome<T>.failureOrNull(): Outcome.Failure? = this as? Outcome.Failure

val Outcome<*>.isSuccess: Boolean get() = this is Outcome.Success

inline fun <T> Outcome<T>.onSuccess(block: (T) -> Unit): Outcome<T> =
    also { if (it is Outcome.Success) block(it.data) }

inline fun <T> Outcome<T>.onFailure(block: (Outcome.Failure) -> Unit): Outcome<T> =
    also { if (it is Outcome.Failure) block(it) }