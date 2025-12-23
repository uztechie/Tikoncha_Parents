package uz.tikoncha_parent.presentation.policy.policy_setup

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import uz.tikoncha_parent.presentation.base.CustomText
import uz.tikoncha_parent.presentation.base.CloseButton
import uz.tikoncha_parent.ui.*
import uz.tikoncha_parent.ui.theme.extendedColor


@Composable
fun PolicySetupRuleItem(
    modifier: Modifier = Modifier,
    title: String,
    subTitle: String,
    canRemove: Boolean = false,
    onRemoveClick: () -> Unit,
    onItemClick: () -> Unit
) {


    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(
                MaterialTheme.extendedColor.cardColor,
                RoundedCornerShape(CardCornerRadius)
            )
            .padding(horizontal = ContainerPadding, vertical = 12.dp)
            .clickable(
                indication = null,
                interactionSource = null,
                onClick = onItemClick
            ),
    ) {
        Column(
            modifier = Modifier
        ) {
            CustomText(
                text = title,
                fontSize = LargeTextSize,
                fontWeight = FontWeight.SemiBold
            )
            SpaceSmall()
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                CustomText(
                    text = subTitle,
                    fontSize = NormalTextSize,
                    color = MaterialTheme.extendedColor.hintColor,
                )
            }
        }

        Spacer(Modifier.weight(1f))
        if (canRemove){
            SpaceSmall()
            CloseButton {
                onRemoveClick()
            }
        }
    }
}