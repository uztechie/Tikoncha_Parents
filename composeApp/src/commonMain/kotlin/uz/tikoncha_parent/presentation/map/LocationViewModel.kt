package uz.tikoncha_parent.presentation.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.icerock.moko.geo.LocationTracker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import uz.tikoncha_parent.domain.model.Resource
import uz.tikoncha_parent.domain.use_case.ChildrenLocationUseCase
import uz.tikoncha_parent.platform.isLocationServiceEnabled

class LocationViewModel(
    val tracker : LocationTracker,
    val childrenLocationUseCase: ChildrenLocationUseCase
): ViewModel() {




    private var childrenLocationJob: Job? = null

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

        getChildrenLocation()
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


    private fun getChildrenLocation(){
        _state.update {
            it.copy(
                childrenLocationList = emptyList(),
                childrenLocationError = "",
                childrenLocationLoading = true
            )
        }

        childrenLocationJob?.cancel()
        childrenLocationJob = viewModelScope.launch {
            val result = childrenLocationUseCase()
            when(result){
                is Resource.Loading ->{}
                is Resource.Error -> {
                    _state.update {
                        it.copy(
                            childrenLocationList = emptyList(),
                            childrenLocationError = result.message?:"",
                            childrenLocationLoading = false
                        )
                    }
                }
                is Resource.Success -> {
                    _state.update {
                        it.copy(
                            childrenLocationList = result.data,
                            childrenLocationError = "",
                            childrenLocationLoading = false
                        )
                    }
                }
            }
        }
    }
}