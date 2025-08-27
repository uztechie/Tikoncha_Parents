package org.example.project.presentation.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import org.example.project.presentation.base.CustomSelectionButton
import org.example.project.presentation.base.SegmentedToggle
import org.example.project.presentation.common.CustomListDialog
import org.example.project.ui.*
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import tikoncha_parents.composeapp.generated.resources.*
import uz.saidburxon.newedu.presentation.base.CustomText


class HomeScreen : Screen {

    @Composable
    override fun Content() {

        val viewModel = koinViewModel<HomeViewModel>()
        val state = viewModel.state.collectAsStateWithLifecycle()
        val event = viewModel::onEvent

        val navigator = LocalNavigator.current

        HomeUi(
            navigator = navigator,
            state = state.value,
            event = event
        )
    }

}

@Composable
fun HomeUi(
    navigator: Navigator?,
    state: HomeState,
    event: (HomeEvent) -> Unit
) {


    val bottomRoundedShape = RoundedCornerShape(
        topStart = 0.dp,
        topEnd = 0.dp,
        bottomStart = ShapeCornerRadius,
        bottomEnd = ShapeCornerRadius
    )


    val list = remember {
        mutableStateListOf(
            AppUsageUi("", "Instagram", "", "1 soat"),
            AppUsageUi("", "You tube", "", "2 soat"),
            AppUsageUi("", "Tik Tok", "", "3 soat"),
            AppUsageUi("", "Pubg Mobile", "", "4 soat"),
            AppUsageUi("", "Mobile Legends Bing Bang", "", "5 soat"),
            AppUsageUi("", "Facebook", "", "6 soat"),
            AppUsageUi("", "Twitter", "", "7 soat"),
            AppUsageUi("", "Linkedin", "", "8 soat"),
            AppUsageUi("", "Duolingo", "", "9 soat"),
            AppUsageUi("", "Telegram", "", "10 soat"),
            AppUsageUi("", "Chrome", "", "11 soat"),
            AppUsageUi("", "Settings", "", "12 soat"),
        )
    }

    var showDialog by remember {
        mutableStateOf(false)
    }

    var selectionTypeIndex by remember {
        mutableIntStateOf(0)
    }
    var selectionType by remember {
        mutableStateOf(DateSelectionType.WEEK)
    }

    CustomListDialog(
        title = stringResource(Res.string.farzandlaringiz),
        items = state.childrenList,
        show = showDialog,
        onItemSelected = {
            event(HomeEvent.OnChildSelected(it))
        },
        onDismiss = {
            showDialog = false
        }
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
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

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(HeaderHeight)
                .shadow(
                    elevation = 4.dp,
                    shape = bottomRoundedShape,
                    ambientColor = MaterialTheme.colorScheme.primary, // 🌈 Soya rangi shu yerda
                    spotColor = MaterialTheme.colorScheme.primary     // Android 12+ uchun
                )
                .padding(bottom = 4.dp),
            shape = bottomRoundedShape,
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.background
            ),
        ) {
            Row(
                modifier = Modifier
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
                        containerColor = MaterialTheme.colorScheme.scrim,
                        contentColor = MaterialTheme.colorScheme.onBackground
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
                    text = stringResource(Res.string.bosh_sahifa),
                    fontSize = LargeTextSize,
                    maxLines = 1
                )


                Spacer(Modifier.weight(1f))

                FilledTonalIconButton(
                    modifier = Modifier
                        .size(LargeIconButtonSize),
                    onClick = {

                    },
                    colors = IconButtonDefaults.filledTonalIconButtonColors(
                        containerColor = MaterialTheme.colorScheme.scrim,
                        contentColor = MaterialTheme.colorScheme.onBackground
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
                text = stringResource(Res.string.farzandlaringiz_telefon_ishlatish_statistikasi),
                color = MaterialTheme.colorScheme.secondary,
                fontSize = NormalTextSize,
                modifier = Modifier.fillMaxWidth()
            )

            SpaceUltraSmall()

            CustomSelectionButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(TextFieldHeight),
                text = state.selectedChildren,
                painter = painterResource(Res.drawable.profile),
                onClick = {
                    showDialog = true
                }
            )

            SpaceMedium()

            SegmentedToggle(
                options = listOf(
                    stringResource(Res.string.haftalik) to null,
                    stringResource(Res.string.kunlik) to null
                ),
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

            DateSelectorSlider(
                type = selectionType,
                periodsDate = if (selectionType == DateSelectionType.WEEK) state.weeklyPeriods else state.dailyPeriods,
                onDateSelected = {
                    event(HomeEvent.GetUsageList(it, selectionType))
                }
            )

            SpaceUltraSmall()

            CustomText(
                text = "Bir kunda o'rtacha 5 soat 44 minut",
                modifier = Modifier
                    .fillMaxWidth(),
                fontSize = SmallTextSize,
                color = MaterialTheme.colorScheme.secondary,
                textAlign = TextAlign.Center
            )

            SpaceUltraSmall()

            UsageBarChart(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                data = if (selectionType == DateSelectionType.DAY) state.dailyChartData else state.weeklyChartData
            )

            if (state.socialAppUsageUiLists.isNotEmpty()) {
                SpaceMedium()

                CustomText(
                    text = stringResource(Res.string.ijtimoiy_tarmoqlar),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = NormalLargeTextSize
                )

                SpaceSmall()

                state.socialAppUsageUiLists.forEach { item ->
                    AppUsageItem(
                        appUsageUi = item,
                        onClickLock = { appUsageUi ->
                            appUsageUi.copy(allowed = !appUsageUi.allowed)
                        }
                    )
                    SpaceUltraSmall()
                    DividerHorizontal()
                }
            }

            if (state.gameUsageList.isNotEmpty()) {
                SpaceMedium()

                CustomText(
                    text = stringResource(Res.string.oyinlar),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = NormalLargeTextSize
                )

                SpaceSmall()

                state.gameUsageList.forEach { item ->
                    AppUsageItem(
                        appUsageUi = item,
                        onClickLock = { appUsageUi ->
                            appUsageUi.copy(allowed = !appUsageUi.allowed)
                        }
                    )
                    SpaceUltraSmall()
                    DividerHorizontal()
                }
            }

            if (state.otherAppUsageUiLists.isNotEmpty()) {
                SpaceMedium()

                CustomText(
                    text = stringResource(Res.string.boshqalar),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = NormalLargeTextSize
                )

                SpaceSmall()

                state.otherAppUsageUiLists.forEach { item ->
                    AppUsageItem(
                        appUsageUi = item,
                        onClickLock = { appUsageUi ->
                            item.copy(allowed = appUsageUi.allowed)
                            event(HomeEvent.OnLockClicked(appUsageUi))
                        }
                    )
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

    HomeUi(
        navigator = null,
        state = HomeState(),
        event = {}
    )
}

