package uz.tikoncha_parent.presentation.home.schedule.time

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.clock
import uz.saidburxon.newedu.presentation.base.CustomText
import uz.tikoncha_parent.ui.BorderColor
import uz.tikoncha_parent.ui.HintTextColor
import uz.tikoncha_parent.ui.NormalIconButtonSize
import uz.tikoncha_parent.ui.NormalIconSize
import uz.tikoncha_parent.ui.ShapeCornerRadius
import uz.tikoncha_parent.ui.SmallIconButtonSize
import uz.tikoncha_parent.ui.SmallIconSize
import uz.tikoncha_parent.ui.SpaceMedium
import uz.tikoncha_parent.ui.theme.extendedColor

@Composable
fun RangeRow(
    label: String,
    onClick: () -> Unit,
    onDelete: () -> Unit,
){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.extendedColor.backgroundColor)
            .border(1.dp, BorderColor, RoundedCornerShape(ShapeCornerRadius))
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable{ onClick() },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(NormalIconButtonSize)
                    .clip(RoundedCornerShape(ShapeCornerRadius))
                    .background(MaterialTheme.extendedColor.tonalButtonColor),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(Res.drawable.clock),
                    contentDescription = "",
                    modifier = Modifier
                        .fillMaxSize(0.7f)
                )
            }
            SpaceMedium()

            CustomText(text = label, modifier = Modifier.weight(1f))

            IconButton(
                onClick = onDelete
            ){
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Delete",
                    modifier = Modifier.size(NormalIconSize)
                )
            }
        }
    }
}