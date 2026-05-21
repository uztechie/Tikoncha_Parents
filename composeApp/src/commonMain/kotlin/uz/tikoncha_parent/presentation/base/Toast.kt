package uz.tikoncha_parent.presentation.base

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import uz.tikoncha_parent.ui.theme.AppColors
import uz.tikoncha_parent.ui.theme.AppTypography

enum class ToastType { Success, Error, Warning, Info }

data class ToastData(
    val type: ToastType,
    val title: String,
    val body: String? = null,
)

class ToastHostState {
    private val _current = MutableStateFlow<ToastData?>(null)
    val current: StateFlow<ToastData?> = _current.asStateFlow()

    suspend fun show(toast: ToastData, durationMs: Long = 3000) {
        _current.value = toast
        delay(durationMs)
        _current.value = null
    }

    fun dismiss() {
        _current.value = null
    }
}

/**
 * Global toast state — istalgan composable dan foydalanish:
 *
 *   val toast = LocalToastHost.current
 *   scope.launch { toast.show(ToastData(ToastType.Success, "Saqlandi!")) }
 */
val LocalToastHost = staticCompositionLocalOf<ToastHostState> {
    error("LocalToastHost not provided. ToastProvider ni App darajasida o'rnating.")
}

/**
 * App darajasida BIR MARTA o'rnating:
 *
 *   ToastProvider {
 *       // barcha kontent
 *       NavHost(...) yoki Navigator(...)
 *   }
 */
@Composable
fun ToastProvider(content: @Composable () -> Unit) {
    val toastState = remember { ToastHostState() }

    CompositionLocalProvider(LocalToastHost provides toastState) {
        Box {
            content()
            ToastHost(state = toastState)
        }
    }
}

@Composable
private fun ToastHost(state: ToastHostState, modifier: Modifier = Modifier) {
    val current by state.current.collectAsState()

    AnimatedVisibility(
        visible = current != null,
        enter = slideInVertically { -it } + fadeIn(),
        exit = slideOutVertically { -it } + fadeOut(),
        modifier = modifier,
    ) {
        current?.let { ToastView(it, onDismiss = state::dismiss) }
    }
}

@Composable
private fun ToastView(data: ToastData, onDismiss: () -> Unit) {
    val icon: ImageVector
    val bg: Color

    when (data.type) {
        ToastType.Success -> {
            icon = Icons.Default.Check
            bg = AppColors.text.accentSuccess
        }
        ToastType.Error -> {
            icon = Icons.Default.ErrorOutline
            bg = AppColors.bg.accentDanger
        }
        ToastType.Warning -> {
            icon = Icons.Default.Warning
            bg = AppColors.bg.accentWarning
        }
        ToastType.Info -> {
            icon = Icons.Default.Info
            bg = AppColors.bg.secondaryBrand
        }
    }

    Row(
        modifier = Modifier
            .statusBarsPadding()
            .padding(horizontal = 16.dp, vertical = 16.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(bg)
            .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(28.dp)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.22f)),
            contentAlignment = Alignment.Center,
        ) {
            Icon(icon, null, tint = Color.White, modifier = Modifier.size(14.dp))
        }
        Spacer(Modifier.width(10.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = data.title,
                style = AppTypography.titleSmMedium,
                color = Color.White
            )
            data.body?.let {
                Text(
                    text = it,
                    style = AppTypography.bodyMdMedium,
                    color = Color.White.copy(alpha = 0.9f)
                )
            }
        }
        Box(
            modifier = Modifier
                .size(22.dp)
                .clip(CircleShape)
                .clickable(onClick = onDismiss),
            contentAlignment = Alignment.Center,
        ) {
            Icon(Icons.Default.Close, null, tint = Color.White, modifier = Modifier.size(11.dp))
        }
    }
}