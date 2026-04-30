package uz.tikoncha_parent.presentation.navigation

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.Navigator
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.roundToInt

expect val isSwipeBackEnabled: Boolean

@Composable
fun SwipeBackContent(navigator: Navigator) {
    if (!isSwipeBackEnabled || !navigator.canPop) {
        navigator.lastItem.Content()
        return
    }

    val scope = rememberCoroutineScope()
    val offsetX = remember { Animatable(0f) }
    var screenWidth by remember { mutableStateOf(1f) }

    var isSwiping by remember { mutableStateOf(false) }
    var gestureStarted by remember { mutableStateOf(false) }

    val previousScreen = remember(navigator.items) {
        if (navigator.items.size >= 2) navigator.items[navigator.items.size - 2] else null
    }

    val progress = if (screenWidth > 0f) (offsetX.value / screenWidth).coerceIn(0f, 1f) else 0f

    Box(
        modifier = Modifier
            .fillMaxSize()
            .onSizeChanged { screenWidth = it.width.toFloat() }
    ) {
        // ── Oldingi screen (parallax) ──
        if (isSwiping && previousScreen != null) {
            val prevOffset = (-screenWidth * 0.3f * (1f - progress)).roundToInt()

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .offset { IntOffset(prevOffset, 0) }
            ) {
                previousScreen.Content()
            }

            // Qorong'ulashish overlay
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .drawWithContent {
                        drawContent()
                        drawRect(Color.Black.copy(alpha = 0.15f * (1f - progress)))
                    }
            )
        }

        // ── Hozirgi screen ──
        Box(
            modifier = Modifier
                .fillMaxSize()
                .offset { IntOffset(offsetX.value.roundToInt(), 0) }
                .then(
                    if (isSwiping) {
                        Modifier.shadow(
                            elevation = (8.dp * (1f - progress)),
                            ambientColor = Color.Black.copy(alpha = 0.2f)
                        )
                    } else Modifier
                )
                .pointerInput(navigator.lastItem.key) {
                    awaitEachGesture {
                        val down = awaitFirstDown(pass = PointerEventPass.Initial)
                        val startX = down.position.x

                        // ── Edge zone: ekranning chap 15% qismi (kamida 50dp) ──
                        val edgeZone = maxOf(screenWidth * 0.15f, 50.dp.toPx())
                        if (startX > edgeZone) return@awaitEachGesture

                        gestureStarted = true
                        var totalDragX = 0f
                        var totalDragY = 0f
                        var directionDecided = false

                        try {
                            while (true) {
                                val event = awaitPointerEvent(PointerEventPass.Initial)
                                val change = event.changes.firstOrNull() ?: break

                                if (!change.pressed) {
                                    // Barmoq ko'tarildi
                                    if (isSwiping) {
                                        val threshold = screenWidth * 0.3f
                                        scope.launch {
                                            if (offsetX.value > threshold) {
                                                offsetX.animateTo(
                                                    screenWidth,
                                                    tween(200, easing = FastOutSlowInEasing)
                                                )
                                                navigator.pop()
                                                offsetX.snapTo(0f)
                                            } else {
                                                offsetX.animateTo(
                                                    0f,
                                                    tween(250, easing = FastOutSlowInEasing)
                                                )
                                            }
                                            isSwiping = false
                                        }
                                    }
                                    gestureStarted = false
                                    break
                                }

                                val delta = change.positionChange()
                                totalDragX += delta.x
                                totalDragY += delta.y

                                // Yo'nalishni aniqlash — 10px dan keyin
                                if (!directionDecided && (abs(totalDragX) > 10f || abs(totalDragY) > 10f)) {
                                    directionDecided = true
                                    if (abs(totalDragX) < abs(totalDragY) || totalDragX < 0) {
                                        // Vertikal yoki chapga — swipe emas
                                        gestureStarted = false
                                        break
                                    }
                                    isSwiping = true
                                }

                                if (isSwiping) {
                                    change.consume()
                                    scope.launch {
                                        val newVal = (offsetX.value + delta.x).coerceIn(0f, screenWidth)
                                        offsetX.snapTo(newVal)
                                    }
                                }
                            }
                        } catch (_: Exception) {
                            scope.launch {
                                if (isSwiping) {
                                    offsetX.animateTo(0f, tween(250))
                                    isSwiping = false
                                }
                            }
                            gestureStarted = false
                        }
                    }
                }
        ) {
            navigator.lastItem.Content()
        }
    }
}