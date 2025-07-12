package org.example.project.presentation.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import org.example.project.presentation.common.CustomText
import org.example.project.ui.BackgroundColor
import org.example.project.ui.NormalTextSize
import org.example.project.ui.PrimaryColor
import org.example.project.ui.ProfileImageSize
import org.example.project.ui.ShapeCornerRadius
import org.example.project.ui.SmallIconButtonSize
import org.example.project.ui.SpaceUltraSmall
import org.example.project.ui.TextColor
import org.example.project.ui.TonalButtonContainerColor
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.camera
import tikoncha_parents.composeapp.generated.resources.profile_hedgehog_img

@Composable
fun ProfileHeader(
    fullName: String,
    fathersName: String,
    image: String?,
    onSelectImageButtonClick: () -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(BackgroundColor),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(
            modifier = Modifier
                .size(ProfileImageSize),
            contentAlignment = Alignment.BottomEnd
        ) {

            Image(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(50)),
                painter = if (
                    image == null ||
                    image.toString() == "" ||
                    image.toString() == "null"
                ) painterResource(Res.drawable.profile_hedgehog_img) else{
                    painterResource(Res.drawable.profile_hedgehog_img)
                },
//                    rememberAsyncImagePainter(
//                        model = image
//                    ),
                contentDescription = "",
                contentScale = ContentScale.Crop
            )

            IconButton(
                modifier = Modifier
                    .clip(RoundedCornerShape(ShapeCornerRadius))
                    .background(TonalButtonContainerColor)
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
                color = TextColor,
                fontSize = NormalTextSize,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1
            )

            CustomText(
                text = fathersName,
                color = TextColor,
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
        onSelectImageButtonClick = {}
    )
}