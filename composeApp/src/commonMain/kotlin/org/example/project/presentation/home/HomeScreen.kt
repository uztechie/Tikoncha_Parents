package org.example.project.presentation.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import org.example.project.presentation.common.CustomListDialog
import org.example.project.presentation.common.CustomSelectionButton
import org.example.project.presentation.common.CustomText
import org.example.project.presentation.common.SegmentedToggle
import org.example.project.presentation.domain.model.UsagePeriod
import org.example.project.presentation.settings.SettingScreen
import org.example.project.ui.Background
import org.example.project.ui.BackgroundColor
import org.example.project.ui.ContainerPadding
import org.example.project.ui.DividerHorizontal
import org.example.project.ui.HintTextColor
import org.example.project.ui.LargeIconButtonPadding
import org.example.project.ui.LargeIconButtonSize
import org.example.project.ui.LargeTextSize
import org.example.project.ui.MainBorderColor
import org.example.project.ui.MainCornerRadius
import org.example.project.ui.NormalIconButtonPadding
import org.example.project.ui.NormalIconButtonSize
import org.example.project.ui.NormalLargeTextSize
import org.example.project.ui.NormalTextSize
import org.example.project.ui.PrimaryColor
import org.example.project.ui.SmallTextSize
import org.example.project.ui.SpaceMedium
import org.example.project.ui.SpaceSmall
import org.example.project.ui.SpaceUltraSmall
import org.example.project.ui.TextColor
import org.example.project.ui.TextFieldHeight
import org.example.project.ui.TonalButtonContainerColor
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.chart
import tikoncha_parents.composeapp.generated.resources.notification
import tikoncha_parents.composeapp.generated.resources.profile

class HomeScreen : Screen {

    @Composable
    override fun Content() {

        val list = remember {
            mutableStateListOf(
                AppUsage("", "Instagram", "", "1 soat"),
                AppUsage("", "You tube", "", "2 soat"),
                AppUsage("", "Tik Tok", "", "3 soat"),
                AppUsage("", "Pubg Mobile", "", "4 soat"),
                AppUsage("", "Mobile Legends Bing Bang", "", "5 soat"),
                AppUsage("", "Facebook", "", "6 soat"),
                AppUsage("", "Twitter", "", "7 soat"),
                AppUsage("", "Linkedin", "", "8 soat"),
                AppUsage("", "Duolingo", "", "9 soat"),
                AppUsage("", "Telegram", "", "10 soat"),
                AppUsage("", "Chrome", "", "11 soat"),
                AppUsage("", "Settings", "", "12 soat"),
            )
        }

        val childrenList = remember {
            mutableStateListOf(
                "Saidburxon",
                "Muhammadsaid",
                "Muhammadyusuf",
                "Beka"
            )
        }

        var showDialog by remember {
            mutableStateOf(false)
        }

        val navigator = LocalNavigator.current

        var selectionTypeIndex by remember {
            mutableIntStateOf(0)
        }
        var selectionType by remember {
            mutableStateOf(DateSelectionType.WEEK)
        }

        CustomListDialog(
            title = "Farzandlaringiz",
            items = childrenList,
            show = showDialog,
            onItemSelected = {

            },
            onDismiss = {
                showDialog = false
            }
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Background)
        ) {
//            Button(
//                onClick = {
//                    navigator?.push(SettingScreen())
//                }
//            ) {
//                Text(
//                    text = "Next"
//                )
//            }

            Row(
                modifier = Modifier
                    .clip(
                        RoundedCornerShape(
                            bottomStart = MainCornerRadius,
                            bottomEnd = MainCornerRadius
                        )
                    )
                    .border(
                        1.dp,
                        MainBorderColor,
                        RoundedCornerShape(
                            bottomStart = MainCornerRadius,
                            bottomEnd = MainCornerRadius
                        )
                    )
                    .background(BackgroundColor)
                    .fillMaxWidth()
                    .height(64.dp)
                    .padding(horizontal = ContainerPadding),
                verticalAlignment = Alignment.CenterVertically
            ) {

                FilledTonalIconButton(
                    modifier = Modifier
                        .size(NormalIconButtonSize),
                    onClick = {},
                    colors = IconButtonDefaults.filledTonalIconButtonColors(
                        containerColor = TonalButtonContainerColor,
                        contentColor = TextColor
                    ),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Image(
                        painter = painterResource(Res.drawable.chart),
                        contentDescription = "",
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(NormalIconButtonPadding)
                    )
                }

                SpaceMedium()
                CustomText(
                    text = "Bosh sahifa",
                    color = TextColor,
                    fontSize = LargeTextSize,
                    fontWeight = FontWeight.W500,
                    maxLines = 1
                )


                Spacer(Modifier.weight(1f))

                FilledTonalIconButton(
                    modifier = Modifier
                        .size(LargeIconButtonSize),
                    onClick = {},
                    colors = IconButtonDefaults.filledTonalIconButtonColors(
                        containerColor = TonalButtonContainerColor,
                        contentColor = TextColor
                    ),
                    shape = CircleShape
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.notification),
                        contentDescription = "",
                        tint = PrimaryColor,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(LargeIconButtonPadding)
                    )
                }

            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        start = ContainerPadding,
                        end = ContainerPadding,
                        bottom = ContainerPadding,
                        top = NormalIconButtonPadding
                    )
                    .verticalScroll(rememberScrollState())
            ) {

                CustomText(
                    text = "Farzandingizning telefon ishlatish statistikasi",
                    color = HintTextColor,
                    fontSize = NormalTextSize,
                    modifier = Modifier.fillMaxWidth()
                )

                SpaceUltraSmall()

                CustomSelectionButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(TextFieldHeight),
                    text = "Saidburxon",
                    painter = painterResource(Res.drawable.profile),
                    onClick = {
                        showDialog = true
                    }
                )

                SpaceMedium()

                SegmentedToggle(
                    options = listOf("Haftalik" to null, "Kunlik" to null),
                    selectedIndex = selectionTypeIndex,
                    modifier = Modifier
                        .fillMaxWidth(),
                    onOptionSelected = {
                        selectionTypeIndex = it
                        selectionType =
                            if (it == 0) DateSelectionType.WEEK else DateSelectionType.DAY
                    }
                )

                SpaceMedium()

//                DateSelectorSlider(
//                    type = selectionType,
//                    periodsDate = if (selectionType == DateSelectionType.WEEK) state.weeklyPeriods else state.dailyPeriods,
//                    onDateSelected = {
//                        event(HomeEvent.GetUsageList(it, selectionType))
//                    }
//                )

                SpaceUltraSmall()

                CustomText(
                    text = "Bir kunda o'rtacha 5 soat 44 minut",
                    modifier = Modifier
                        .fillMaxWidth(),
                    fontSize = SmallTextSize,
                    color = HintTextColor,
                    textAlign = TextAlign.Center
                )

                SpaceUltraSmall()

                UsageBarChart(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    data = mapOf(
                        1 to 23.0,
                        2 to 30.5,
                        3 to 13.0,
                        4 to 22.0,
                        5 to 32.6,
                        6 to 5.8,
                        7 to 14.5
                    )
                )

                SpaceMedium()

                CustomText(
                    text = "Ijtimoiy tarmoqlar",
                    color = TextColor,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = NormalLargeTextSize
                )

                SpaceSmall()

                list.forEach { item ->
                    AppUsageItem(appUsage = item)
                    SpaceUltraSmall()
                    DividerHorizontal()
                }

            }

        }
    }

}

@Preview
@Composable
fun Pre() {

    HomeScreen().Content()
}

