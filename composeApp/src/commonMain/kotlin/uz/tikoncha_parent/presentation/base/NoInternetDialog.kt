package uz.tikoncha_parent.presentation.base

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jetbrains.compose.resources.stringResource
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.diqqat
import tikoncha_parents.composeapp.generated.resources.iltimos_internetga_ulang
import tikoncha_parents.composeapp.generated.resources.ok
import uz.tikoncha_parent.platform.isInternetAvailable

/** Internet check helper holati */
class InternetCheckState(
    val showDialog: MutableState<Boolean>,
    private val scope: CoroutineScope,
) {
    /**
     * Internet tekshiradi. Yo'q bo'lsa dialog ko'rsatadi.
     * Bor bo'lsa [onAvailable] ni chaqiradi.
     */
    fun check(onAvailable: suspend () -> Unit): Job = scope.launch {
        val isOnline = withContext(Dispatchers.Default) { isInternetAvailable() }
        if (isOnline) {
            onAvailable()
        } else {
            showDialog.value = true
        }
    }
}

@Composable
fun rememberInternetCheck(scope: CoroutineScope): InternetCheckState {
    val showDialog = remember { mutableStateOf(false) }
    return remember(scope) { InternetCheckState(showDialog, scope) }
}

@Composable
fun NoInternetDialog(state: InternetCheckState) {
    CustomDialog(
        show = state.showDialog.value,
        title = stringResource(Res.string.diqqat),
        message = stringResource(Res.string.iltimos_internetga_ulang),
        buttonText = stringResource(Res.string.ok),
        onDismiss = { state.showDialog.value = false },
        onButtonClick = { state.showDialog.value = false }
    )
}