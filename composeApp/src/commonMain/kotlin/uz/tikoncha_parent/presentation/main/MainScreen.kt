package uz.tikoncha_parent.presentation.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.CurrentScreen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import uz.tikoncha_parent.presentation.statistic.StatisticScreen
import uz.tikoncha_parent.presentation.map.MapScreen
import uz.tikoncha_parent.presentation.profile.ProfileScreen
import uz.tikoncha_parent.presentation.task.TaskScreen
import uz.tikoncha_parent.ui.*
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.*
import uz.saidburxon.newedu.presentation.feature.main.BottomNavItem
import uz.tikoncha_parent.presentation.base.bottomShadow
import uz.tikoncha_parent.presentation.base.topShadow
import uz.tikoncha_parent.presentation.chat.ChatScreen
import uz.tikoncha_parent.ui.theme.extendedColor


class MainScreen : Screen {

    @Composable
    override fun Content() {
        MainUi()
    }
}

@Composable
fun MainUi() {

    val density = LocalDensity.current
    val imeBottomPx = WindowInsets.ime.getBottom(density)
    val imeVisible = imeBottomPx > 0

    val bottomRoundedShape = RoundedCornerShape(
        topStart = 0.dp,
        topEnd = 0.dp,
        bottomStart = ShapeCornerRadius,
        bottomEnd = ShapeCornerRadius
    )

    val bottomNavItems = listOf(
        BottomNavItem(
            StatisticScreen(),
            painterResource(Res.drawable.home_only_borders),
            stringResource(Res.string.asosiy)
        ),
        BottomNavItem(
            TaskScreen(),
            painterResource(Res.drawable.file_text),
            stringResource(Res.string.vazifa)
        ),
//        BottomNavItem(
//            MonitorScreen(),
//            painterResource(Res.drawable.eye_check),
//            stringResource(Res.string.kuzatuv)
//        ),
        BottomNavItem(
            ChatScreen(),
            painterResource(Res.drawable.dialog_main),
            stringResource(Res.string.suhbat)
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

    Navigator(StatisticScreen()) {
        val mainNavigator = LocalNavigator.current
        Scaffold(
            modifier = Modifier
                .topShadow(
                    shape = RoundedCornerShape(ShapeCornerRadius),
                    color = MaterialTheme.extendedColor.backgroundColor
                )
                .background(
                    color = MaterialTheme.extendedColor.backgroundColor,
                    shape = bottomRoundedShape
                ),
            containerColor = MaterialTheme.extendedColor.backgroundColor,
            contentWindowInsets = WindowInsets(0),
            bottomBar = {
                if (imeVisible){
                    return@Scaffold
                }
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .bottomShadow(
                            shape = RoundedCornerShape(
                                topStart = ButtonCornerRadius,
                                topEnd = ButtonCornerRadius
                            ),
                            color = MaterialTheme.extendedColor.backgroundColor
                        )
                        .bottomShadow(
                            shape = RoundedCornerShape(
                                topStart = ButtonCornerRadius,
                                topEnd = ButtonCornerRadius
                            ),
                            color = MaterialTheme.extendedColor.backgroundColor,
                            lowerOffset = -5.dp,
                            radius = 10.dp

                        )
                        .background(MaterialTheme.extendedColor.backgroundColor)
                )
                {
                NavigationBar(
                    containerColor = MaterialTheme.extendedColor.backgroundColor,
                    modifier = Modifier
                        .border(
                            width = 1.dp,
                            color = MaterialTheme.extendedColor.tonalButtonColor,
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
            mainNavigator?.replaceAll(listOf(StatisticScreen(), item.screen))
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
