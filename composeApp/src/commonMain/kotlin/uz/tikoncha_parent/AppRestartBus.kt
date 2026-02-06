package uz.tikoncha_parent

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

object AppRestartBus {
    private val _key = MutableStateFlow(0)
    val key = _key.asStateFlow()
    fun restart() { _key.value += 1 }
}