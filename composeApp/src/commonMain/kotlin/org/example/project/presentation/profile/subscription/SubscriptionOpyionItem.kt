package org.example.project.presentation.profile.subscription

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import org.example.project.ui.DividerHorizontal
import org.example.project.ui.NormalIconButtonPadding
import org.example.project.ui.NormalTextSize
import org.example.project.ui.NormalTextSizeSp
import org.example.project.ui.PrimaryColor
import org.example.project.ui.SpaceMedium
import org.jetbrains.compose.resources.painterResource
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.checked

@Composable
fun SubscriptionOptionItem(
    title: String,
    priceUsd: String,
    isSelected: Boolean,
    fontSize: TextUnit = NormalTextSizeSp,
    fontWeight: FontWeight = FontWeight.Normal,
    onCheckedChange: (Boolean) -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable{ onCheckedChange(true) },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            CustomCheckBox(
                checked = isSelected,
                onCheckedChange = onCheckedChange
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = title,
                fontSize = fontSize,
                fontWeight = fontWeight
            )
        }

        Text(
            text = priceUsd,
            fontSize = fontSize,
            fontWeight = fontWeight
        )
    }

    SpaceMedium()

    DividerHorizontal()
}

@Composable
fun CustomCheckBox(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    borderColor: Color = PrimaryColor,
    checkColor: Color = PrimaryColor
) = Box(
    modifier = Modifier
        .size(24.dp)
        .border(1.dp, borderColor, RoundedCornerShape(NormalIconButtonPadding))
        .clickable { onCheckedChange(!checked) },
    contentAlignment = Alignment.Center
) {
    if (checked) {
        Icon(
            painter = painterResource(Res.drawable.checked),
            contentDescription = "Checked",
            tint = checkColor,
        )
    }
}