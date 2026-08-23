package uz.tikoncha_parent.data.remote.app_error

import uz.tikoncha_parent.domain.model.app_error.ErrorCause

actual fun platformErrorCause(t: Throwable): ErrorCause? = when (t) {
    is java.net.UnknownHostException,
    is java.net.ConnectException -> ErrorCause.NoInternet
    is java.net.SocketTimeoutException -> ErrorCause.Timeout
    else -> null
}