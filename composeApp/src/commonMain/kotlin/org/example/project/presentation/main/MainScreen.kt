package uz.saidburxon.newedu.presentation.feature.main

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.*
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import org.example.project.presentation.base.theme.*
import org.example.project.presentation.task.TaskScreen
import org.example.project.presentation.task.TaskViewModel
import uz.saidburxon.newedu.presentation.navigation.ScreenMain
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.*
import uz.saidburxon.newedu.presentation.feature.home.MainHomeScreen


class MainScreen :Screen {

    @Composable
    override fun Content() {

        val navigator = LocalNavigator.current

        Main(
            navigator = navigator
        )
    }
}
@Composable
fun Main(
    navigator: Navigator?
) {
    val mainNavController = rememberNavController()

    val bottomNavItems = listOf(
        BottomNavItem(ScreenMain.Home, painterResource(Res.drawable.home), stringResource(Res.string.asosiy)),
        BottomNavItem(ScreenMain.Task, painterResource(Res.drawable.file_text), stringResource(Res.string.vazifa)),
        BottomNavItem(ScreenMain.Conversation, painterResource(Res.drawable.eye_check), stringResource(Res.string.kuzatuv)),
        BottomNavItem(ScreenMain.Conversation, painterResource(Res.drawable.map), stringResource(Res.string.xarita)),
        BottomNavItem(ScreenMain.Profile, painterResource(Res.drawable.profil), stringResource(Res.string.profil))
    )

    Scaffold(
        containerColor = BackgroundColor,
        bottomBar = {
            NavigationBar(
                containerColor = OnPrimaryColor,
                modifier = Modifier
                    .clip(RoundedCornerShape(topStart = MainCornerRadius, topEnd = MainCornerRadius))
                    .border(1.dp, MainBorderColor, RoundedCornerShape(topStart = MainCornerRadius, topEnd = MainCornerRadius))
                    .background(PrimaryColor)
            ) {
                val navBackStackEntry = mainNavController.currentBackStackEntryAsState().value
                val currentRoute = navBackStackEntry?.destination?.route

                bottomNavItems.forEach { item ->
                    val isSelected = currentRoute == item.route.route
                    val color = if (isSelected) PrimaryColor else TextColor

                    NavigationBarItem(
                        selected = isSelected,
                        onClick = {
                            if (currentRoute != item.route.route) {
                                mainNavController.navigate(item.route.route) {
                                    popUpTo(ScreenMain.Home.route)
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        },
                        icon = {
                            Icon(
                                painter = item.icon,
                                contentDescription = item.label,
                                modifier = Modifier.size(20.dp),
                                tint = color
                            )
                        },
                        label = {
                            Text(
                                text = item.label,
                                fontSize = 10.sp,
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = PrimaryColor,
                            selectedTextColor = PrimaryColor,
                            unselectedIconColor = MainDisableColor,
                            unselectedTextColor = MainDisableColor,
                            indicatorColor = Color.Transparent
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            modifier = Modifier.padding(innerPadding),
            navController = mainNavController,
            startDestination = ScreenMain.Home.route
        ) {
            composable(ScreenMain.Home.route) {
                val viewModel = remember { TaskViewModel() }
                val state by viewModel.state.collectAsState()

                MainHomeScreen(
                    state = state,
                    event = viewModel::onEvent
                )

            }

            composable(ScreenMain.Task.route) {

                val viewModel = remember { TaskViewModel() }
                val state by viewModel.state.collectAsState()
                val event = viewModel::onEvent

                TaskScreen(
                    state = state,
                    event = event,
                    navigator = navigator
                )
            }
//
//            composable(ScreenMain.Conversation.route) {
//                ChatScreen()
//            }
//
//            composable(ScreenMain.Profile.route) {
//                ProfileScreen()
//            }
        }
    }
}
