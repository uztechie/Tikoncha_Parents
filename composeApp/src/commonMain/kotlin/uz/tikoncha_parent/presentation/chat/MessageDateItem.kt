package uz.tikoncha_parent.presentation.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview
import uz.saidburxon.newedu.presentation.base.CustomText
import uz.tikoncha_parent.ui.ChatTextSize
import uz.tikoncha_parent.ui.DividerColor
import uz.tikoncha_parent.ui.theme.ThemeMode
import uz.tikoncha_parent.ui.theme.TikonchaParentTheme
import uz.tikoncha_parent.ui.theme.extendedColor

@Composable
fun MessageDateItem(
    modifier: Modifier = Modifier,
    date: String,
) {


    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        HorizontalDivider(
            modifier = Modifier
                .weight(1f),
            color = DividerColor,
            thickness = 1.dp
        )
        CustomText(
            text = date,
            color = MaterialTheme.extendedColor.textColor,
            fontSize = ChatTextSize,
            modifier = Modifier
                .padding(horizontal = 20.dp)
        )
        HorizontalDivider(
            modifier = Modifier
                .weight(1f),
            color = DividerColor,
            thickness = 1.dp
        )

    }

}

@Preview
@Composable
private fun Pre() {
    TikonchaParentTheme(
        ThemeMode.DARK
    ){
        Box(modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
            contentAlignment = Alignment.Center
        ){
            MessageDateItem(
                date = "10.05.2025"
            )
        }
    }



}