// iosMain
import androidx.compose.runtime.*

@Composable
actual fun rememberPlatformKeyboardState(): State<Boolean> {
    // TODO: UIKit orqali real implementatsiya yozish
    // Hozircha false qaytaramiz
    return remember { mutableStateOf(false) }
}
