package uz.tikoncha_parent.presentation.child_confirm_cod

import androidx.lifecycle.ViewModel
import cafe.adriel.voyager.core.model.ScreenModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ChildConfirmViewModel(): ScreenModel {

    private val _state = MutableStateFlow(ChildConfirmState())
    val state = _state.asStateFlow()

    fun onEvent(event: ChildConfirmEvent){
        when(event){
            is ChildConfirmEvent.SetConfirmCode -> {
                _state.update {
                    it.copy(
                        codeNumber = event.code
                    )
                }
            }
        }
    }
}