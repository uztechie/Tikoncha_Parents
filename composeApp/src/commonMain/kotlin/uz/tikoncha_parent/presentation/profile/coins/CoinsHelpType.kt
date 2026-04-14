package uz.tikoncha_parent.presentation.profile.coins

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringArrayResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.coins_help_close
import tikoncha_parents.composeapp.generated.resources.coins_help_earn_bullets
import tikoncha_parents.composeapp.generated.resources.coins_help_earn_intro
import tikoncha_parents.composeapp.generated.resources.coins_help_earn_note
import tikoncha_parents.composeapp.generated.resources.coins_help_earn_title
import tikoncha_parents.composeapp.generated.resources.coins_help_use_bullets
import tikoncha_parents.composeapp.generated.resources.coins_help_use_intro
import tikoncha_parents.composeapp.generated.resources.coins_help_use_note
import tikoncha_parents.composeapp.generated.resources.coins_help_use_title
import uz.tikoncha_parent.presentation.base.CustomText
import uz.tikoncha_parent.ui.CardCornerRadius
import uz.tikoncha_parent.ui.SpaceMedium
import uz.tikoncha_parent.ui.SpaceSmall
import uz.tikoncha_parent.ui.theme.extendedColor

enum class CoinsHelpType { USE, EARN }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CoinsHelpBottomSheet(
    type: CoinsHelpType,
    onDismiss: () -> Unit,
) {
    val titleRes = if (type == CoinsHelpType.USE) stringResource(Res.string.coins_help_use_title) else stringResource(Res.string.coins_help_earn_title)
    val introRes = if (type == CoinsHelpType.USE) stringResource(Res.string.coins_help_use_intro) else stringResource(Res.string.coins_help_earn_intro)
    val bulletsRes = if (type == CoinsHelpType.USE) stringArrayResource(Res.array.coins_help_use_bullets) else stringArrayResource(Res.array.coins_help_earn_bullets)
    val noteRes = if (type == CoinsHelpType.USE) stringResource(Res.string.coins_help_use_note) else stringResource(Res.string.coins_help_earn_note)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = MaterialTheme.extendedColor.cardColor,
        shape = RoundedCornerShape(CardCornerRadius)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(bottom = 16.dp)
        ) {
            CustomText(
                text = titleRes,
                fontWeight = FontWeight.W700,
            )
            SpaceSmall()

            CustomText(text = introRes)
            SpaceMedium()

            val bullets = bulletsRes.toList()
            bullets.forEach { item ->
                BulletRow(text = item)
            }
            
            SpaceMedium()
            CustomText(text = noteRes)
            SpaceMedium()

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.weight(1f))

                CustomText(
                    text = stringResource(Res.string.coins_help_close),
                    fontWeight = FontWeight.W700,
                    modifier = Modifier.padding(vertical = 8.dp).clickable{onDismiss()}
                )
            }
        }
    }
}

@Composable
private fun BulletRow(text: String) {
    Row(
        modifier = Modifier.padding(vertical = 6.dp),
        verticalAlignment = Alignment.Top
    ) {
        CustomText(text = "• ")
        CustomText(text = text)
    }
}

@Preview
@Composable
private fun Preview() {
    CoinsHelpBottomSheet(
        type = CoinsHelpType.USE,
        onDismiss = {},
    )
}
