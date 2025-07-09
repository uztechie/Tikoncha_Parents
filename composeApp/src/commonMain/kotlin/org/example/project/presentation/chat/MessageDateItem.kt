package uz.saidburxon.newedu.presentation.feature.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.example.project.presentation.base.theme.DividerColor
import org.example.project.presentation.base.theme.TextColor
import org.example.project.ui.ChatTextSize
import org.jetbrains.compose.ui.tooling.preview.Preview
import uz.saidburxon.newedu.presentation.base.CustomText


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
            color = TextColor,
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