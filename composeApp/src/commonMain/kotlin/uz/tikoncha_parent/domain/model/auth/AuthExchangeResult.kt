package uz.tikoncha_parent.domain.model.auth

import uz.saidburxon.newedu.data.model.VerifyOtpResponseData

sealed interface AuthExchangeResult {
    data class Success(val data: VerifyOtpResponseData) : AuthExchangeResult
    data class Error(val message: String) : AuthExchangeResult
}