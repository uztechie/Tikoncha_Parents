package uz.saidburxon.newedu.presentation.feature.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.CurrentScreen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import uz.tikoncha_parent.presentation.home.HomeScreen
import uz.tikoncha_parent.presentation.map.MapScreen
import uz.tikoncha_parent.presentation.monitoring.MonitorScreen
import uz.tikoncha_parent.presentation.profile.ProfileScreen
import uz.tikoncha_parent.presentation.task.TaskScreen
import uz.tikoncha_parent.ui.*
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.*
import uz.tikoncha_parent.ui.theme.extendedColor


class MainScreen : Screen {

    @Composable
    override fun Content() {
        MainUi()
    }
}

@Composable
fun MainUi() {
    val bottomNavItems = listOf(
        BottomNavItem(
            HomeScreen(),
            painterResource(Res.drawable.home),
            stringResource(Res.string.asosiy)
        ),
        BottomNavItem(
            TaskScreen(),
            painterResource(Res.drawable.file_text),
            stringResource(Res.string.vazifa)
        ),
        BottomNavItem(
            MonitorScreen(),
            painterResource(Res.drawable.eye_check),
            stringResource(Res.string.kuzatuv)
        ),
        BottomNavItem(
            MapScreen(),
            painterResource(Res.drawable.map),
            stringResource(Res.string.xarita)
        ),
        BottomNavItem(
            ProfileScreen(),
            painterResource(Res.drawable.profil),
            stringResource(Res.string.profil)
        )
    )

    Navigator(HomeScreen()) {
        val mainNavigator = LocalNavigator.current
        Scaffold(
            containerColor = MaterialTheme.extendedColor.backgroundColor,
            bottomBar = {
                NavigationBar(
                    containerColor = MaterialTheme.extendedColor.backgroundColor,
                    modifier = Modifier
                        .border(
                            width = 1.dp,
                            color = MaterialTheme.extendedColor.borderColor,
                            shape = RoundedCornerShape(
                                topStart = MainCornerRadius,
                                topEnd = MainCornerRadius
                            )
                        )
                        .shadow(
                            elevation = 4.dp,
                            shape = RoundedCornerShape(
                                topStart = MainCornerRadius,
                                topEnd = MainCornerRadius
                            ),
                            clip = true // bu muhim: soyani chizish uchun
                        )
//                        .padding(top = 4.dp)
                        .background(MaterialTheme.extendedColor.primaryColor)
                ) {

                    bottomNavItems.forEach { item ->
                        NavigationItem(item, mainNavigator)
                    }
                }
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .padding(innerPadding)
            ) {
                CurrentScreen()
            }
        }
    }
}

@Composable
private fun RowScope.NavigationItem(item: BottomNavItem, mainNavigator: Navigator?) {
    val isSelected = mainNavigator?.lastItem?.key == item.screen.key
    println("isSelected=${isSelected}  MAINNAvigator=${mainNavigator?.lastItem?.key}  key=${item.screen.key}")
    val color = if (isSelected) MaterialTheme.extendedColor.primaryColor else MaterialTheme.extendedColor.buttonMenuColor

    NavigationBarItem(
        selected = isSelected,
        onClick = {
//            mainNavigator?.push(item.key)
            mainNavigator?.replaceAll(listOf(HomeScreen(), item.screen))
        },
        label = {
            Text(
                text = item.label,
                maxLines = 1,
                softWrap = false,
                overflow = TextOverflow.Ellipsis,
                color = color
            )
        },
        icon = {
            if (item.screen.key == ProfileScreen().key) {
                Image(
                    painter = item.icon,
                    contentDescription = item.label,
                    modifier = Modifier.size(20.dp),
                )
            } else {
                Icon(
                    painter = item.icon,
                    contentDescription = item.label,
                    modifier = Modifier.size(20.dp),
                    tint = color
                )
            }
        },
        colors = NavigationBarItemDefaults.colors(
            selectedIconColor = MaterialTheme.extendedColor.primaryColor,
            selectedTextColor = MaterialTheme.extendedColor.primaryColor,
            unselectedIconColor = MainDisableColor,
            unselectedTextColor = MainDisableColor,
            indicatorColor = Color.Transparent,
        )
    )
}
