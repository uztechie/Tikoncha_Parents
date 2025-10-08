package uz.tikoncha_parent.presentation.task

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.add
import tikoncha_parents.composeapp.generated.resources.coin
import tikoncha_parents.composeapp.generated.resources.sizda_mavjud_tangachalar
import tikoncha_parents.composeapp.generated.resources.subtruct_icon
import tikoncha_parents.composeapp.generated.resources.tangachalar
import uz.tikoncha_parent.presentation.common.CoinGeneratorTextField
import uz.tikoncha_parent.ui.AppIconInnerPadding
import uz.tikoncha_parent.ui.BackgroundColor
import uz.tikoncha_parent.ui.BorderColor
import uz.tikoncha_parent.ui.CoinTextFieldWidth
import uz.tikoncha_parent.ui.NormalIconButtonSize
import uz.tikoncha_parent.ui.NormalLargeTextSize
import uz.tikoncha_parent.ui.PrimaryColor
import uz.tikoncha_parent.ui.SpaceSmall
import uz.tikoncha_parent.ui.TextFieldCornerRadius
import uz.tikoncha_parent.ui.theme.extendedColor
@Composable
fun CoinAmountTextField(
    coinsAmount: String,                         // tanlangan miqdor (matn ko‘rinishida)
    onAddCoinClicked: (Int) -> Unit,             // "+" bosilganda qaytariladigan yangi qiymat
    onSubtractButtonClicked: (Int) -> Unit,      // "–" bosilganda qaytariladigan yangi qiymat
    onValueChange: (String) -> Unit,             // qo‘lda kiritilganda
    maxAvailable: Int = 50                       // ✅ mavjud tangalar limiti ("50 ta")
) {
    // Ichki matn holati (kursorni oxirida ushlab turish uchun TextFieldValue ishlatyapsiz)
    var textFieldValueState by remember(coinsAmount) {
        mutableStateOf(
            TextFieldValue(
                text = coinsAmount,
                selection = TextRange(coinsAmount.length)
            )
        )
    }

    // coinsAmount tashqaridan o‘zgarsa, inputni sync qilib qo‘yamiz (kursor oxirida qolsin)
    LaunchedEffect(coinsAmount) {
        if (textFieldValueState.text != coinsAmount) {
            textFieldValueState = TextFieldValue(
                text = coinsAmount,
                selection = TextRange(coinsAmount.length)
            )
        }
    }

    Row {
        // -------------------- "–" tugma --------------------
        Box(
            modifier = Modifier
                .size(NormalIconButtonSize)
                .border(
                    width = 1.dp,
                    color = BorderColor,
                    shape = RoundedCornerShape(TextFieldCornerRadius)
                )
                .clickable {
                    val current = coinsAmount.toIntOrNull() ?: 0
                    // ✅ 0 dan pastga tushirmaymiz
                    val next = (current - 1).coerceAtLeast(0)
                    if (next != current) {
                        onSubtractButtonClicked(next)
                    }
                }
                .padding(AppIconInnerPadding)
                .background(MaterialTheme.extendedColor.backgroundColor),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(Res.drawable.subtruct_icon),
                tint = PrimaryColor,
                contentDescription = ""
            )
        }

        SpaceSmall()

        // -------------------- Raqamli input --------------------
        CoinGeneratorTextField(
            modifier = Modifier
                .width(CoinTextFieldWidth)
                .height(NormalIconButtonSize),
            singleLine = true,
            value = textFieldValueState,
            hasBorder = true,
            fonSize = NormalLargeTextSize,
            contentColor = PrimaryColor,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            isCenteredText = true,
            onValueChange = { newTFV ->
                val raw = newTFV.text

                // Faqat raqamlarni qoldiramiz
                val digitsOnly = raw.filter { it.isDigit() }

                // Bo‘sh bo‘lsa 0 deb olaylik
                val parsed = digitsOnly.toIntOrNull() ?: 0

                // ✅ 0…maxAvailable oralig‘iga majburlaymiz
                val coerced = parsed.coerceIn(0, maxAvailable)

                // TextFieldValue’ni yangilaymiz (kursor oxirida qolsin)
                val coercedText = coerced.toString()
                textFieldValueState = newTFV.copy(
                    text = coercedText,
                    selection = TextRange(coercedText.length)
                )

                // Tashqi state’ga ham xabar beramiz
                onValueChange(coercedText)
            }
        )

        SpaceSmall()

        // -------------------- "+" tugma --------------------
        Box(
            modifier = Modifier
                .size(NormalIconButtonSize)
                .clip(RoundedCornerShape(TextFieldCornerRadius))
                .clickable {
                    val current = coinsAmount.toIntOrNull() ?: 0
                    // ✅ maxAvailable dan oshirmaymiz
                    val next = (current + 1).coerceAtMost(maxAvailable)
                    if (next != current) {
                        onAddCoinClicked(next)
                    }
                }
                .background(PrimaryColor)
                .padding(AppIconInnerPadding),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(Res.drawable.add),
                tint = BackgroundColor,
                contentDescription = ""
            )
        }
    }
}
