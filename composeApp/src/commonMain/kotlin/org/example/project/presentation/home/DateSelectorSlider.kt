package org.example.project.presentation.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import org.example.project.presentation.common.CustomText
import org.example.project.presentation.domain.model.UsagePeriod
import org.example.project.ui.PrimaryColor
import org.example.project.ui.SmallTextSize
import org.example.project.ui.TextColor
import org.example.project.ui.TonalButtonContainerColor
import org.jetbrains.compose.resources.painterResource
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.date_selection_arrow_left
import tikoncha_parents.composeapp.generated.resources.date_selection_arrow_right

@Composable
fun DateSelectorSlider(
    type: DateSelectionType,
    modifier: Modifier = Modifier,
    periodsDate: List<UsagePeriod>,
    onDateSelected:(UsagePeriod) -> Unit
) {
    if (periodsDate.isEmpty()){
        return
    }

    var selectedIndex by remember {
        mutableIntStateOf(periodsDate.lastIndex)
    }

    LaunchedEffect(periodsDate) {
        selectedIndex = periodsDate.lastIndex
    }



    val density = LocalDensity.current

    val pagerState = rememberPagerState(initialPage = periodsDate.lastIndex){
        periodsDate.size
    }

    LaunchedEffect(selectedIndex) {
        if (selectedIndex>=0 && selectedIndex < pagerState.pageCount){
            pagerState.animateScrollToPage(selectedIndex)
            onDateSelected(periodsDate[selectedIndex])
        }

    }

    val selectionType = if (type == DateSelectionType.DAY){
        "Kunlik"
    }
    else{
        "Haftalik"
    }

    val textMeasurer = rememberTextMeasurer()
    val textSize = SmallTextSize
    val maxTextWidthDp by remember {
        mutableStateOf(
            try {
                with(density) {
                    val titleWidth = periodsDate.maxOf {
                        textMeasurer.measure(
                            AnnotatedString(it.label),
                            style = TextStyle(fontSize = textSize)
                        ).size.width
                    }.toDp()

                    val typeWidth = textMeasurer.measure(
                        AnnotatedString(selectionType),
                        style = TextStyle(fontSize = textSize)
                    ).size.width.toDp()

                    titleWidth+typeWidth
                }
            }catch (e: Exception){
                e.printStackTrace()
                0.dp
            }

        )
    }


    Row(
        modifier = modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterHorizontally)
    ) {

        if (selectedIndex == 0){
            Spacer(
                Modifier.size(40.dp)
            )
        }
        else{
            FilledTonalIconButton(
                onClick = {
                    if (selectedIndex > 0) {
                        selectedIndex = selectedIndex-1
                    }
                },
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = TonalButtonContainerColor,
                    contentColor = PrimaryColor
                ),
                modifier = Modifier
                    .size(40.dp),
                shape = RoundedCornerShape(15.dp)
            ) {
                Icon(
                    painter = painterResource(Res.drawable.date_selection_arrow_left),
                    contentDescription = "",
                    modifier = Modifier
                        .size(24.dp)
                )
            }
        }




        HorizontalPager(
            userScrollEnabled = false,
            state = pagerState,
            modifier = Modifier
                .width(maxTextWidthDp + 20.dp) // fixed width!
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ){
                CustomText(

                    text = if (selectedIndex in periodsDate.indices)
                        "$selectionType ${periodsDate[selectedIndex].label}"
                    else
                        "",
                    color = TextColor,
                    fontSize = textSize,
                    modifier = Modifier
                )
            }

        }

        if (selectedIndex == periodsDate.lastIndex){
            Spacer(
                Modifier.size(40.dp)
            )
        }
        else{
            FilledTonalIconButton(
                onClick = {
                    if (selectedIndex < periodsDate.lastIndex) {
                        selectedIndex = selectedIndex + 1
                    }
                },
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = TonalButtonContainerColor,
                    contentColor = PrimaryColor
                ),
                modifier = Modifier
                    .size(40.dp),
                shape = RoundedCornerShape(15.dp)
            ) {
                Icon(
                    painter = painterResource(Res.drawable.date_selection_arrow_right),
                    contentDescription = "",
                    modifier = Modifier
                        .size(24.dp)
                )
            }
        }
    }
}