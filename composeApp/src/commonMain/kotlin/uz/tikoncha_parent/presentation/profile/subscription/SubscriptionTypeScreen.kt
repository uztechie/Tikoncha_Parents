package uz.tikoncha_parent.presentation.profile.subscription

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import org.jetbrains.compose.resources.*
import org.jetbrains.compose.ui.tooling.preview.Preview
import tikoncha_parents.composeapp.generated.resources.*
import uz.tikoncha_parent.presentation.base.CustomHeader
import uz.tikoncha_parent.ui.ContainerPadding
import uz.tikoncha_parent.ui.theme.ThemeMode
import uz.tikoncha_parent.ui.theme.TikonchaParentTheme
import uz.tikoncha_parent.ui.theme.extendedColor


class SubscriptionTypeScreen : Screen {
    @Composable
    override fun Content() {

        val navigator = LocalNavigator.current

        SubscriptionTypeUi(
            navigator = navigator,
        )
    }
}

@Composable
fun SubscriptionTypeUi(
    navigator: Navigator?,
) {
    val sections = remember {
        mutableStateListOf(
            SubscriptionSectionItemData(
                painter = Res.drawable.telegrams_star,
                section = SubscriptionTypeSection.PREMIUM
            ),
            SubscriptionSectionItemData(
                painter = Res.drawable.telegrams_star,
                section = SubscriptionTypeSection.DONATION
            ),
            SubscriptionSectionItemData(
                painter = Res.drawable.telegrams_star,
                section = SubscriptionTypeSection.INVITING_FRIENDS
            ),
        )
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.extendedColor.backgroundColor)
    ) {
        CustomHeader(
            title = stringResource(Res.string.obuna),
            showBackButton = true,
            onBackClick = {
                navigator?.pop()
            }
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(ContainerPadding)
        ) {
            sections.forEach { data ->
                SubscriptionSectionsItem(
                    icon = painterResource(data.painter),
                    section = data.section,
                    onItemClick = { section ->
                        when (section) {
                            SubscriptionTypeSection.PREMIUM -> {}
                            SubscriptionTypeSection.DONATION -> {}
                            SubscriptionTypeSection.INVITING_FRIENDS -> {}
                        }
                    }
                )
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    TikonchaParentTheme(
        ThemeMode.DARK
    ) {
        SubscriptionTypeUi(
            navigator = null,
        )
    }
}