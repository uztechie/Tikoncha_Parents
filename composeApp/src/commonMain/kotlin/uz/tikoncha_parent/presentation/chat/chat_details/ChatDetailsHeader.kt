package uz.tikoncha_parent.presentation.chat.chat_details

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import uz.tikoncha_parent.ui.HeaderHeight
import uz.tikoncha_parent.ui.ShapeCornerRadius

@Composable
fun ChatDetailsHeader(
    navController: NavController,
    state: ChatDetailState,
) {

    val bottomShape = RoundedCornerShape(topStart = 0.dp, topEnd = 0.dp, bottomStart = ShapeCornerRadius, bottomEnd = ShapeCornerRadius)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .height(HeaderHeight),
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            Modifier
                .fillMaxWidth()
                .shadow(
                    elevation = 4.dp,
                    shape = bottomShape,
                    ambientColor = MaterialTheme.extendedColor.shadowColor,
                    spotColor = MaterialTheme.extendedColor.shadowColor
                )
                .background(
                    color = MaterialTheme.extendedColor.backgroundColor,
                    shape = bottomShape
                )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(HeaderHeight)
                    .padding(horizontal = ContainerPadding),
                verticalAlignment = Alignment.CenterVertically
            )
            {
                FilledTonalIconButton(
                    modifier = size(NormalIconButtonSize),
                    onClick = { navController.popBackStack() },
                    colors = IconButtonDefaults.filledTonalIconButtonColors(
                        containerColor = MaterialTheme.extendedColor.secondaryBackgroundColor,
                        contentColor = MaterialTheme.extendedColor.textColor
                    ),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.arrow_left),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(NormalIconButtonPadding)
                    )
                }

                SpaceSmall()

                AsyncImage(
                    model = state.chatAvatar,
                    error = painterResource(R.drawable.chat_icon),
                    placeholder = painterResource(R.drawable.chat_icon),
                    contentDescription = null,
                    modifier = size(NormalIconButtonSize)
                        .border(1.dp, TonalButtonContainerColor, CircleShape)
                        .clip(CircleShape)

                )


                SpaceSmall()


                Column(Modifier.fillMaxWidth()) {
                    CustomText(
                        text = state.chatTitle,
                        fontSize = SmallTextSize,
                        maxLines = 1,
                        lineHeight = SmallTextSize,
                        fontWeight = FontWeight.Normal
                    )


                    val memberBuilder = StringBuilder()
                    if (state.memberCount>1){
                        memberBuilder.append(stringResource(R.string.azolar))
                    }else{
                        memberBuilder.append(stringResource(R.string.azo))
                    }

                    memberBuilder.append(": ")
                    memberBuilder.append(state.memberCount)


                    CustomText(
                        text = memberBuilder.toString(),
                        fontSize = UltraSmallTextSize,
                        maxLines = 1,
                        color = MaterialTheme.extendedColor.hintColor,
                        lineHeight = UltraSmallTextSize,
                    )


                }
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    NewEduTheme {
        ChatDetailsHeader(
            navController = rememberNavController(),
            state = ChatDetailState()
        )
    }

}
