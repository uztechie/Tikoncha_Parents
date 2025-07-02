package org.example.project.presentation.common

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import org.example.project.ui.NormalTextSize
import org.example.project.ui.OnPrimaryColor
import org.example.project.ui.PrimaryColor
import org.example.project.ui.TextFieldCornerRadius
import org.example.project.ui.TextFieldHeight
import org.example.project.ui.TextFieldIconSize
import org.example.project.ui.TextFieldInnerPadding

@Composable
fun SegmentedToggle(
    options: List<Pair<String, Painter?>>,
    selectedIndex: Int,
    fontSize: TextUnit = NormalTextSize,
    onOptionSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val TAG = "SegmentedToggle"
    val itemCount = options.size
    val itemWidth = remember { mutableStateOf(0.dp) }

    val density = LocalDensity.current

    Box(
        modifier = modifier
            .height(TextFieldHeight)

            .clip(RoundedCornerShape(TextFieldCornerRadius))
            .border(1.dp, PrimaryColor, RoundedCornerShape(TextFieldCornerRadius))
            .padding(4.dp)
            .onGloballyPositioned { layoutCoordinates ->
                val totalWidth = layoutCoordinates.size.width

                itemWidth.value = with(density) {
                    (totalWidth.toDp() / itemCount)
                }
                println("SegmentedToggle: totalWidth: $totalWidth  itemWidth=${itemWidth.value}  selectedIndex=$selectedIndex, itemCount=$itemCount")

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
                .clip(RoundedCornerShape(TextFieldCornerRadius))
                .background(PrimaryColor)
        )

        Row(
            modifier = Modifier.fillMaxSize()
        ) {
            options.forEachIndexed { index, (label, icon) ->
                val isSelected = index == selectedIndex

                val contentColor by animateColorAsState(
                    targetValue = if (isSelected) OnPrimaryColor else Color.Black,
                    label = "ContentColor"
                )

                val iconColor by animateColorAsState(
                    targetValue = if (isSelected) OnPrimaryColor else PrimaryColor,
                    label = "IconColor"
                )

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
                    if (icon != null) {
                        Icon(
                            painter = icon,
                            contentDescription = label,
                            tint = iconColor,
                            modifier = Modifier.size(TextFieldIconSize)
                        )
                        Spacer(modifier = Modifier.width(TextFieldInnerPadding))
                    }

                    CustomText(
                        text = label,
                        color = contentColor,
                        fontWeight = FontWeight.Bold,
                        fontSize = fontSize
                    )
                }
            }
        }
    }
}