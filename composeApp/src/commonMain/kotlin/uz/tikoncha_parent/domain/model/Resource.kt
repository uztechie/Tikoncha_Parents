package uz.tikoncha_parent.domain.model

import org.jetbrains.compose.resources.StringResource

sealed class Resource<T> {
    class Loading<T>:Resource<T>()
    data class Success<T>(val data:T):Resource<T>()
    data class Error<T>(
        val resId: StringResource,
        val message: String? = null,
        val data:T? = null,
        val cause: Throwable? = null,
        val code: Int? = null,
    ):Resource<T>()
}