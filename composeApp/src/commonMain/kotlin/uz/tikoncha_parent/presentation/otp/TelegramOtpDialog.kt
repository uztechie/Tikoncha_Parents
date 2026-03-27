package uz.tikoncha_parent.presentation.otp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.material3.SheetValue
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.*
import uz.saidburxon.newedu.presentation.base.CustomButton
import uz.tikoncha_parent.presentation.base.CustomText
import uz.tikoncha_parent.ui.*
import uz.tikoncha_parent.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelegramOtpDialog(
    show: Boolean,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    if (show){

        val sheetState = rememberModalBottomSheetState(
            skipPartiallyExpanded = true,
            confirmValueChange = { it != SheetValue.Hidden }
        )

        ModalBottomSheet(
            sheetState = sheetState,
            onDismissRequest = {},
            containerColor = Color.Transparent,
            dragHandle = {},
            properties = ModalBottomSheetProperties(
                shouldDismissOnBackPress = false,
                shouldDismissOnClickOutside = false
            ),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(
                        start = ContainerPadding,
                        end = ContainerPadding
                    )
            ) {
                Card(
                    shape = RoundedCornerShape(CardCornerRadius),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.extendedColor.cardColor
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(ContainerPadding),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .padding(bottom = 15.dp)
                                .background( color = Color(0XFF9A9A9A), shape = CircleShape)
                                .height(3.dp)
                                .width(36.dp),
                        )

                        CustomText(
                            text = stringResource(Res.string.kod_telegramga_yuboriladi),
                        )
                        SpaceMedium()

                        CustomText(
                            text = stringResource(Res.string.tasdiqlash_kodi_telegram),
                            modifier = Modifier
                                .fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                        SpaceLarge()

                        CustomButton(
                            text = stringResource(Res.string.telegramga_otish),
                            textColor = OnPrimaryColor,
                            onClick = {
                                onConfirm()
                            },
                            modifier = Modifier
                                .fillMaxWidth(),
                            color = TelegramButtonColor,
                            leadingIcon = {
                                Icon(
                                    painter = painterResource(Res.drawable.telegram),
                                    contentDescription = null,
                                    tint = OnPrimaryColor
                                )
                            }
                        )
                        SpaceSmall()

                        CustomButton(
                            text = stringResource(Res.string.raqamda_telegram_mavjud_emas),
                            onClick = onDismiss,
                            color = MaterialTheme.extendedColor.disabledContentColor,
                            textColor = MaterialTheme.extendedColor.textColor,
                            modifier = Modifier
                                .fillMaxWidth()
                        )
                    }
                }
                SpaceLarge()
            }
        }
    }
}

@Preview(
    showBackground = true,
)
@Composable
private fun InAppUpdateDialogPreview() {
    TikonchaParentTheme(
        ThemeMode.DARK
    ) {
        TelegramOtpDialog(
            show = true,
            onDismiss = {},
            onConfirm = {},
        )
    }
}