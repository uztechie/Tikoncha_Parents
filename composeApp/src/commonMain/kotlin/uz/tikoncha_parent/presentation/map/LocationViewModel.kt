package uz.tikoncha_parent.presentation.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.icerock.moko.geo.LocationTracker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import uz.tikoncha_parent.domain.model.Resource
import uz.tikoncha_parent.domain.use_case.ChildrenLocationUseCase
import uz.tikoncha_parent.platform.Logger
import uz.tikoncha_parent.platform.isLocationServiceEnabled

class LocationViewModel(
    val tracker: LocationTracker,
    private val childrenLocationUseCase: ChildrenLocationUseCase? = null
) : ViewModel() {

    private val _state = MutableStateFlow(LocationState())
    val state = _state.asStateFlow()

    init {
        observeLocation()
        loadChildrenLocation()
    }

    private fun observeLocation() {
        viewModelScope.launch {
            tracker.getLocationsFlow()
                .distinctUntilChanged()
                .collectLatest { data ->
                    Logger.d("LocationViewModel", "location = $data")
                    _state.update { it.copy(locationData = data) }
                }
        }
    }

    /**
     * 🚀 BU — LOCATION TRACKINGNI BEVOSITA BOSHLAYDIGAN FUNKSIYA.
     */
    fun start() = viewModelScope.launch {
        try {
            Logger.d("LocationViewModel", "start() called")
            tracker.startTracking()
        } catch (e: Exception) {
            Logger.d("LocationViewModel", "start error: ${e.message}")
        }
    }

    /**
     * 📍 Permission bo‘lgach chaqiriladi.
     * GPS yoqilgan bo‘lsa -> startTracking()
     * O‘chiq bo‘lsa -> dialog uchun flag
     */
    fun checkGPS() = viewModelScope.launch(Dispatchers.Default) {
        val gpsEnabled = isLocationServiceEnabled()
        Logger.d("LocationViewModel", "GPS enabled = $gpsEnabled")

        if (gpsEnabled) {
            start()  // 💥 ENDI BU YERDA start() chaqirilyapti
            _state.update { it.copy(showGpsDialog = false) }
        } else {
            _state.update { it.copy(showGpsDialog = true) }
        }
    }

    fun stop() = tracker.stopTracking()

    private fun loadChildrenLocation() {
        val useCase = childrenLocationUseCase ?: return

        viewModelScope.launch {
            _state.update {
                it.copy(childrenLocationList = emptyList(), childrenLocationLoading = true)
            }

            when (val result = useCase()) {
                is Resource.Success -> {
                    _state.update {
                        it.copy(
                            childrenLocationList = result.data ?: emptyList(),
                            childrenLocationLoading = false
                        )
                    }
                }

                is Resource.Error -> {
                    _state.update {
                        it.copy(
                            childrenLocationList = emptyList(),
                            childrenLocationError = result.message ?: "",
                            childrenLocationLoading = false
                        )
                    }
                }

                else -> Unit
            }
        }
    }

    override fun onCleared() {
        tracker.stopTracking()
        super.onCleared()
    }
}

