package org.example.project.presentation.location

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.icerock.moko.permissions.DeniedAlwaysException
import dev.icerock.moko.permissions.DeniedException
import dev.icerock.moko.permissions.Permission
import dev.icerock.moko.permissions.PermissionState
import dev.icerock.moko.permissions.PermissionsController
import dev.icerock.moko.permissions.RequestCanceledException
import dev.icerock.moko.permissions.location.LOCATION
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PermissionViewModel(
    private val controller: PermissionsController
) : ViewModel() {
    private val _state = MutableStateFlow<PermissionState>(PermissionState.NotDetermined)
    val state: StateFlow<PermissionState> = _state.asStateFlow()


//    var state by mutableStateOf(value = PermissionState.NotDetermined)
//        private set
//
//    var isGranted by mutableStateOf(false)
//        private  set

    init{
        viewModelScope.launch {
            _state.value = controller.getPermissionState(Permission.LOCATION)
        }
    }

    fun requestLocation() = viewModelScope.launch {
        _state.value = try {
            controller.providePermission(Permission.LOCATION)
            PermissionState.Granted
        } catch (e: DeniedAlwaysException) {
            PermissionState.DeniedAlways
        } catch (e: DeniedException) {
            PermissionState.Denied
        }
    }

//    fun checkPermission(){
//        viewModelScope.launch {
//            isGranted = controller.getPermissionState(Permission.LOCATION) == PermissionState.Granted
//        }
//    }

    fun refresh() = viewModelScope.launch {
        _state.value = controller.getPermissionState(Permission.LOCATION)
    }
}