package org.example.project.presentation.splash

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
import com.russhwolf.settings.Settings
import kotlinx.coroutines.delay
import org.example.project.data.local.AppSettings
import org.example.project.presentation.login.LoginScreen
import org.example.project.ui.PrimaryColor
import org.example.project.presentation.slider.SliderScreen
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.tikoncha_logo
import uz.saidburxon.newedu.presentation.feature.main.MainScreen

class SplashScreen : Screen {

    @Composable
    override fun Content() {


        val navigator = LocalNavigator.current

        LaunchedEffect(true) {


            delay(1000) // 1 sekund
            if (AppSettings.hasUserLogin){
                navigator?.replaceAll(MainScreen())
            }else{
                if (AppSettings.isFirstLaunch){
                    navigator?.replaceAll(SliderScreen())
                }
                else{
                    navigator?.replaceAll(LoginScreen())
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                painter = painterResource(Res.drawable.tikoncha_logo),
                contentDescription = null,
                tint = PrimaryColor,
                modifier = Modifier.size(200.dp)
            )
        }
    }
}

@Preview
@Composable
fun Preview(){
    SplashScreen().Content()
}