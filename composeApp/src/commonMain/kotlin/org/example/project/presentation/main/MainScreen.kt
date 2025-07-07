package uz.saidburxon.newedu.presentation.feature.main

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.CurrentScreen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import org.example.project.presentation.base.theme.*
import org.example.project.presentation.home.HomeScreen
import org.example.project.presentation.map.MapScreen
import org.example.project.presentation.monitoring.MonitorScreen
import org.example.project.presentation.profile.ProfileScreen
import org.example.project.presentation.task.TaskScreen
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.*


class MainScreen :Screen {

    @Composable
    override fun Content() {
        MainUi()
    }
}
@Composable
fun MainUi() {
    val bottomNavItems = listOf(
        BottomNavItem(HomeScreen(), painterResource(Res.drawable.home), stringResource(Res.string.asosiy)),
        BottomNavItem(TaskScreen(), painterResource(Res.drawable.file_text), stringResource(Res.string.vazifa)),
        BottomNavItem(MonitorScreen(), painterResource(Res.drawable.eye_check), stringResource(Res.string.kuzatuv)),
        BottomNavItem(MapScreen(), painterResource(Res.drawable.map), stringResource(Res.string.xarita)),
        BottomNavItem(ProfileScreen(), painterResource(Res.drawable.profil), stringResource(Res.string.profil))
    )

    Navigator(HomeScreen()){
        val mainNavigator = LocalNavigator.current
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

                    bottomNavItems.forEach { item ->
                        NavigationItem(item, mainNavigator)
                    }
                }
            }
        ) { innerPadding ->
            CurrentScreen()
        }
    }

}

@Composable
private fun RowScope.NavigationItem(item: BottomNavItem, mainNavigator: Navigator?){
    val isSelected = mainNavigator?.lastItem?.key == item.screen.key
    println("isSelected=${isSelected}  MAINNAvigator=${mainNavigator?.lastItem?.key}  key=${item.screen.key}")
    val color = if (isSelected) PrimaryColor else TextColor
    NavigationBarItem(
        selected = isSelected,
        onClick = {
//            mainNavigator?.push(item.key)
            mainNavigator?.replaceAll(listOf(HomeScreen(), item.screen ))
        },
        label = { Text(text = item.label) },
        icon = {
            Icon(
                painter = item.icon,
                contentDescription = item.label,
                modifier = Modifier.size(20.dp),
                tint = color
            )
        }
    )
}
