package org.example.project.presentation.task

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import org.example.project.presentation.base.CustomHeader
import org.example.project.presentation.base.CustomSelectionButton
import org.example.project.presentation.base.theme.*
import org.example.project.presentation.base.theme.SpaceLarge
import org.example.project.presentation.base.theme.SpaceMedium
import org.example.project.presentation.base.theme.SpaceSmall
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.*

@Composable
fun TaskScreen(
    navigator: Navigator?,
    state: TaskState,
    event: (TaskEvent) -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor)
            .verticalScroll(rememberScrollState())
    ) {

        CustomHeader(
            title = stringResource(Res.string.vazifalar),
            trailingIcon = {

                SpaceMedium()

                FilledTonalIconButton(
                    modifier = Modifier.size(LargeIconButtonSize),
                    onClick = { },
                    colors = IconButtonDefaults.filledTonalIconButtonColors(
                        containerColor = TonalButtonContainerColor,
                        contentColor = TextColor
                    ),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.task_square2),
                        contentDescription = "",
                        tint = PrimaryColor,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(LargeIconButtonPadding)
                    )
                }
            }
        )

        Column(
            modifier = Modifier.padding(ContainerPadding)
        ) {

            Row(
                modifier = Modifier
                    .wrapContentSize()
                    .border(
                        1.dp,
                        WheelPickerSelectionColor,
                        RoundedCornerShape(ContainerCornerRadius)
                    )
                    .padding(vertical = 10.dp, horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {

                Image(
                    painter = painterResource(Res.drawable.calendar_search),
                    contentDescription = "",
                )

                SpaceSmall()

                Text(
                    text = reformattedToday(state.date),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                )
            }

            SpaceLarge()

            Text(
                text = stringResource(Res.string.farzandingiz_vazifalari),
                fontSize = SmallTextSize,
                color = HintTextColor,
                fontWeight = FontWeight.W500
            )

            SpaceMedium()

            CustomSelectionButton(
                text = state.title,
                modifier = Modifier
                    .fillMaxWidth(),
                onClick = { },
                painter = painterResource(Res.drawable.parent),
                loading = false,
                label = stringResource(Res.string.farzandlaringiz)
            )

            SpaceMedium()

            Text(
                text = stringResource(Res.string.sizdan_vazifalar),
                fontSize = SmallTextSize,
                fontWeight = FontWeight.W600,
                color = TextColor
            )

            SpaceSmall()



            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(2f),
                shape = RoundedCornerShape(CardCornerRadius),
                colors = CardDefaults.cardColors(
                    containerColor = WheelPickerSelectionColor
                )
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(Res.string.xozir_vazifalar_yo_q),
                        fontSize = SmallTextSize,
                        color = HintTextColor,
                        fontWeight = FontWeight.W500
                    )
                }
            }

            SpaceMedium()

            TextButton(
                onClick = {
                    navigator?.push(AddNewTaskScreen())
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, PrimaryColor, RoundedCornerShape(ButtonCornerRadius))
                    .height(ButtonHeight),
            ) {
                Row {

                    Text(
                        text = stringResource(Res.string.vazifa_qo_shish),
                        fontSize = 16.sp,
                        color = PrimaryColor
                    )

                    SpaceMedium()

                    Icon(
                        painter = painterResource(Res.drawable.add_square),
                        contentDescription = "",
                        tint = PrimaryColor
                    )
                }
            }

            SpaceMedium()

            Text(
                text = stringResource(Res.string.farzandingiz_vazifalari),
                fontSize = SmallTextSize,
                fontWeight = FontWeight.W600,
            )

            SpaceSmall()

            if (state.newTasks.isEmpty()) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(2f),
                    shape = RoundedCornerShape(CardCornerRadius),
                    colors = CardDefaults.cardColors(
                        containerColor = WheelPickerSelectionColor
                    )
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Text(
                            text = stringResource(Res.string.xozir_vazifalar_yo_q),
                            fontSize = 12.sp,
                            color = HintTextColor,
                            fontWeight = FontWeight.W500
                        )
                    }
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    state.newTasks.forEach {
                        TaskItem(
                            task = it,
                            onEditIconClick = { },
                            onDoneButtonClick = { },
                            onDetailsIconClick = { }
                        )
                    }
                }
            }


        }

        Spacer(modifier = Modifier.weight(1f))
    }
}

@Preview
@Composable
private fun Preview() {
    TaskScreen(
        state = TaskState(),
        event = {},
        navigator = LocalNavigator.current
    )
}