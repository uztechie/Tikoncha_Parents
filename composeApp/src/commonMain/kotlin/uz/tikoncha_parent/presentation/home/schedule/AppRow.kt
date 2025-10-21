package uz.tikoncha_parent.presentation.home.schedule

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.coin
import tikoncha_parents.composeapp.generated.resources.compose_multiplatform
import tikoncha_parents.composeapp.generated.resources.ic_launcher_foreground
import uz.saidburxon.newedu.presentation.base.CustomText
import uz.tikoncha_parent.data.mapper.AppsUi
import uz.tikoncha_parent.ui.CardColors
import uz.tikoncha_parent.ui.CardCornerRadius
import uz.tikoncha_parent.ui.ContainerPadding
import uz.tikoncha_parent.ui.LargeIconSize
import uz.tikoncha_parent.ui.NormalIconSize
import uz.tikoncha_parent.ui.SpaceMedium

@Composable
fun AppRow(
    app: AppsUi,
    onCheckedChange: (Boolean) -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 26.dp, end = 0.dp, top = 8.dp, bottom = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(LargeIconSize)
                .clip(RoundedCornerShape(6.dp))
                .background(app.iconBg),
            contentAlignment = Alignment.Center
        ) {
            if (!app.iconUrl?.isNullOrBlank()!!) {
                AsyncImage(
                    modifier = Modifier
                        .size(LargeIconSize)
                        .clip(RoundedCornerShape(6.dp)),
                    model = app.iconUrl,
                    contentDescription = null,
                    placeholder = painterResource(Res.drawable.ic_launcher_foreground),
                    error = painterResource(Res.drawable.ic_launcher_foreground),
                    contentScale = ContentScale.Crop
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(CardColors)
                )
            }
        }

        SpaceMedium()

        CustomText(
            text = app.title,
            modifier = Modifier.weight(1f),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        RoundedCheckbox(
            checked = app.checked,
            onCheckedChange = onCheckedChange
        )
    }
}

@Preview
@Composable
private fun Preview() {
    AppRow(
        app = AppsUi(
            id = "1",
            title = "Instagram",
            iconUrl = "",
            checked = true
        ),
        onCheckedChange = {}
    )
}