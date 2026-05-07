package uz.tikoncha_parent.presentation.task

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import tikoncha_parents.composeapp.generated.resources.*
import uz.tikoncha_parent.ui.theme.AppColors
import uz.tikoncha_parent.ui.theme.AppTypography
import uz.tikoncha_parent.ui.theme.ThemeMode
import uz.tikoncha_parent.ui.theme.TikonchaParentTheme

@Composable
fun TaskSegmentedToggle(
    options: List<Pair<String, Painter?>>,
    selectedIndex: Int,
    style: TextStyle = AppTypography.bodyLgMedium,
    onOptionSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
    containerColor: Color = Color.Transparent,
    shape: Shape = RoundedCornerShape(100.dp)
) {

    val itemCount = options.size
    val itemWidth = remember { mutableStateOf(0.dp) }

    val density = LocalDensity.current

    Box(
        modifier = modifier
            .height(36.dp)
            .background(containerColor, shape)
            .border(1.dp, AppColors.border.secondarySubtle, shape)
            .padding(4.dp)
            .onGloballyPositioned { layoutCoordinates ->
                val totalWidth = layoutCoordinates.size.width

                itemWidth.value = with(density) {
                    (totalWidth.toDp() / itemCount)
                }
            }
    ) {
        // Sliding background indicator
        val indicatorOffset by animateDpAsState(
            targetValue = itemWidth.value * selectedIndex,
            label = "IndicatorOffset"
        )

        Box(
            modifier = Modifier
                .fillMaxHeight()
                .width(itemWidth.value)
                .offset(x = indicatorOffset)
                .clip(shape = shape)
                .background(AppColors.modal.primary)
        )

        Row(
            modifier = Modifier.fillMaxSize()
        ) {
            options.forEachIndexed { index, (label) ->

                Row(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) {
                            onOptionSelected(index)
                        },
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = label,
                        color = AppColors.text.primary,
                        style = style
                    )
                }
            }
        }
    }
}




@Preview
@Composable
private fun PRe() {
    TikonchaParentTheme(
        ThemeMode.DARK
    ){
        TaskSegmentedToggle(
            options = listOf(
                "Ertalabdan" to painterResource(Res.drawable.father_icon),
                "Tushlikdan" to painterResource(Res.drawable.mather_icon)
            ),
            selectedIndex = 0,
            onOptionSelected = {  },
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        )
    }
}
