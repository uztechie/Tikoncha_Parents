package uz.tikoncha_parent.presentation.base

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import coil3.compose.AsyncImage
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.profile_hedgehog_img
import uz.tikoncha_parent.ui.ColorWhite
import uz.tikoncha_parent.ui.DialogButtonHeight
import uz.tikoncha_parent.ui.NormalTextSize
import uz.tikoncha_parent.ui.SpaceSmall
import uz.tikoncha_parent.ui.theme.ThemeMode
import uz.tikoncha_parent.ui.theme.TikonchaParentTheme
import uz.tikoncha_parent.ui.theme.extendedColor
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin


@Composable
fun ChildSelectionButton(
    modifier: Modifier = Modifier,
    text: String,
    label: String = "",
    imageUrl: String = "",
    onClick: () -> Unit,
    shape: Shape = CircleShape,
    fonSize: TextUnit = NormalTextSize,
    fontWeight: FontWeight = FontWeight.Normal,
    background: Color = MaterialTheme.extendedColor.cardColor,
) {
    // 1) Bitta anim progress (t)
    val infinite = rememberInfiniteTransition(label = "child_btn_glow")
    val t by infinite.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "t"
    )

    // 2) spread + alpha bitta progressdan
    val animatedSpreadDp = lerp(1.dp, 2.dp, t)
    val animatedAlpha = lerp1(0.55f, 0.95f, t)

    val glowColors = remember {
        listOf(
            Color(0xFFc3955b), // primary gold
            Color(0xFFE7C089), // light gold
            Color(0xFFFFE0B2), // warm cream
            Color(0xFFD4A373), // amber
            Color(0xFFE7C089), // light gold
            Color(0xFFc3955b), // primary gold
        )
    }

    val color = if (text.isEmpty()) MaterialTheme.extendedColor.hintColor else MaterialTheme.extendedColor.primaryColor
    val newText = text.ifEmpty { label }

    BoxWithConstraints(
        modifier = modifier.fillMaxWidth()
    ) {
        val density = LocalDensity.current
        val isCompact = maxWidth < 360.dp

        val buttonHeight =
            if (isCompact) DialogButtonHeight.coerceAtMost(48.dp) else DialogButtonHeight
        val avatarSize = if (isCompact) 28.dp else 30.dp
        val paddingH = if (isCompact) 10.dp else 12.dp

        val radiusPx = with(density) { (maxWidth * 1.25f).toPx() }

        val brush = rememberRotatingLinearGradient(
            colors = glowColors,
            radiusPx = radiusPx,
            durationMs = 3000
        )

        Row(
            modifier = modifier
                .fillMaxWidth()
                .height(buttonHeight)
                .dropShadow(
                    shape = shape,
                    shadow = Shadow(
                        radius = 10.dp,
                        spread = animatedSpreadDp,
                        brush = brush,
                        offset = DpOffset(0.dp, 0.dp),
                        alpha = animatedAlpha
                    )
                )
                .clip(shape)
                .background(background, shape)
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() },
                    onClick = {
                        onClick()
                    }
                )
                .padding(horizontal = paddingH),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(avatarSize)
                    .background(MaterialTheme.extendedColor.backgroundColor, CircleShape)
                    .padding(2.dp)
            ) {
                AsyncImage(
                    model = imageUrl,
                    contentDescription = "",
                    error = painterResource(Res.drawable.profile_hedgehog_img),
                    placeholder = painterResource(Res.drawable.profile_hedgehog_img),
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                )
            }
            SpaceSmall()

            CustomText(
                text = newText,
                fontSize = fonSize,
                fontWeight = fontWeight,
                color = color,
                maxLines = 1,
                modifier = Modifier
                    .weight(1f)
            )
        }
    }
}

@Stable
fun lerp1(start: Float, stop: Float, fraction: Float): Float {
    return start + (stop - start) * fraction.coerceIn(0f, 1f)
}

@Composable
fun rememberRotatingLinearGradient(
    colors: List<Color>,
    radiusPx: Float,
    durationMs: Int = 3000
): Brush {
    val infinite = rememberInfiniteTransition(label = "rotateGradient")
    val angle by infinite.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMs, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "angle"
    )

    val rad = angle * PI.toFloat() / 180f
    val dx = (cos(rad) * radiusPx).toFloat()
    val dy = (sin(rad) * radiusPx).toFloat()

    val start = Offset(-dx, -dy)
    val end = Offset(dx, dy)

    // start/end o'zgarsa brush qayta hosil bo'ladi
    return remember(colors, start, end) {
        Brush.linearGradient(
            colors = colors,
            start = start,
            end = end
        )
    }
}

@Preview
@Composable
private fun Pre() {
    TikonchaParentTheme(
        ThemeMode.DARK
    ){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(ColorWhite)
                .padding(vertical = 100.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            ChildSelectionButton(
                text = "",
                onClick = {},
                label = "Viloyat"
            )
        }
    }
}