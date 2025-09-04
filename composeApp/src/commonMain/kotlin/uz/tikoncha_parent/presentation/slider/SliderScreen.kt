package uz.tikoncha_parent.presentation.slider

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import uz.tikoncha_parent.data.local.AppSettings
import uz.tikoncha_parent.presentation.base.LogoText
import uz.tikoncha_parent.ui.*
import uz.tikoncha_parent.presentation.login.LoginScreen
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import tikoncha_parents.composeapp.generated.resources.*
import uz.saidburxon.newedu.presentation.base.CustomText

class SliderScreen : Screen {

    @Composable
    override fun Content() {

        val navigator = LocalNavigator.current

        val titles = listOf(
            Res.string.bilimdon_bo_lishga_birinchi_qadam,
            Res.string.bilimdon_bo_lishga_birinchi_qadam,
            Res.string.bilimdon_bo_lishga_birinchi_qadam,
        )
        val descriptions = listOf(
            Res.string.bu_ilova_senga_kun_davomida_nimalar_qilganingni_eslatadi_o_yin_ham_dars_ham_muhim_hammasi_muvozanatda_bo_lsin,
            Res.string.bu_ilova_senga_kun_davomida_nimalar_qilganingni_eslatadi_o_yin_ham_dars_ham_muhim_hammasi_muvozanatda_bo_lsin,
            Res.string.bu_ilova_senga_kun_davomida_nimalar_qilganingni_eslatadi_o_yin_ham_dars_ham_muhim_hammasi_muvozanatda_bo_lsin,
        )

        DisposableEffect(Unit){
            onDispose {
                AppSettings.isFirstLaunch = false
            }
        }

        val pageItems = mutableListOf(
            painterResource(Res.drawable.slider_background),
            painterResource(Res.drawable.slider_background),
            painterResource(Res.drawable.slider_background),
        )
        val pagerState = rememberPagerState(pageCount = { pageItems.size })

        LaunchedEffect(pagerState) {
            var previousPage = 0
            snapshotFlow { pagerState.isScrollInProgress }
                .collect { isScrolling ->
                    if (!isScrolling) {
                        // Scroll tugadi
                        val current = pagerState.currentPage
                        val offset = pagerState.currentPageOffsetFraction

                        // Foydalanuvchi oxirgi sahifada edi va yana o‘ngga surdi
                        if (previousPage == pagerState.pageCount - 1 && current == pagerState.pageCount - 1 && offset >= 0.0f) {

                            navigator?.push(LoginScreen())
                        }

                        previousPage = current
                    }
                }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(ContainerPadding)
        ) {

            HorizontalPager(
                state = pagerState
            ) { page ->

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(shape = RoundedCornerShape(CardCornerRadius))
                        .background(PrimaryColor)
                        .paint(
                            painter = pageItems[page],
                            alpha = 0.2f,
                            contentScale = ContentScale.FillBounds,
                            colorFilter = ColorFilter.tint(
                                PrimaryColor,
                                blendMode = BlendMode.Softlight
                            )
                        )
                ) {
                    PagerIndicator(pageItems.size, pagerState.currentPage)

                    SpaceLarge()

                    CustomText(
                        text = stringResource(titles[page]),
                        modifier = Modifier.padding(start = 20.dp),
                        fontSize = 32.sp,
                        style = TextStyle.Default,
                        fontWeight = FontWeight.W700,
                        color = DisableButtonTextColor,
                        fontStyle = FontStyle.Italic
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    Image(
                        painter = painterResource(Res.drawable.star),
                        contentDescription = "Star",
                        modifier = Modifier
                            .padding(start = 150.dp)
                            .size(269.dp)
                    )

                    LogoText()

                    CustomText(
                        text = stringResource(descriptions[page]),
                        modifier = Modifier.padding(start = 24.dp),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.W500,
                        color = DisableButtonTextColor
                    )

                    SpaceLarge()

                    TextButton(
                        onClick = {
                            navigator?.push(LoginScreen())
                        },
                        modifier = Modifier.padding(start = 0.dp)
                    ) {
                        CustomText(
                            text = stringResource(Res.string.o_tkazib_yuborish),
                            modifier = Modifier.padding(start = 12.dp),
                            color = DisableButtonTextColor,
                            fontSize = 18.sp,
                            style = TextStyle(textDecoration = TextDecoration.Underline),
                            fontWeight = FontWeight.W600
                        )
                    }
                    SpaceLarge()
                }
            }
        }
    }
}

@Preview(
//    heightDp = 700,
)
@Composable
private fun Preview() {
    SliderScreen()
}