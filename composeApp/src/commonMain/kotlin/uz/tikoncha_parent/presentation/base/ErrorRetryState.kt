package uz.tikoncha_parent.presentation.base

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.dialog_failed
import tikoncha_parents.composeapp.generated.resources.dialog_internet
import tikoncha_parents.composeapp.generated.resources.qayta_urinish
import uz.tikoncha_parent.domain.model.app_error.ErrorCause
import uz.tikoncha_parent.domain.model.app_error.Outcome
import uz.tikoncha_parent.ui.theme.AppColors
import uz.tikoncha_parent.ui.theme.AppTypography

/**
 * Yuklash yiqilganda ro'yxat o'rnida chiqadigan holat.
 *
 * Matn asText() orqali @Composable kontekstda hosil bo'ladi — til
 * almashtirilsa o'zi yangilanadi. "Qayta urinish" tugmasi faqat
 * ErrorCause.isRetryable bo'lganda ko'rinadi.
 */
@Composable
fun ErrorRetryState(
    failure: Outcome.Failure,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
    isRetrying: Boolean = false,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(
                if (failure.cause == ErrorCause.NoInternet || failure.cause == ErrorCause.Timeout)
                    Res.drawable.dialog_internet
                else
                    Res.drawable.dialog_failed
            ),
            contentDescription = null,
            modifier = Modifier.size(90.dp)
        )
        Spacer(Modifier.height(26.dp))

        Text(
            text = failure.asText(),
            style = AppTypography.emphasizedMdMedium,
            color = AppColors.text.secondary,
            textAlign = TextAlign.Center
        )

        if (failure.cause.isRetryable) {
            Spacer(Modifier.height(20.dp))
            CustomOutlinedButton(
                text = stringResource(Res.string.qayta_urinish),
                onClick = onRetry,
                enabled = !isRetrying,
                modifier = Modifier.padding(horizontal = 40.dp),
                leadingIcon = if (isRetrying) {
                    {
                        CircularProgressIndicator(
                            modifier = Modifier.size(18.dp),
                            strokeWidth = 2.dp,
                            color = AppColors.text.accentEmphasis
                        )
                    }
                } else null
            )
        }
    }
}