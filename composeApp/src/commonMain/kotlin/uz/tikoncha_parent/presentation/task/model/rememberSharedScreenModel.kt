package uz.tikoncha_parent.presentation.task.model

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow

@Composable
inline fun <reified T : ScreenModel> rememberSharedScreenModel(): T {
    val navigator = LocalNavigator.currentOrThrow
    val rootScreen = navigator.items.first()
    return rootScreen.koinScreenModel<T>()
}