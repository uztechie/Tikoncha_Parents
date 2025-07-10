package org.example.project.presentation.profile.subscription

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import org.example.project.presentation.common.CustomButton
import org.example.project.presentation.common.CustomListDialog
import org.example.project.presentation.common.CustomOutlinedButton
import org.example.project.presentation.common.CustomSelectionButton
import org.example.project.presentation.common.CustomText
import org.example.project.presentation.domain.model.Subscription
import org.example.project.presentation.home.HomeEvent
import org.example.project.presentation.profile.CustomHeader
import org.example.project.presentation.profile.ProfileScreen
import org.example.project.ui.BackgroundColor
import org.example.project.ui.ButtonHeight
import org.example.project.ui.CardColors
import org.example.project.ui.ContainerCornerRadius
import org.example.project.ui.ContainerPadding
import org.example.project.ui.DialogButtonHeight
import org.example.project.ui.HintTextColor
import org.example.project.ui.LargeTextSize
import org.example.project.ui.NormalLargeTextSize
import org.example.project.ui.NormalTextSize
import org.example.project.ui.PrimaryColor
import org.example.project.ui.SmallTextSize
import org.example.project.ui.SpaceMedium
import org.example.project.ui.SpaceSmall
import org.example.project.ui.SpaceUltraSmall
import org.example.project.ui.TextColor
import org.example.project.ui.TextFieldHeight
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.profile

class SubscriptionScreen : Screen {
    @Composable
    override fun Content() {

        val navigator = LocalNavigator.current

        SubscriptionUi(
            navigator = navigator
        )
    }
}

@Composable
fun SubscriptionUi(
    navigator: Navigator?
) {

    val childrenList = remember {
        mutableStateListOf(
            "Saidburxon",
            "Muhammadsaid",
            "Muhammadyusuf",
            "Beka"
        )
    }

    val subscriptions = remember {
        mutableStateMapOf(
            0 to Subscription(
                title = "Yillik",
                price = "150 000",
                isSelected = false
            ),
            1 to Subscription(
                title = "Oylik",
                price = "16 000",
                isSelected = false
            )
        )
    }

    val selectedChildren = remember {
        mutableStateOf(childrenList[0])
    }

    var showDialog by remember {
        mutableStateOf(false)
    }

    CustomListDialog(
        title = "Farzandlaringiz",
        items = childrenList,
        show = showDialog,
        onItemSelected = { child ->
            selectedChildren.value = child
        },
        onDismiss = {
            showDialog = false
        }
    )


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor)
    ) {

        CustomHeader(
            title = "Obuna",
            showBackButton = true,
            onBackClick = {
                navigator!!.pop()
            }
        )

        SpaceMedium()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = ContainerPadding)
                .verticalScroll(rememberScrollState())
        ) {

            CustomSelectionButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(TextFieldHeight),
                text = selectedChildren.value,
                painter = painterResource(Res.drawable.profile),
                onClick = {
                    showDialog = true
                }
            )

            SpaceMedium()

            Card(
                modifier = Modifier
                    .fillMaxWidth(),
                shape = RoundedCornerShape(ContainerCornerRadius),
                colors = CardDefaults.cardColors(
                    containerColor = CardColors
                )
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {

                    Row {

                        CustomText(
                            text = "Xozr sizning obunangiz ",
                            fontSize = NormalLargeTextSize,
                            fontWeight = FontWeight.SemiBold,
                            color = TextColor
                        )

                        CustomText(
                            text = "Standart",
                            fontSize = NormalLargeTextSize,
                            fontWeight = FontWeight.SemiBold,
                            color = PrimaryColor
                        )
                    }

                    SpaceUltraSmall()

                    CustomText(
                        text = "Ilovaning barcha funksiyalaridan foydalanish uchun Pro versiyasini yuklab oling",
                        fontSize = NormalTextSize,
                        color = HintTextColor,
                        style = TextStyle()
                    )
                }
            }

            SpaceMedium()

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(15.dp)
            ) {

                Column(
                    modifier = Modifier
                        .weight(1.8f)
                ) {

                    CustomText(
                        text = "PRO",
                        color = PrimaryColor,
                        fontSize = LargeTextSize,
                        fontWeight = FontWeight.SemiBold
                    )

                    CustomText(
                        color = HintTextColor,
                        text = "Premium ibuna bilan ko'proq erkinlik va eksklyuziv xususiyatlar",
                        fontSize = NormalTextSize,
                        style = TextStyle()
                    )

                }

                CustomOutlinedButton(
                    text = "Tangachalar",
                    onClick = {

                    },
                    textColor = PrimaryColor,
                    modifier = Modifier
                        .weight(1f)
                        .height(DialogButtonHeight)
                )

            }

            SpaceMedium()

            subscriptions.forEach { (index, subscription) ->

                SpaceMedium()

                SubscriptionOptionItem(
                    title = subscription.title,
                    priceUsd = subscription.price,
                    isSelected = subscription.isSelected,
                    onCheckedChange = { selected ->
                        subscriptions.forEach { (i, sub) ->
                            subscriptions[i] = sub.copy(isSelected = i == index)
                        }
                    }
                )

            }

            SpaceMedium()

            Card(
                modifier = Modifier
                    .fillMaxWidth(),
                shape = RoundedCornerShape(ContainerCornerRadius),
                colors = CardDefaults.cardColors(
                    containerColor = CardColors
                )
            ) {

                SubscriptionItem(
                    selectedSubscription = SubscriptionType.COIN,
                    onSubscriptionSelected = {}
                )
            }

            SpaceMedium()

            CustomButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(ButtonHeight),
                text = "Sotib olish",
                enabled = true,
                fontSize = NormalLargeTextSize,
                onClick = {

                }
            )

        }

    }
}

@Preview
@Composable
fun PreviewSubscriptionScreen() {
    SubscriptionUi(
        navigator = null
    )
}