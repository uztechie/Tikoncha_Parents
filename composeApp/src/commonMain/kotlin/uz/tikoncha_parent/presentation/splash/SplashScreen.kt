package uz.tikoncha_parent.presentation.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import kotlinx.coroutines.delay
import uz.tikoncha_parent.data.local.AppSettings
import uz.tikoncha_parent.presentation.login.LoginScreen
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.tikoncha_logo
import uz.tikoncha_parent.platform.Logger
import uz.tikoncha_parent.presentation.new_home.NewHomeScreen
import uz.tikoncha_parent.ui.theme.AppColors
import uz.tikoncha_parent.ui.theme.TikonchaParentTheme
import uz.tikoncha_parent.ui.theme.extendedColor

class SplashScreen : Screen {

    @Composable
    override fun Content() {


        val navigator = LocalNavigator.current

        LaunchedEffect(true) {

            println("Launcher screem")



            delay(1000) // 1 sekund
            if (AppSettings.hasUserLogin) {
                Logger.d("SplashScreen", "hasUserLogin")
                navigator?.replaceAll(NewHomeScreen())
            } else {
//                if (AppSettings.isFirstLaunch){
//                    navigator?.replaceAll(SliderScreen())
//                }
//                else{
//                    navigator?.replaceAll(LoginScreen())
//                }
                navigator?.replaceAll(LoginScreen())
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(AppColors.bg.page),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                painter = painterResource(Res.drawable.tikoncha_logo),
                contentDescription = null,
                tint = MaterialTheme.extendedColor.primaryColor,
                modifier = Modifier.size(200.dp)
            )
        }
    }
}

@Preview
@Composable
fun Preview() {
    TikonchaParentTheme() {
        SplashScreen().Content()
    }
}