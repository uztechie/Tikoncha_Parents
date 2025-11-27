package uz.tikoncha_parent.presentation.ui_state

import androidx.compose.runtime.Composable
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

sealed class ResponseState<out T> {
    data object Idle : ResponseState<Nothing>()
    data object Loading : ResponseState<Nothing>()
    data class Success<T>(val data: T? = null) : ResponseState<T>()
    data class Error(
        val res: StringResource? = null,
        val message: String? = null
    ) : ResponseState<Nothing>()

}


@Composable
fun ResponseState<*>.errorText(): String = when (this) {
    is ResponseState.Error -> message?:res?.let { stringResource(it) } ?:""
    else -> ""
}