package uz.tikoncha_parent.presentation.task.model

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.flow.Flow

@Composable
fun <T> CollectEffects(
    flow: Flow<T>,
    onEffect: (T) -> Unit
) {
    LaunchedEffect(Unit) {
        flow.collect { effect ->
            onEffect(effect)
        }
    }
}