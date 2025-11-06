@file:Suppress("EQUALITY_NOT_APPLICABLE_WARNING")

package uz.tikoncha_parent.presentation.policy.app_selection

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
import tikoncha_parents.composeapp.generated.resources.jadval
import tikoncha_parents.composeapp.generated.resources.saqlash
import tikoncha_parents.composeapp.generated.resources.search_normal
import tikoncha_parents.composeapp.generated.resources.veb_sayt
import uz.saidburxon.newedu.presentation.base.CustomButton
import uz.tikoncha_parent.presentation.base.CustomHeader
import uz.tikoncha_parent.presentation.base.SegmentedToggle
import uz.tikoncha_parent.ui.ButtonHeight
import uz.tikoncha_parent.ui.ContainerPadding
import uz.tikoncha_parent.ui.NormalTextSize
import uz.tikoncha_parent.ui.SpaceLarge
import uz.tikoncha_parent.ui.theme.extendedColor

class AppWebSelectionScreen : Screen {
    @Composable
    override fun Content() {

        val navigator = LocalNavigator.current

        val viewModel = koinViewModel<AppWebViewModel>()
        val state = viewModel.state.collectAsStateWithLifecycle()
        val event = viewModel::onEvent

        ScheduleAppsSelectUi(
            navigator = navigator,
            state = state.value,
            event = event
        )
    }
}

@Composable
fun ScheduleAppsSelectUi(
    navigator: Navigator?,
    state: AppWebState,
    event: (AppWebEvent) -> Unit
) {

    val navigator = LocalNavigator.currentOrThrow


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.extendedColor.backgroundColor)
    ) {
        CustomHeader(
            showBackButton = true,
            onBackClick = { navigator?.pop() },
            title = stringResource(Res.string.jadval),
            trailingIcon = {
                Image(
                    painter = painterResource(Res.drawable.search_normal),
                    contentDescription = "Search",
                    modifier = Modifier
                        .clickable {

                        }
                )
            }
        )

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
                selectedIndex = state.appWebSelectionIndex,
                onOptionSelected = {
                    event(AppWebEvent.OnAppWebSelected(it))
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(ButtonHeight),
                fontWeight = FontWeight.W600,
                fontSize = NormalTextSize,
            )

            SpaceLarge()

            when(state.appWebSelectionIndex){
                0->{
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(state.categories, key = {it.id}) { category ->
                            AppCategoryItem(
                                category = category,
                                onHeaderClick = {
                                    event(AppWebEvent.ExpandCategory(category))
                                },
                                onToggleApp = { app, checked ->
                                    event(AppWebEvent.ToggleApp(app, checked))
                                },
                                onToggleAll = { checked ->
                                    event(AppWebEvent.ToggleCategory(category, checked))
                                }
                            )
                        }
                    }
                }

                1->{
//                    navigator.push(ScheduleTimeListScreen() )
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
    ScheduleAppsSelectUi(
        navigator = null,
        state = AppWebState(),
        event = {}
    )
}