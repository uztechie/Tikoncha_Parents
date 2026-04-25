package uz.tikoncha_parent.presentation.profile.children

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.farzand_malumotlari_keyin_korinadi
import tikoncha_parents.composeapp.generated.resources.farzand_qoshilmagan
import tikoncha_parents.composeapp.generated.resources.farzandlaringiz
import uz.tikoncha_parent.common.Util.normalizePhone
import uz.tikoncha_parent.presentation.base.AppEmptyList
import uz.tikoncha_parent.presentation.base.CustomHeader
import uz.tikoncha_parent.presentation.new_home.HomeEvent
import uz.tikoncha_parent.presentation.profile.ProfileEvent
import uz.tikoncha_parent.presentation.profile.ProfileState
import uz.tikoncha_parent.presentation.profile.ProfileViewModel
import uz.tikoncha_parent.presentation.profile.child_user_edit.EditScreen
import uz.tikoncha_parent.presentation.profile.personal_information.PersonalInfoItem
import uz.tikoncha_parent.presentation.statistic.StatisticEvent
import uz.tikoncha_parent.ui.CardCornerRadius
import uz.tikoncha_parent.ui.ContainerPadding
import uz.tikoncha_parent.ui.PrimaryColor
import uz.tikoncha_parent.ui.theme.AppColors
import uz.tikoncha_parent.ui.theme.ThemeMode
import uz.tikoncha_parent.ui.theme.TikonchaParentTheme
import uz.tikoncha_parent.ui.theme.extendedColor
import uz.tikoncha_parent.ui.theme.rememberScreenSystemBars


class   ChildrenScreen(
    private val highlightPhone: String? = null
) : Screen {
    @Composable
    override fun Content() {

        val navigator = LocalNavigator.current

        val viewModel = koinScreenModel<ProfileViewModel>()
        val state = viewModel.state.collectAsStateWithLifecycle()
        val event = viewModel::onEvent

        LaunchedEffect(Unit){
            viewModel.getChildren()
        }

        ChildrenUi(
            event = event,
            state = state.value,
            navigator = navigator,
            highlightPhone = highlightPhone
        )
    }
}

@Composable
fun ChildrenUi(
    state: ProfileState,
    navigator: Navigator?,
    event: (ProfileEvent) -> Unit,
    highlightPhone: String? = null
){
    val listState = rememberLazyListState()
    var highlightedUserId by remember { mutableStateOf<String?>(null) }
    val normalizedTarget = remember(highlightPhone) { normalizePhone(highlightPhone) }

    // Ro'yxat kelganda scroll qilib, highlight beramiz
    LaunchedEffect(state.children, normalizedTarget) {
        if (normalizedTarget == null || state.children.isEmpty()) return@LaunchedEffect

        val index = state.children.indexOfFirst {
            normalizePhone(it.phoneNumber) == normalizedTarget
        }
        if (index !in state.children.indices) return@LaunchedEffect

        highlightedUserId = state.children[index].userId
        delay(200) // layout o'tirib olishi uchun
        runCatching { listState.animateScrollToItem(index) }
        delay(2000) // highlight ko'rinib tursin
        highlightedUserId = null
    }

    val systemBars = rememberScreenSystemBars(
        statusBarColor = AppColors.bg.page,
        navigationBarColor = AppColors.bg.page
    )

    var isRefreshing by remember { mutableStateOf(false) }
    val refreshScope = rememberCoroutineScope()

    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = {
            refreshScope.launch {
                isRefreshing = true
                event(ProfileEvent.Refresh)
                delay(500)
                isRefreshing = false
            }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .then(systemBars.modifier)
                .background(AppColors.bg.page)
        ) {
            CustomHeader(
                title = stringResource(Res.string.farzandlaringiz),
                showBackButton = true,
                onBackClick = {
                    navigator?.pop()
                }
            )

            if (state.children.isEmpty()) {
                AppEmptyList(
                    title = stringResource(Res.string.farzand_qoshilmagan),
                    message = stringResource(Res.string.farzand_malumotlari_keyin_korinadi),
                )
            }

            LazyColumn(
                state = listState,
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(ContainerPadding),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                items(state.children, key = { it.userId }) { userInfo ->
                    val isHighlighted = userInfo.userId == highlightedUserId
                    val borderColor by animateColorAsState(
                        targetValue = if (isHighlighted) PrimaryColor else Color.Transparent,
                        animationSpec = tween(600),
                        label = "highlightBorder"
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(
                                width = 2.dp,
                                color = borderColor,
                                shape = RoundedCornerShape(CardCornerRadius)
                            )
                    ) {
                        PersonalInfoItem(
                            userInfo,
                            onEdit = {
                                navigator?.push(EditScreen(userInfo))
                            }
                        )
                    }
                }
            }
        }
    }
}


@Preview
@Composable
private fun PreviewPersonalInformationScreen() {
    TikonchaParentTheme(
        ThemeMode.DARK
    ){
        ChildrenUi(
            navigator = null,
            state = ProfileState(),
            event = {}
        )
    }
}