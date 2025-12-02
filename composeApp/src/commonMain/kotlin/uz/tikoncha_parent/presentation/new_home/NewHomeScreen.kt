package uz.tikoncha_parent.presentation.new_home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinNavigatorScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import org.jetbrains.compose.resources.Font
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.baloo_2_medium
import tikoncha_parents.composeapp.generated.resources.farzandlaringiz
import tikoncha_parents.composeapp.generated.resources.*
import tikoncha_parents.composeapp.generated.resources.instagram_icon
import tikoncha_parents.composeapp.generated.resources.linkedin_icon
import tikoncha_parents.composeapp.generated.resources.notification
import tikoncha_parents.composeapp.generated.resources.profile
import tikoncha_parents.composeapp.generated.resources.whatsapp_icon
import uz.saidburxon.newedu.presentation.base.CustomText
import uz.tikoncha_parent.platform.Logger
import uz.tikoncha_parent.presentation.base.ChildSelectionButton
import uz.tikoncha_parent.presentation.chat.ChatScreen
import uz.tikoncha_parent.presentation.common.CustomListDialog
import uz.tikoncha_parent.presentation.map.MapScreen
import uz.tikoncha_parent.presentation.notification.NotificationScreen
import uz.tikoncha_parent.presentation.policy.PolicyListScreen
import uz.tikoncha_parent.presentation.profile.ProfileScreen
import uz.tikoncha_parent.presentation.statistic.StatisticScreen
import uz.tikoncha_parent.presentation.task.TaskScreen
import uz.tikoncha_parent.presentation.ui_state.ResponseState
import uz.tikoncha_parent.presentation.ui_state.errorText
import uz.tikoncha_parent.ui.CardCornerPadding
import uz.tikoncha_parent.ui.CardCornerRadius
import uz.tikoncha_parent.ui.ContainerPadding
import uz.tikoncha_parent.ui.NormalIconSize
import uz.tikoncha_parent.ui.NormalTextSize
import uz.tikoncha_parent.ui.PrimaryColor
import uz.tikoncha_parent.ui.SmallIconSize
import uz.tikoncha_parent.ui.SpaceLarge
import uz.tikoncha_parent.ui.SpaceUltraSmall
import uz.tikoncha_parent.ui.TextFieldCornerRadius
import uz.tikoncha_parent.ui.theme.ThemeMode
import uz.tikoncha_parent.ui.theme.TikonchaParentTheme
import uz.tikoncha_parent.ui.theme.extendedColor


class NewHomeScreen : Screen {


    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current?:return

        val viewModel = navigator.koinNavigatorScreenModel<HomeViewModel>()
        val state = viewModel.state.collectAsStateWithLifecycle()
        val event = viewModel::onEvent

        Logger.d("NewHomeScreen", "Content")

        NewHomeUi(
            navigator = navigator,
            state = state.value,
            event = event
        )
    }
}
@Composable
fun NewHomeUi(
    navigator: Navigator?,
    state: HomeState,
    event: (HomeEvent) -> Unit
){

    LaunchedEffect(true){
        event(HomeEvent.GetChildren)
    }


    Logger.d("NewHomeScreen", "NewHomeUi")
    val baloo2 = FontFamily(
        Font(Res.font.baloo_2_medium)
    )

    var showDialog by remember {
        mutableStateOf(false)
    }

    val childrenLoading = state.childrenResponseState is ResponseState.Loading
    val childrenErrorText = state.childrenResponseState.errorText()

    CustomListDialog(
        title = stringResource(Res.string.farzandlaringiz),
        items = state.childrenList,
        show = showDialog,
        loading = childrenLoading,
        errorMessage = childrenErrorText,
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
            .background(MaterialTheme.extendedColor.backgroundColor)
            .padding(ContainerPadding)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {

            ChildSelectionButton(
                modifier = Modifier
                    .widthIn(120.dp, 160.dp),
                text = state.selectedChild?.name?:"",
                label = stringResource(Res.string.farzandingizni_tanlang),
                onClick = {
                    showDialog = true
                },
            )

            Spacer(Modifier.weight(1f))

            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(MaterialTheme.extendedColor.cardColor)
                    .padding(12.dp)
                    .clip(CircleShape)
                    .clickable{
                        navigator?.push(NotificationScreen())
                    }
            ){
                Image(
                    painter = painterResource(Res.drawable.notification),
                    contentDescription = "",
                    colorFilter = ColorFilter.tint(MaterialTheme.extendedColor.primaryAlphaColor),
                    modifier = Modifier
                        .size(NormalIconSize)
                )
            }
            SpaceUltraSmall()
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(MaterialTheme.extendedColor.cardColor)
                    .padding(12.dp)
                    .clip(CircleShape)
                    .clickable{
                        navigator?.push(ProfileScreen())
                    }
            ){
                Image(
                    painter = painterResource(Res.drawable.profile),
                    contentDescription = "",
                    modifier = Modifier
                        .size(NormalIconSize),
                    colorFilter = ColorFilter.tint(MaterialTheme.extendedColor.primaryAlphaColor)
                )
            }
        }
        SpaceLarge()

        LazyColumn(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(ContainerPadding)
        ){
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(130.dp)
                        .background(MaterialTheme.extendedColor.cardColor, RoundedCornerShape(CardCornerRadius))
                        .clip(RoundedCornerShape(CardCornerRadius))
                        .clickable{
                            navigator?.push(StatisticScreen())
                        }
                        .padding(CardCornerPadding),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier
                            .weight(1f)
                    ) {
                        CustomText(
                            text = "25 daq",
                            color = MaterialTheme.extendedColor.titleColor,
                            fontSize = 30.sp,
                            fontWeight = FontWeight.W500,
                        )
                        CustomText(
                            text = "bugun sarfladi",
                            color = PrimaryColor,
                            fontSize = NormalTextSize,
                        )
                    }

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.Bottom
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxHeight(0.5f)
                                .background(MaterialTheme.extendedColor.backgroundColor, RoundedCornerShape(TextFieldCornerRadius))
                                .padding(6.dp),
                            contentAlignment = Alignment.BottomCenter
                        ){
                            Image(
                                painter = painterResource(Res.drawable.linkedin_icon),
                                contentDescription = "",
                                modifier = Modifier
                                    .size(SmallIconSize),
                                alignment = Alignment.BottomCenter,
                            )
                        }
                        Box(
                            modifier = Modifier
                                .fillMaxHeight(0.65f)
                                .background(MaterialTheme.extendedColor.backgroundColor, RoundedCornerShape(TextFieldCornerRadius))
                                .padding(6.dp),
                            contentAlignment = Alignment.BottomCenter
                        ){
                            Image(
                                painter = painterResource(Res.drawable.whatsapp_icon),
                                contentDescription = "",
                                modifier = Modifier
                                    .size(SmallIconSize),
                                alignment = Alignment.BottomCenter,
                            )
                        }
                        Box(
                            modifier = Modifier
                                .fillMaxHeight(0.8f)
                                .background(MaterialTheme.extendedColor.backgroundColor, RoundedCornerShape(TextFieldCornerRadius))
                                .padding(6.dp),
                            contentAlignment = Alignment.BottomCenter
                        ){
                            Image(
                                painter = painterResource(Res.drawable.instagram_icon),
                                contentDescription = "",
                                modifier = Modifier
                                    .size(SmallIconSize),
                                alignment = Alignment.BottomCenter,
                            )
                        }
                    }
                }
            }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(130.dp)
                        .background(MaterialTheme.extendedColor.cardColor, RoundedCornerShape(CardCornerRadius))
                        .padding(horizontal = CardCornerPadding, vertical = ContainerPadding)
                        .clip(RoundedCornerShape(CardCornerRadius))
                        .clickable {
                            navigator?.push(TaskScreen())
                        },
                ) {
                    Column(
                        modifier = Modifier
                            .weight(1f)
                    ) {
                        CustomText(
                            text = "Topshiriqlar",
                            color = MaterialTheme.extendedColor.titleColor,
                            fontSize = 30.sp,
                            fontWeight = FontWeight.W500,
                        )
                        CustomText(
                            text = "4 ta faol vazifa",
                            color = PrimaryColor,
                            fontSize = NormalTextSize,
                        )
                    }

                    Image(
                        painter = painterResource(Res.drawable.home_task),
                        contentDescription = "",
                        modifier = Modifier
                            .size(60.dp)
                            .align(Alignment.Bottom)
                    )
                }
            }
            
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(130.dp)
                        .background(MaterialTheme.extendedColor.cardColor, RoundedCornerShape(CardCornerRadius))
                        .padding(horizontal = CardCornerPadding, vertical = ContainerPadding)
                        .clip(RoundedCornerShape(CardCornerRadius))
                        .clickable{
                            navigator?.push(PolicyListScreen(
                                state.selectedChild
                            ))
                        },
                ) {
                    Column(
                        modifier = Modifier
                            .weight(1f)
                    ) {
                        CustomText(
                            text = "Jadvallar",
                            color = MaterialTheme.extendedColor.titleColor,
                            fontSize = 30.sp,
                            fontWeight = FontWeight.W500,
                        )
                        CustomText(
                            text = "42 ta ilova cheklangan",
                            color = PrimaryColor,
                            fontSize = NormalTextSize,
                        )
                    }

                    Image(
                        painter = painterResource(Res.drawable.home_table),
                        contentDescription = "",
                        modifier = Modifier
                            .size(60.dp)
                            .align(Alignment.Bottom)
                    )
                }
            }

            item {
                NewHomeItem(
                    onSettingSelected = {selectionItem ->
                        when(selectionItem){
                            HomeSelectionItem.XARITA -> {
                                navigator?.push(MapScreen())
                            }
                            HomeSelectionItem.SIHBAT -> {
                                navigator?.push(ChatScreen())
                            }
                        }
                    }
                )
            }
        }
    }
}

@Preview
@Composable
private fun Pre(){
    TikonchaParentTheme(
        ThemeMode.DARK
    ){
        NewHomeUi(
            navigator = null,
            state = HomeState(),
            event = {}
        )
    }
}