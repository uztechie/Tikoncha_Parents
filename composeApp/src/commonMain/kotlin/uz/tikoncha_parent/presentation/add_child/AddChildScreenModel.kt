package uz.tikoncha_parent.presentation.add_child

import androidx.compose.ui.util.fastCoerceAtLeast
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import uz.tikoncha_parent.data.local.AppSettings
import uz.tikoncha_parent.domain.model.app_error.Outcome
import uz.tikoncha_parent.domain.repository.ChildRepository
import uz.tikoncha_parent.presentation.add_child.AddChildEffect.OpenUrl
import uz.tikoncha_parent.presentation.add_child.AddChildEffect.ShareText
import kotlin.time.Clock

internal const val CHILD_APP_URL =
    "https://play.google.com/store/apps/details?id=uz.tikoncha.student&hl=en"

class AddChildScreenModel(
    private val childRepository: ChildRepository
) : ScreenModel {

    private var expiryJob: Job? = null

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

    fun onEvent(event: AddChildEvent) {
        when (event) {
            is AddChildEvent.PhoneChanged -> onPhoneChanged(event.value)
            AddChildEvent.RequestCode -> requestCode()
            AddChildEvent.RefreshCode -> requestCode()
            AddChildEvent.CodeCopied -> _state.update { it.copy(showCopiedSnackbar = true) }
            AddChildEvent.DismissSnackbar -> _state.update { it.copy(showCopiedSnackbar = false) }
            AddChildEvent.DismissError -> _state.update { it.copy(error = null) }
            AddChildEvent.OpenAppLink -> _effects.trySend(OpenUrl(CHILD_APP_URL))
            AddChildEvent.ShareLink -> _effects.trySend(ShareText(CHILD_APP_URL))
            AddChildEvent.PlayTutorial -> _effects.trySend(AddChildEffect.PlayTutorialVideo)
            AddChildEvent.NavigateBack -> _effects.trySend(AddChildEffect.NavigateBack)
            is AddChildEvent.OnCountryChange -> {
                _state.update {
                    it.copy(
                        selectedCountry = event.country
                    )
                }
            }
        }
    }

    private fun onPhoneChanged(value: String) {
        _state.update { current ->
            current.copy(
                phoneNumber = value,
                // Telefon o'zgartirilsa va requested phone'dan farq qilsa — kodni tozalaymiz
                code = if (value == current.requestedPhone) current.code else null,
                error = null
            )
        }
    }

    private fun requestCode() {
        val phone = _state.value.phoneNumber
        val fullPhoneNumber = _state.value.fullPhoneNumber
        if (phone.length != _state.value.selectedCountry.maxDigits || _state.value.isLoading) return

        screenModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            when (val res = childRepository.addChild(fullPhoneNumber)) {
                is Outcome.Success -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            code = res.data.code,
                            codeExpiresAt = res.data.expiresAtMillis,
                            requestedPhone = phone,
                        )
                    }
                    startExpiryCountdown(res.data.expiresAtMillis)
                }

                is Outcome.Failure -> {
                    _state.update {
                        it.copy(
                            error = res,
                            isLoading = false,
                        )
                    }
                }
            }
        }
    }

    private fun startExpiryCountdown(expiresAt: Long) {
        expiryJob?.cancel()
        if (expiresAt <= 0L) {
            _state.update {
                it.copy(
                    remainingSeconds = 0
                )
            }
            return
        }
        expiryJob = screenModelScope.launch {
            while (true) {
                val left = ((expiresAt - Clock.System.now().toEpochMilliseconds()) / 1000).toInt()
                _state.update {
                    it.copy(
                        remainingSeconds = left.fastCoerceAtLeast(0)
                    )
                }
                if (left <= 0) break
                delay(1000)
            }
        }
    }
}