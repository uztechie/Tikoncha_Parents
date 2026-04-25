package uz.tikoncha_parent.presentation.navigation

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.Navigator
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

/**
 * iOS-style swipe back gesture wrapper for Voyager Navigator.
 *
 * Usage in App.kt:
 *   Navigator(SplashScreen()) { nav ->
 *       SwipeBackContent(navigator = nav)
 *   }
 *
 * Faqat iOS da ishlaydi (Android da oddiy CurrentScreen ko'rsatadi).
 */

// ── expect/actual — faqat iOS da swipe ishlaydi ──
expect val isSwipeBackEnabled: Boolean

@Composable
fun SwipeBackContent(navigator: Navigator) {
    if (!isSwipeBackEnabled || !navigator.canPop) {
        // Android yoki root screen — oddiy ko'rsatish
        navigator.lastItem.Content()
        return
    }

    val scope = rememberCoroutineScope()
    val offsetX = remember { Animatable(0f) }
    var screenWidth by remember { mutableStateOf(1f) }
    val density = LocalDensity.current
    val edgeThresholdPx = with(density) { 20.dp.toPx() }

    // Swipe boshlangan x pozitsiyasi
    var startX by remember { mutableStateOf(0f) }
    var isSwiping by remember { mutableStateOf(false) }

    // Oldingi screen
    val previousScreen = remember(navigator.items) {
        if (navigator.items.size >= 2) navigator.items[navigator.items.size - 2] else null
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .onSizeChanged { screenWidth = it.width.toFloat() }
    ) {
        // ── Oldingi screen (orqa fonda) ──
        if (isSwiping && previousScreen != null) {
            val prevOffset = (-screenWidth * 0.3f * (1f - offsetX.value / screenWidth)).roundToInt()
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .offset { IntOffset(prevOffset, 0) }
            ) {
                previousScreen.Content()
            }

            // Qorong'u overlay
            val shadowAlpha = 0.1f * (1f - offsetX.value / screenWidth)
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .offset { IntOffset(prevOffset, 0) }
                    .drawWithContent {
                        drawContent()
                        drawRect(Color.Black.copy(alpha = shadowAlpha))
                    }
            )
        }

        // ── Hozirgi screen ──
        Box(
            modifier = Modifier
                .fillMaxSize()
                .offset { IntOffset(offsetX.value.roundToInt(), 0) }
                .pointerInput(navigator.lastItem.key) {
                    detectHorizontalDragGestures(
                        onDragStart = { offset ->
                            startX = offset.x
                            isSwiping = startX < edgeThresholdPx
                        },
                        onDragEnd = {
                            if (!isSwiping) return@detectHorizontalDragGestures
                            val threshold = screenWidth * 0.35f
                            scope.launch {
                                if (offsetX.value > threshold) {
                                    // Swipe yakunlandi — orqaga qaytish
                                    offsetX.animateTo(screenWidth, tween(250))
                                    navigator.pop()
                                    offsetX.snapTo(0f)
                                } else {
                                    // Bekor qilindi — qaytish
                                    offsetX.animateTo(0f, tween(250))
                                }
                                isSwiping = false
                            }
                        },
                        onDragCancel = {
                            scope.launch {
                                offsetX.animateTo(0f, tween(250))
                                isSwiping = false
                            }
                        },
                        onHorizontalDrag = { _, dragAmount ->
                            if (!isSwiping) return@detectHorizontalDragGestures
                            scope.launch {
                                val newValue = (offsetX.value + dragAmount).coerceIn(0f, screenWidth)
                                offsetX.snapTo(newValue)
                            }
                        }
                    )
                }
        ) {
            navigator.lastItem.Content()
        }
    }
}