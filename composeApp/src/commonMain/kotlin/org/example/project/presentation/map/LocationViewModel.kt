package org.example.project.presentation.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.icerock.moko.geo.LocationTracker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.example.project.platform.isLocationServiceEnabled

class LocationViewModel(
    val tracker : LocationTracker
): ViewModel() {

    private val _state = MutableStateFlow(LocationState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            tracker.getLocationsFlow().collectLatest { data->
                _state.update {
                    it.copy(
                        locationData = data
                    )
                }
            }
        }
    }

    fun checkGPS() = viewModelScope.launch(Dispatchers.Default) {

        try {
            if (isLocationServiceEnabled()){
                tracker.startTracking()
                _state.update {
                    it.copy(
                        showGpsDialog = false
                    )
                }
            }
            else{
                _state.update {
                    it.copy(
                        showGpsDialog = true
                    )
                }
            }
        }catch (e: Exception){
            e.printStackTrace()
        }



    }
    fun stop()  = tracker.stopTracking()
    fun reset(){
        _state.update {
            it.copy(
                showGpsDialog = null
            )
        }
    }

    fun openGpsSettings(){
        _state.update {
            it.copy(
                showGpsDialog = false
            )
        }
        openGpsSettings()
    }
}