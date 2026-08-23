package uz.tikoncha_parent.presentation.base

import androidx.compose.runtime.Composable
import org.jetbrains.compose.resources.stringResource
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.iltimos_internetga_ulang
import tikoncha_parents.composeapp.generated.resources.kutilmagan_xatolik_qayta_urining
import tikoncha_parents.composeapp.generated.resources.server_connection_error
import tikoncha_parents.composeapp.generated.resources.token_eskirgan
import uz.tikoncha_parent.domain.model.app_error.ErrorCause
import uz.tikoncha_parent.domain.model.app_error.Outcome

@Composable
fun Outcome.Failure.asText(): String {
    serverMessage?.takeIf { it.isNotBlank() }?.let { return it }
    return cause.asFallbackText()
}

@Composable
fun ErrorCause.asFallbackText(): String = when (this) {
    ErrorCause.NoInternet,
    ErrorCause.Timeout -> stringResource(Res.string.iltimos_internetga_ulang)
    ErrorCause.SessionExpired -> stringResource(Res.string.token_eskirgan)
    ErrorCause.Forbidden,
    is ErrorCause.Server -> stringResource(Res.string.server_connection_error)
    is ErrorCause.AccountDeletionRequired -> stringResource(Res.string.server_connection_error)
    ErrorCause.InvalidResponse,
    ErrorCause.Unknown -> stringResource(Res.string.kutilmagan_xatolik_qayta_urining)
}