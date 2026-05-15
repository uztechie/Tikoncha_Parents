package uz.tikoncha_parent.presentation.add_child

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import uz.tikoncha_parent.data.local.AppSettings
import uz.tikoncha_parent.data.remote.model.AddChildRequest
import uz.tikoncha_parent.domain.model.Resource
import uz.tikoncha_parent.domain.use_case.AddChildUseCase

internal const val CHILD_APP_URL = "https://play.google.com/store/apps/details?id=uz.tikoncha.student&hl=en"

class AddChildScreenModel(
    private val addChildUseCase: AddChildUseCase
) : ScreenModel {

    private val _state = MutableStateFlow(AddChildState())
    val state: StateFlow<AddChildState> = _state.asStateFlow()

    private val _effects = Channel<AddChildEffect>(Channel.BUFFERED)
    val effects: Flow<AddChildEffect> = _effects.receiveAsFlow()

    init {
        _state.update {
            it.copy(
                showBindChildTutorial = AppSettings.showBindChildTutorial
            )
        }
    }

    fun onEvent(intent: AddChildEvent) {
        when (intent) {
            is AddChildEvent.PhoneChanged -> onPhoneChanged(intent.value)
            AddChildEvent.RequestCode     -> requestCode()
            AddChildEvent.RefreshCode     -> requestCode()
            AddChildEvent.CodeCopied      -> _state.update { it.copy(showCopiedSnackbar = true) }
            AddChildEvent.DismissSnackbar -> _state.update { it.copy(showCopiedSnackbar = false) }
            AddChildEvent.DismissError    -> _state.update { it.copy(errorRes = null, errorMessage = null) }
            AddChildEvent.OpenAppLink     -> _effects.trySend(AddChildEffect.OpenUrl(CHILD_APP_URL))
            AddChildEvent.ShareLink       -> _effects.trySend(AddChildEffect.ShareText(CHILD_APP_URL))
            AddChildEvent.PlayTutorial    -> _effects.trySend(AddChildEffect.PlayTutorialVideo)
            AddChildEvent.NavigateBack    -> _effects.trySend(AddChildEffect.NavigateBack)
        }
    }

    private fun onPhoneChanged(value: String) {
        val digits = value.filter { it.isDigit() }.take(9)
        _state.update { current ->
            current.copy(
                phoneNumber = digits,
                // Telefon o'zgartirilsa va requested phone'dan farq qilsa — kodni tozalaymiz
                code = if (digits == current.requestedPhone) current.code else null,
                errorRes = null,
                errorMessage = null
            )
        }
    }

    private fun requestCode() {
        val phone = _state.value.phoneNumber
        if (phone.length != 9 || _state.value.isLoading) return

        screenModelScope.launch {
            _state.update { it.copy(isLoading = true, errorRes = null, errorMessage = null) }

            val result = addChildUseCase(AddChildRequest(phone_number = "+998$phone"))

            when (result) {
                is Resource.Success -> _state.update {
                    it.copy(
                        isLoading = false,
                        code = result.data,
                        requestedPhone = phone,
                    )
                }
                is Resource.Error -> _state.update {
                    it.copy(
                        isLoading = false,
                        errorRes = result.resId,
                        errorMessage = result.message
                    )
                }

                is Resource.Loading -> {}
            }
        }
    }

}