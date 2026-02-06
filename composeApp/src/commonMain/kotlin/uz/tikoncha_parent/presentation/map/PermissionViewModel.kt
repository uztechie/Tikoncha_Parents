package uz.tikoncha_parent.presentation.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.icerock.moko.permissions.DeniedAlwaysException
import dev.icerock.moko.permissions.DeniedException
import dev.icerock.moko.permissions.Permission
import dev.icerock.moko.permissions.PermissionState
import dev.icerock.moko.permissions.PermissionsController
import dev.icerock.moko.permissions.location.LOCATION
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PermissionViewModel(
    private val controller: PermissionsController
) : ViewModel() {
    private val _state = MutableStateFlow<PermissionState>(PermissionState.NotDetermined)
    val state: StateFlow<PermissionState> = _state.asStateFlow()

    init{
        viewModelScope.launch {
            _state.value = controller.getPermissionState(Permission.LOCATION)
        }
    }

    fun requestPermission() = viewModelScope.launch {
        _state.value = try {
            controller.providePermission(Permission.LOCATION)
            PermissionState.Granted
        } catch (e: DeniedAlwaysException) {
            PermissionState.DeniedAlways
        } catch (e: DeniedException) {
            PermissionState.Denied
        }
    }



    fun refresh() = viewModelScope.launch {

        _state.value = controller.getPermissionState(Permission.LOCATION)
    }
}