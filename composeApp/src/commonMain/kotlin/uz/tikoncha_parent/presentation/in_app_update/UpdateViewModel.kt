package uz.tikoncha_parent.presentation.in_app_update

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import uz.tikoncha_parent.domain.model.in_app_update.InstallEvent
import uz.tikoncha_parent.domain.model.in_app_update.UpdateEffect
import uz.tikoncha_parent.domain.model.in_app_update.UpdateStatus
import uz.tikoncha_parent.domain.model.in_app_update.UpdateType
import uz.tikoncha_parent.domain.use_case.in_app_update.CheckUpdateUseCase
import uz.tikoncha_parent.domain.use_case.in_app_update.CompleteFlexibleUpdateUseCase
import uz.tikoncha_parent.domain.use_case.in_app_update.ObserveInstallEventsUseCase

class UpdateViewModel(
    private val checkUpdate: CheckUpdateUseCase,
    private val observeInstallEvents: ObserveInstallEventsUseCase,
    private val completeFlexibleUpdate: CompleteFlexibleUpdateUseCase
) : ViewModel() {

    // ── State ────────────────────────────────
    private val _state = MutableStateFlow(UpdateUiState())
    val state: StateFlow<UpdateUiState> = _state.asStateFlow()

    // ── One-shot effects ─────────────────────
    private val _effect = MutableSharedFlow<UpdateEffect>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val effect: SharedFlow<UpdateEffect> = _effect.asSharedFlow()

    private var started = false

    // ── Init: install eventlarini kuzatish ───
    init {
        viewModelScope.launch {
            observeInstallEvents().collect { event ->
                when (event) {
                    is InstallEvent.Downloading -> _state.update {
                        it.copy(
                            isDownloading = true,
                            bytesDownloaded = event.bytesDownloaded,
                            totalBytes = event.totalBytes
                        )
                    }

                    InstallEvent.Downloaded -> _state.update {
                        it.copy(
                            isDownloading = false,
                            bytesDownloaded = 0L,
                            totalBytes = 0L,
                            flexibleDownloaded = true
                        )
                    }

                    is InstallEvent.Failed -> _state.update {
                        it.copy(
                            isDownloading = false,
                            message = "Update failed: ${event.code}"
                        )
                    }
                }
            }
        }
    }

    // ── Event handler ────────────────────────
    fun onEvent(event: UpdateEvent) {
        when (event) {
            UpdateEvent.ScreenStarted -> {
                handleScreenStarted()
            }

            UpdateEvent.DismissUpdateDialog -> {
                _state.update {
                    it.copy(
                        showUpdateDialog = false
                    )
                }
            }

            is UpdateEvent.StartUpdateClicked -> {
                _state.update {
                    it.copy(
                        showUpdateDialog = false,
                        showCard = false
                    )
                }

                // Android'da → StartUpdateFlow (Activity'da handle qilinadi)
                // iOS'da IMMEDIATE bo'lsa → OpenAppStore
                if (event.type == UpdateType.IMMEDIATE &&
                    _state.value.allowedTypes == setOf(UpdateType.IMMEDIATE)
                ) {
                    // iOS holatida faqat IMMEDIATE bo'ladi
                    _effect.tryEmit(UpdateEffect.OpenAppStore)
                } else {
                    _effect.tryEmit(UpdateEffect.StartUpdateFlow(event.type))
                }
            }

            UpdateEvent.RestartClicked -> {
                viewModelScope.launch {
                    completeFlexibleUpdate()
                    _state.update { it.copy(flexibleDownloaded = false) }
                }
            }

            UpdateEvent.InstallLater -> {
                _state.update { it.copy(flexibleDownloaded = false) }
            }

            UpdateEvent.ClearMessage -> _state.update { it.copy(message = null) }
        }
    }

    // ── Birinchi marta ekran ochilganda ──────
    private fun handleScreenStarted() {
        if (started) return
        started = true
        performUpdateCheck()
    }

    // ── Resume'da (IMMEDIATE update davom ettirilishi kerak) ──
    private fun handleScreenResumed() {
        // Agar IMMEDIATE update boshlangan bo'lsa-yu, foydalanuvchi
        // orqaga qaytgan bo'lsa — qayta tekshirish kerak.
        // Ammo birinchi ochilishda emas — faqat keyingi resume'larda.
        if (!started) return

        viewModelScope.launch {
            val status = runCatching { checkUpdate() }
                .getOrElse { UpdateStatus.Failed(it.message) }

            // Agar hali ham IMMEDIATE update kerak bo'lsa, dialogni qayta ko'rsat
            if (status is UpdateStatus.UpdateAvailable &&
                status.allowedTypes.contains(UpdateType.IMMEDIATE)
            ) {
                _state.update {
                    it.copy(
                        showUpdateDialog = true,
                        allowedTypes = status.allowedTypes,
                        recommendedType = UpdateType.IMMEDIATE
                    )
                }
            }
        }
    }

    // ── Asosiy update tekshirish logikasi ────
    private fun performUpdateCheck() {
        viewModelScope.launch {
            _state.update { it.copy(isChecking = true, message = null) }

            val result = runCatching { checkUpdate() }
                .getOrElse { UpdateStatus.Failed(it.message) }

            when (result) {
                UpdateStatus.NoUpdate -> _state.update {
                    it.copy(
                        isChecking = false,
                        showUpdateDialog = false,
                        showCard = false,
                    )
                }

                is UpdateStatus.UpdateAvailable -> {
                    val recommended = when {
                        result.allowedTypes.contains(UpdateType.IMMEDIATE) -> UpdateType.IMMEDIATE
                        result.allowedTypes.contains(UpdateType.FLEXIBLE) -> UpdateType.FLEXIBLE
                        else -> null
                    }

                    _state.update {
                        it.copy(
                            isChecking = false,
                            showUpdateDialog = recommended != null,
                            showCard = recommended != null,
                            allowedTypes = result.allowedTypes,
                            recommendedType = recommended
                        )
                    }
                }

                UpdateStatus.NotSupported -> _state.update {
                    it.copy(
                        isChecking = false,
                        showUpdateDialog = false,
                        showCard = false,
                    )
                }

                is UpdateStatus.Failed -> _state.update {
                    it.copy(
                        isChecking = false,
                        message = result.message ?: "Update check failed"
                    )
                }

                UpdateStatus.Downloaded -> _state.update {
                    it.copy(
                        isChecking = false,
                        flexibleDownloaded = true
                    )
                }
            }
        }
    }
}