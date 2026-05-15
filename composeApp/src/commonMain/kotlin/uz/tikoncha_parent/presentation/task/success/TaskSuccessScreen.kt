package uz.tikoncha_parent.presentation.task.success

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import cafe.adriel.voyager.navigator.Navigator
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.check_gold
import tikoncha_parents.composeapp.generated.resources.davom_etish
import tikoncha_parents.composeapp.generated.resources.vazifa_tahrirlandi
import tikoncha_parents.composeapp.generated.resources.vazifa_yuborildi
import tikoncha_parents.composeapp.generated.resources.yangi_vazifa_qo_shish
import uz.tikoncha_parent.presentation.base.CustomButton
import uz.tikoncha_parent.presentation.base.CustomButtonNew
import uz.tikoncha_parent.presentation.task.TaskScreen
import uz.tikoncha_parent.presentation.task.create_task.CreateTaskEvent
import uz.tikoncha_parent.presentation.task.create_task.CreateTaskScreen
import uz.tikoncha_parent.presentation.task.create_task.CreateTaskViewModel
import uz.tikoncha_parent.presentation.task.model.rememberSharedScreenModel
import uz.tikoncha_parent.ui.theme.AppColors
import uz.tikoncha_parent.ui.theme.AppTypography
import uz.tikoncha_parent.ui.theme.ThemeMode
import uz.tikoncha_parent.ui.theme.TikonchaParentTheme
import uz.tikoncha_parent.ui.theme.rememberScreenSystemBars

class TaskSuccessScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val createVm = rememberSharedScreenModel<CreateTaskViewModel>()
        val state by createVm.state.collectAsStateWithLifecycle()
        val event = createVm::onEvent


        TaskSuccessUI(
            navigator = navigator,
            onClick = {
                event(CreateTaskEvent.OnReset)
                navigator.popUntil { it is TaskScreen }
            },
            onCreateTask = {
                event(CreateTaskEvent.OnReset)
                navigator.popUntil { it is TaskScreen }
                navigator.push(CreateTaskScreen())
            }
        )
    }
}

@Composable
fun TaskSuccessUI(
    navigator: Navigator?,
    isEditing: Boolean = false,
    onClick: () -> Unit,
    onCreateTask: () -> Unit
) {
    val successText = if (isEditing) stringResource(Res.string.vazifa_tahrirlandi)
    else stringResource(Res.string.vazifa_yuborildi)
    val systemBars = rememberScreenSystemBars(
        statusBarColor = AppColors.bg.secondary,
        navigationBarColor = AppColors.bg.secondary
    )
    Box(
        modifier = Modifier
            .fillMaxSize()
            .then(systemBars.modifier)
            .background(AppColors.bg.secondary)
            .padding(horizontal = 20.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 140.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Image(
                painter = painterResource(Res.drawable.check_gold),
                contentDescription = null,
                modifier = Modifier.size(200.dp)
            )
            Spacer(Modifier.height(32.dp))

            Text(
                text = successText,
                style = AppTypography.displayMdSemiBold,
                color = AppColors.text.accentEmphasis,
            )
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 24.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            CustomButtonNew(
                text = stringResource(Res.string.davom_etish),
                onClick = onClick,
                modifier = Modifier.fillMaxWidth(),
                enabled = true,
            )

            CustomButton(
                text = stringResource(Res.string.yangi_vazifa_qo_shish),
                color = AppColors.button.surface,
                textColor = AppColors.text.primary,
                onClick = onCreateTask,
                modifier = Modifier.fillMaxWidth(),
                enabled = true,
            )
        }
    }
}

@Preview
@Composable
private fun Preview() {
    TikonchaParentTheme(
        ThemeMode.LIGHT
    ) {
        TaskSuccessUI(
            navigator = null,
            onClick = {},
            onCreateTask = {}
        )
    }
}