package uz.tikoncha_parent.presentation.chat

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import dev.icerock.moko.resources.compose.painterResource
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.jetbrains.compose.resources.painterResource
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.arrow_left
import uz.tikoncha_parent.data.local.AppSettings
import uz.tikoncha_parent.platform.Logger
import uz.tikoncha_parent.platform.UniversalJsonWebView
import uz.tikoncha_parent.presentation.policy.location_rule.LocationRuleUi
import uz.tikoncha_parent.presentation.policy.policy_setup.PolicySetupScreen
import uz.tikoncha_parent.presentation.policy.shared.PolicySharedEvent
import uz.tikoncha_parent.ui.ContainerPadding
import uz.tikoncha_parent.ui.NormalIconButtonPadding
import uz.tikoncha_parent.ui.NormalIconButtonSize
import uz.tikoncha_parent.ui.NormalTextSize
import uz.tikoncha_parent.ui.theme.extendedColor
import uz.tikoncha_parent.ui.theme.rememberIsDarkTheme

class ChatMessageAiScreen(
    private val chatId: String,
    private val chatTitle: String,
): Screen {


    @Composable
    override fun Content() {


        val isDark = rememberIsDarkTheme()

        val payload = remember(isDark, chatId, chatTitle) {
            Json.encodeToString(
                ChatAiMessagePayload(
                    type = "parent",
                    token = AppSettings.accessToken,
                    chatId = chatId,
                    chatTitle = chatTitle,
                    theme = if (isDark) "dark" else "light",
                    fontSize = "14",
                    parent = true
                )
            )
        }

        val navigator = LocalNavigator.current


        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {

            UniversalJsonWebView(
                url = "https://tikoncha.uz/chat/ai/",
                json = payload,
                onIncomingJson = {},
                modifier = Modifier
                    .fillMaxSize(),
                onBackPressed = {
                    navigator?.pop()
                }
            )

            Box(
                modifier = Modifier
                    .padding(start = ContainerPadding, top = 21.dp, bottom = ContainerPadding)
            ) {
                FilledTonalIconButton(
                    modifier = Modifier
                        .size(NormalIconButtonSize),
                    onClick = {
                        navigator?.pop()
                    },
                    colors = IconButtonDefaults.filledTonalIconButtonColors(
                        containerColor = MaterialTheme.extendedColor.cardColor,
                        contentColor = MaterialTheme.extendedColor.textColor
                    ),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.arrow_left),
                        contentDescription = "",
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(NormalIconButtonPadding)
                    )
                }
            }

        }



    }


}

@Serializable
data class ChatAiMessagePayload(
    val type: String,
    val token: String,
    val chatId: String,
    val chatTitle: String,
    val theme: String,
    val fontSize: String,
    val parent: Boolean

)