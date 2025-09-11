package uz.tikoncha_parent.presentation.base

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import uz.tikoncha_parent.ui.PrimaryColor
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.tikoncha_logo
import uz.tikoncha_parent.ui.theme.extendedColor

@Composable
fun LogoHeader(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 25.dp)
    ) {
        Icon(
            painter = painterResource(Res.drawable.tikoncha_logo),
            contentDescription = null,
            tint = MaterialTheme.extendedColor.primaryColor,
            modifier = Modifier.weight(1f)
        )

        Spacer(Modifier.weight(1f))
    }
}

@Preview
@Composable
private fun Pre() {
    LogoHeader()
}