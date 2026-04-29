package uz.tikoncha_parent.presentation.policy.template

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.farzandingiz_kun_davomida_telefondan_qancha
import tikoncha_parents.composeapp.generated.resources.qoshish
import tikoncha_parents.composeapp.generated.resources.sleep_large_icon
import tikoncha_parents.composeapp.generated.resources.uyqu_vaqti_rejasi
import uz.tikoncha_parent.presentation.base.DashedBorderButton
import uz.tikoncha_parent.presentation.base.simpleShadow
import uz.tikoncha_parent.ui.Space
import uz.tikoncha_parent.ui.theme.AppColors
import uz.tikoncha_parent.ui.theme.AppTypography
import uz.tikoncha_parent.ui.theme.TikonchaParentTheme

@Composable
fun PolicyTemplateEmptyItem(
    modifier: Modifier = Modifier,
    icon: Painter,
    title: String,
    desc: String,
    onClick: () -> Unit
){
    Column(
        modifier = modifier
            .fillMaxWidth()
            .simpleShadow(RoundedCornerShape(20.dp))
            .background(AppColors.bg.surface, RoundedCornerShape(20.dp))
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = icon,
                contentDescription = "",
                modifier = Modifier
                    .size(56.dp)
            )
            Space(12.dp)
            Column {
                Text(
                    text = title,
                    modifier = Modifier
                        .fillMaxWidth(),
                    color = AppColors.text.primary,
                    style = AppTypography.titleSmSemiBold
                )
                Space(8.dp)
                Text(
                    text = desc,
                    modifier = Modifier
                        .fillMaxWidth(),
                    color = AppColors.text.secondary,
                    style = AppTypography.emphasizedSmRegular
                )
            }
        }
        Space(12.dp)
        DashedBorderButton(
            text = stringResource(Res.string.qoshish),
            onClick = onClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(36.dp)
        )
    }
}

@Preview
@Composable
fun Pre(){
    TikonchaParentTheme {
        PolicyTemplateEmptyItem(
            icon = painterResource(Res.drawable.sleep_large_icon),
            title = stringResource(Res.string.uyqu_vaqti_rejasi),
            desc = stringResource(Res.string.farzandingiz_kun_davomida_telefondan_qancha),
            onClick = {}
        )
    }
}