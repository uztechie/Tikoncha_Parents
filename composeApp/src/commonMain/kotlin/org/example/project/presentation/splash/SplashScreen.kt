package org.example.project.presentation.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import kotlinx.coroutines.delay
import org.example.project.presentation.base.theme.BackgroundColor
import org.example.project.presentation.base.theme.PrimaryColor
import org.example.project.presentation.slider.SliderScreen
import org.jetbrains.compose.ui.tooling.preview.Preview

class SplashScreen : Screen {

    @Composable
    override fun Content() {

        val navigator = LocalNavigator.current

        LaunchedEffect(true) {
            delay(1000) // 1 sekund
            navigator?.push(SliderScreen())
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(BackgroundColor),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(color = Color.Black)) {
                        append("New ")
                    }
                    withStyle(style = SpanStyle(color = PrimaryColor)) {
                        append("edu")
                    }
                },
                fontSize = 40.sp,
                fontStyle = FontStyle.Italic,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Preview
@Composable
fun Preview(){
    SplashScreen().Content()
}