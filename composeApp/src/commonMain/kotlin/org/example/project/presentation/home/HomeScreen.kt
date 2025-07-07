package org.example.project.presentation.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import org.example.project.presentation.settings.SettingScreen
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.UZ
import tikoncha_parents.composeapp.generated.resources.app_namee


class HomeScreen: Screen {


    @Composable
    override fun Content() {
        val viewModel = koinViewModel<HomeViewModel>()
        val state = viewModel.state.collectAsStateWithLifecycle()
        val event = viewModel::onEvent

        val navigator = LocalNavigator.current
        Home(
            navigator = navigator,
            state = state.value,
            event = event
        )
    }

}





@Composable
private fun Home(
    navigator: Navigator?,
    state: HomeState,
    event: (HomeEvent) -> Unit,
){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = {
//                    navigator?.push(SettingScreen())
                event(HomeEvent.Count)

            }
        ) {
            Text(
                text = stringResource(Res.string.app_namee)
            )
        }

        Image(
            painter = painterResource(Res.drawable.UZ),
            contentDescription = "",
            modifier = Modifier
                .size(100.dp)
        )

        LazyColumn {
            items(state.list){
                Text(
                    text = stringResource(Res.string.app_namee)
                )
            }
        }
    }
}

@Preview
@Composable
fun Pre(){
    Home(
        navigator = null,
        state = HomeState(),
        event = {}
    )
}

