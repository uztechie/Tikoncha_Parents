package uz.tikoncha_parent.presentation.base

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.close
import tikoncha_parents.composeapp.generated.resources.close_circle
import uz.tikoncha_parent.ui.NormalIconButtonSize
import uz.tikoncha_parent.ui.theme.extendedColor

@Composable
fun CloseButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
){
    FilledTonalIconButton(
        modifier = modifier
            .size(NormalIconButtonSize),
        onClick = onClick,
        colors = IconButtonDefaults.filledTonalIconButtonColors(
            containerColor = MaterialTheme.extendedColor.cardColor,
            contentColor = MaterialTheme.extendedColor.onBackgroundColor
        ),
        shape = RoundedCornerShape(10.dp)
    ) {
        Icon(
            painter = painterResource(Res.drawable.close_circle),
            contentDescription = "Delete",
            modifier = Modifier

        )
    }
}