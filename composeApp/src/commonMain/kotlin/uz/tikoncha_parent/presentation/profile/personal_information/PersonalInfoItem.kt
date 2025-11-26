package uz.tikoncha_parent.presentation.profile.personal_information

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import uz.tikoncha_parent.domain.model.UserInfo
import uz.tikoncha_parent.ui.AppIconInnerPadding
import uz.tikoncha_parent.ui.CardCornerRadius
import uz.tikoncha_parent.ui.ContainerPadding
import uz.tikoncha_parent.ui.NormalLargeTextSize
import uz.tikoncha_parent.ui.SpaceSmall
import uz.tikoncha_parent.ui.SpaceUltraSmall
import org.jetbrains.compose.resources.stringResource
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.calendar
import tikoncha_parents.composeapp.generated.resources.class_icon
import tikoncha_parents.composeapp.generated.resources.ism
import tikoncha_parents.composeapp.generated.resources.jins
import tikoncha_parents.composeapp.generated.resources.maktab
import tikoncha_parents.composeapp.generated.resources.phone
import tikoncha_parents.composeapp.generated.resources.profile
import tikoncha_parents.composeapp.generated.resources.school_icon
import tikoncha_parents.composeapp.generated.resources.shaxsiy_malumotlar
import tikoncha_parents.composeapp.generated.resources.shift_clock
import tikoncha_parents.composeapp.generated.resources.sinf
import tikoncha_parents.composeapp.generated.resources.smena
import tikoncha_parents.composeapp.generated.resources.telefon_nomer
import tikoncha_parents.composeapp.generated.resources.two_users
import tikoncha_parents.composeapp.generated.resources.yosh
import tikoncha_parents.composeapp.generated.resources.yosh_1
import uz.saidburxon.newedu.presentation.base.CustomText
import uz.tikoncha_parent.ui.theme.extendedColor

@Composable
fun PersonalInfoItem(userInfo: UserInfo?){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(CardCornerRadius))
            .background(MaterialTheme.extendedColor.tonalButtonColor)
            .padding(horizontal = AppIconInnerPadding, vertical = ContainerPadding)
    )
    {
        CustomText(
            text = stringResource(Res.string.shaxsiy_malumotlar),
            fontSize = NormalLargeTextSize,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier
                .padding(start = 6.dp)
        )

        SpaceSmall()

        PersonalInformationItemRow(
            icon = Res.drawable.profile,
            title = stringResource(Res.string.ism),
            value = userInfo?.name?:""
        )
        SpaceUltraSmall()
        PersonalInformationItemRow(
            icon = Res.drawable.calendar,
            title = stringResource(Res.string.yosh),
            value = "${userInfo?.age} ${stringResource(Res.string.yosh_1)}"
        )
        SpaceUltraSmall()

        val genderRes = if (userInfo?.genderType?.resId == null){
            ""
        }
        else{
            stringResource(userInfo.genderType.resId)
        }

        PersonalInformationItemRow(
            icon = Res.drawable.two_users,
            title = stringResource(Res.string.jins),
            value = genderRes
        )
        SpaceUltraSmall()
        PersonalInformationItemRow(
            icon = Res.drawable.phone,
            title = stringResource(Res.string.telefon_nomer),
            value = userInfo?.phoneNumber
        )

        SpaceSmall()

        if (userInfo?.schoolName != null){
            Column {
                CustomText(
                    text = stringResource(Res.string.maktab),
                    fontSize = NormalLargeTextSize,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier
                        .padding(start = 6.dp)
                )

                SpaceSmall()

                PersonalInformationItemRow(
                    icon = Res.drawable.school_icon,
                    title = stringResource(Res.string.maktab),
                    value = userInfo.schoolName?:""
                )
                SpaceUltraSmall()
                PersonalInformationItemRow(
                    icon = Res.drawable.class_icon,
                    title = stringResource(Res.string.sinf),
                    value = userInfo.schoolClassName?:""
                )
                SpaceUltraSmall()
                PersonalInformationItemRow(
                    icon = Res.drawable.shift_clock,
                    title = stringResource(Res.string.smena),
                    value = userInfo.shift?:""
                )
                SpaceSmall()
            }
        }
    }
}
