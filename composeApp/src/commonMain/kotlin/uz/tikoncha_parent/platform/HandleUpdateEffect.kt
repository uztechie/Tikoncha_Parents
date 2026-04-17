package uz.tikoncha_parent.platform

import androidx.compose.runtime.Composable
import uz.tikoncha_parent.presentation.in_app_update.UpdateViewModel

@Composable
expect fun HandleUpdateEffect(viewModel: UpdateViewModel)