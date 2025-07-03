import androidx.compose.runtime.Composable
import androidx.compose.runtime.State

@Composable
expect fun rememberPlatformKeyboardState(): State<Boolean>

@Composable
fun KeyboardAsState(): State<Boolean> {
    return rememberPlatformKeyboardState()
}
