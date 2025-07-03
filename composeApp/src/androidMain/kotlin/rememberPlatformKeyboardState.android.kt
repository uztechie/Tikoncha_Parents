import androidx.compose.runtime.Composable
import androidx.compose.runtime.State

@Composable
actual fun rememberPlatformKeyboardState(): State<Boolean> {
    return androidx.compose.runtime.remember {
        androidx.compose.runtime.mutableStateOf(false)
    }

}