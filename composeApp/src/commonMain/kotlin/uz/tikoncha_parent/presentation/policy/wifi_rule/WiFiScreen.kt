package uz.tikoncha_parent.presentation.policy.wifi_rule

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.search_normal
import tikoncha_parents.composeapp.generated.resources.ulandi
import tikoncha_parents.composeapp.generated.resources.wi_fi
import tikoncha_parents.composeapp.generated.resources.wi_fi_tarmoqlarni_tanlang
import uz.tikoncha_parent.presentation.base.CustomText
import uz.tikoncha_parent.presentation.base.CustomHeader
import uz.tikoncha_parent.presentation.base.CustomTextField
import uz.tikoncha_parent.presentation.policy.common.RoundedCheckbox
import uz.tikoncha_parent.ui.ContainerPadding
import uz.tikoncha_parent.ui.HintTextColor
import uz.tikoncha_parent.ui.LargeTextSize
import uz.tikoncha_parent.ui.NormalIconButtonSize
import uz.tikoncha_parent.ui.NormalTextSize
import uz.tikoncha_parent.ui.OnPrimaryColor
import uz.tikoncha_parent.ui.PrimaryColor
import uz.tikoncha_parent.ui.ShapeCornerRadius
import uz.tikoncha_parent.ui.SpaceMedium
import uz.tikoncha_parent.ui.TextFieldHeight
import uz.tikoncha_parent.ui.theme.extendedColor



class WiFiScreen : Screen {
    @Composable
    override fun Content() {

        val navigator = LocalNavigator.current


        WiFiUi()
    }
}

@Composable
fun WiFiUi() {

    var search by remember { mutableStateOf(false) }
    var check by remember { mutableStateOf(false) }
    var title by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.extendedColor.backgroundColor)
    ) {
        CustomHeader(
            title = stringResource(Res.string.wi_fi),
            showBackButton = true,
            onBackClick = { },
            trailingIcon = {
                Icon(
                    painter = painterResource(Res.drawable.search_normal),
                    contentDescription = null,
                    modifier = Modifier.clickable{
                        search = !search
                    }
                )
            }
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = ContainerPadding)
        ) {
            CustomText(
                text = stringResource(Res.string.wi_fi_tarmoqlarni_tanlang),
                fontSize = LargeTextSize,
                fontWeight = FontWeight.SemiBold
            )



            if (search) {
                SpaceMedium()
                CustomTextField(
                    value = title,
                    onValueChange = { title = it},
                    label = "Qidiruv",
                    hasBorder = true,
                    modifier = Modifier.height(TextFieldHeight),
                )
            }



            SpaceMedium()

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(NormalIconButtonSize)
                        .clip(RoundedCornerShape(ShapeCornerRadius))
                        .background(if (check) PrimaryColor else MaterialTheme.extendedColor.tonalButtonColor),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.wi_fi),
                        contentDescription = null,
                        tint = if (check) OnPrimaryColor else HintTextColor
                    )
                }

                SpaceMedium()

                Column {
                    CustomText(
                        text = stringResource(Res.string.wi_fi),
                        fontSize = NormalTextSize,
                        fontWeight = FontWeight.SemiBold
                    )

                    if (check)
                        CustomText(
                            text = stringResource(Res.string.ulandi),
                            fontSize = NormalTextSize,
                            fontWeight = FontWeight.SemiBold,
                            color = HintTextColor
                        )
                }

                Spacer(modifier = Modifier.weight(1f))

                val checked = if (check) true else false
                RoundedCheckbox(
                    checked = checked,
                    onCheckedChange = { check = !check},
                    modifier = Modifier.size(24.dp)
                )
            }

        }
    }
}