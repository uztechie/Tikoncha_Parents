@file:Suppress("EQUALITY_NOT_APPLICABLE_WARNING")

package uz.tikoncha_parent.presentation.home.schedule

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.ilovalar
import tikoncha_parents.composeapp.generated.resources.jadvallar
import tikoncha_parents.composeapp.generated.resources.saqlash
import tikoncha_parents.composeapp.generated.resources.search_normal
import tikoncha_parents.composeapp.generated.resources.veb_sayt
import uz.saidburxon.newedu.presentation.base.CustomButton
import uz.saidburxon.newedu.presentation.base.CustomText
import uz.tikoncha_parent.data.mapper.AppCategoryUi
import uz.tikoncha_parent.data.mapper.AppsUi
import uz.tikoncha_parent.presentation.base.CustomHeader
import uz.tikoncha_parent.presentation.base.CustomTextField
import uz.tikoncha_parent.presentation.base.SegmentedToggle
import uz.tikoncha_parent.presentation.home.schedule.timelist.ScheduleTimeListScreen
import uz.tikoncha_parent.ui.ButtonHeight
import uz.tikoncha_parent.ui.ContainerPadding
import uz.tikoncha_parent.ui.NormalTextSize
import uz.tikoncha_parent.ui.SpaceLarge
import uz.tikoncha_parent.ui.SpaceMedium
import uz.tikoncha_parent.ui.TextFieldHeight
import uz.tikoncha_parent.ui.theme.extendedColor

class ScheduleScreen : Screen {
    @Composable
    override fun Content() {

        val navigator = LocalNavigator.current

        val viewModel = koinViewModel<ScheduleViewModel>()
        val state = viewModel.state.collectAsStateWithLifecycle()
        val event = viewModel::onEvent

        ScheduleUi(
            navigator = navigator,
            state = state.value,
            event = event
        )
    }
}

@Composable
fun ScheduleUi(
    navigator: Navigator?,
    state: ScheduleState,
    event: (ScheduleEvent) -> Unit
) {

    val navigator = LocalNavigator.currentOrThrow

    var searchMode by rememberSaveable { mutableStateOf(false) }
    var query by rememberSaveable { mutableStateOf("") }
    var doSearch by rememberSaveable { mutableStateOf(false) }

    val ui: ScheduleViewModel = koinViewModel()

    val categories = remember {
        mutableStateListOf(
            AppCategoryUi(
                id = "social",
                title = "Social",
                expanded = false,
                apps = listOf(
                    AppsUi(
                        id = "1",
                        title = "Instagram",
                        iconUrl = "https://vk.com/images/community_100.png",
                        checked = false
                    ),
                    AppsUi(
                        id = "2",
                        title = "Telegram",
                        iconUrl = "https://vk.com/images/community_100.png",
                        checked = false
                    ),
                    AppsUi(
                        id = "3",
                        title = "WhatsApp",
                        iconUrl = "https://vk.com/images/community_100.png",
                        checked = false
                    )
                )
            ),
            AppCategoryUi(
                id = "productivity",
                title = "Productivity",
                expanded = false,
                apps = listOf(
                    AppsUi(
                        id = "1",
                        title = "Clash",
                        iconUrl = "https://vk.com/images/community_100.png",
                        checked = false
                    ),
                    AppsUi(
                        id = "2",
                        title = "Imperius",
                        iconUrl = "https://vk.com/images/community_100.png",
                        checked = false
                    ),
                    AppsUi(
                        id = "3",
                        title = "Legends",
                        iconUrl = "https://vk.com/images/community_100.png",
                        checked = false
                    )
                )
            )
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.extendedColor.backgroundColor)
    ) {
        CustomHeader(
            showBackButton = true,
            onBackClick = { navigator?.pop() },
            title = stringResource(Res.string.jadvallar),
            trailingIcon = {
                Image(
                    painter = painterResource(Res.drawable.search_normal),
                    contentDescription = "Search",
                    modifier = Modifier
                        .clickable {
                            searchMode = !searchMode
                            if (!searchMode) {
                                query = ""
                                doSearch = false
                            }
                        }
                )
            }
        )

        if (searchMode) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = ContainerPadding)
            ) {
                CustomTextField(
                    value = query,
                    onValueChange = {
                        query = it
                        doSearch = false
                    },
                    label = "Qidiruv",
                    hasBorder = true,
                    modifier = Modifier.height(TextFieldHeight),
                    trailingIcon = {
                        Image(
                            painter = painterResource(Res.drawable.search_normal),
                            contentDescription = "Search",
                        )
                    }
                )

                val searchResults = remember(query, categories) {
                    if (query.isBlank()) emptyList()
                    else {
                        val q = query.trim().lowercase()
                        categories.flatMap { cat ->
                            cat.apps
                                .filter { it.title.lowercase().contains(q) }
                                .map { app -> SearchHit(app, cat.id, cat.title) }
                        }
                    }
                }

                if (searchMode) {
                    if (query.isNotBlank()){
                        SpaceMedium()
                        if (searchResults.isEmpty()) {
                            CustomText(text = "Hech narsa topilmadi")
                        } else {
                            LazyColumn(
                                modifier = Modifier.fillMaxWidth(),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                items(searchResults, key = { it.app.id }) { hit ->
                                    AppRow(
                                        app = hit.app,
                                        onCheckedChange = { checked ->
                                            val categoryIdX =
                                                categories.indexOfFirst { it.id == hit.categoryId }
                                            if (categoryIdX >= 0) {
                                                val category = categories[categoryIdX]
                                                val newApps = category.apps.map { app ->
                                                    if (app.id == hit.app.id) app.copy(checked = checked) else app
                                                }
                                                categories[categoryIdX] = category.copy(apps = newApps)
                                            }
                                        }
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                CustomButton(
                    text = stringResource(Res.string.saqlash),
                    onClick = { },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = ContainerPadding)
        ) {
            SegmentedToggle(
                options = listOf(
                    stringResource(Res.string.ilovalar) to null,
                    stringResource(Res.string.veb_sayt) to null,
                ),
                selectedIndex = state.genderIndex,
                onOptionSelected = {
                    event(ScheduleEvent.OnGenderSelected(it))
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(ButtonHeight),
                fontWeight = FontWeight.W600,
                fontSize = NormalTextSize,
            )

            SpaceLarge()

            when(state.genderIndex){
                0->{
                    AppCategoryList(
                        categories = categories,
                        onToggleCategory = { categoryId ->
                            val idx = categories.indexOfFirst { it.id == categoryId }
                            if (idx >= 0) {
                                val c = categories[idx]
                                categories[idx] = c.copy(expanded = !c.expanded)
                            }
                        },
                        onToggleApp = { categoryId, appId, checked ->
                            val cIdx = categories.indexOfFirst { it.id == categoryId }
                            if (cIdx >= 0) {
                                val c = categories[cIdx]
                                val newApps = c.apps.map { a ->
                                    if (a.id == appId) a.copy(checked = checked) else a
                                }
                                categories[cIdx] = c.copy(apps = newApps)
                            }
                        }
                    )
                }

                1->{
                    navigator.push(ScheduleTimeListScreen() )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            CustomButton(
                text = stringResource(Res.string.saqlash),
                onClick = { },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Preview
@Composable
private fun Preview() {
    ScheduleUi(
        navigator = null,
        state = ScheduleState(),
        event = {}
    )
}