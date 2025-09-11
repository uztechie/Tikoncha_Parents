package uz.tikoncha_parent.presentation.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import uz.tikoncha_parent.ui.NormalTextSize
import uz.tikoncha_parent.ui.PrimaryColor
import uz.tikoncha_parent.ui.ProfileImageSize
import uz.tikoncha_parent.ui.ShapeCornerRadius
import uz.tikoncha_parent.ui.SmallIconButtonSize
import uz.tikoncha_parent.ui.SpaceUltraSmall
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.camera
import tikoncha_parents.composeapp.generated.resources.profile_hedgehog_img
import uz.saidburxon.newedu.presentation.base.CustomText
import uz.tikoncha_parent.ui.theme.extendedColor

@Composable
fun ProfileHeader(
    fullName: String,
    fathersName: String,
    image: ImageBitmap?,
    state: ProfileState,
    onSelectImageButtonClick: () -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.extendedColor.backgroundColor),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(
            modifier = Modifier
                .size(ProfileImageSize),
            contentAlignment = Alignment.BottomEnd
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(50))
                    .background(MaterialTheme.extendedColor.backgroundColor)
                    .border(width = 2.dp, color = MaterialTheme.extendedColor.cardColor, shape = RoundedCornerShape(50))
            ){
                AsyncImage(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(50))
                        .background(MaterialTheme.extendedColor.backgroundColor),
                    model = state.profileImageUrl,
                    placeholder = painterResource(Res.drawable.profile_hedgehog_img),
                    error = painterResource(Res.drawable.profile_hedgehog_img),
                    contentScale = ContentScale.Crop,
                    contentDescription = null,
//                    onError = { e ->  e.result.throwable.printStackTrace()}
                )

//                Image(
//                    modifier = Modifier
//                        .fillMaxSize()
//                        .clip(RoundedCornerShape(50))
//                        .background(MaterialTheme.colorScheme.background),
//                    contentScale = ContentScale.Crop,
//                    contentDescription = null,
//                    painter = painterResource(Res.drawable.profile_hedgehog_img)
//                )
            }

            IconButton(
                modifier = Modifier
                    .clip(RoundedCornerShape(ShapeCornerRadius))
                    .background(MaterialTheme.extendedColor.tonalButtonColor)
                    .size(SmallIconButtonSize),
                onClick = {
                    onSelectImageButtonClick()
                },

                ) {

                Icon(
                    painter = painterResource(Res.drawable.camera),
                    contentDescription = "",
                    tint = PrimaryColor.copy(alpha = 0.8f),
                    modifier = Modifier
                        .fillMaxSize(0.75f)
                )
            }
        }

        SpaceUltraSmall()

        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {

            CustomText(
                text = fullName,
                fontSize = NormalTextSize,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1
            )

            CustomText(
                text = fathersName,
                fontSize = NormalTextSize,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1
            )
        }
    }
}

@Preview
@Composable
private fun Pre() {
    ProfileHeader(
        fullName = "Ahmadjonov Husniddin",
        fathersName = "Nazirjon o'g'li",
        image = null,
        onSelectImageButtonClick = {},
        state = ProfileState()
    )
}