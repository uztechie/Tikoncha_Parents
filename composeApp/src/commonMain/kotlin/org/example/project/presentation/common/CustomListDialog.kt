package org.example.project.presentation.common

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import org.example.project.ui.BackgroundColor
import org.example.project.ui.CardCornerRadius
import org.example.project.ui.CloseButtonInnerPadding
import org.example.project.ui.CloseButtonSize
import org.example.project.ui.ContainerPadding
import org.example.project.ui.ItemHeight
import org.example.project.ui.NormalTextSize
import org.example.project.ui.PrimaryColor
import org.example.project.ui.SpaceMedium
import org.example.project.ui.TextColor
import org.example.project.ui.TextFieldCornerRadius
import org.example.project.ui.TextFieldIconSize
import org.example.project.ui.TextFieldInnerPadding
import org.jetbrains.compose.resources.painterResource
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.close_circle
import tikoncha_parents.composeapp.generated.resources.happyemoji_icon

@Composable
fun <T>CustomListDialog(
    modifier: Modifier = Modifier,
    title:String,
    items:List<T>,
    show:Boolean = true,
    onItemSelected:(T) -> Unit,
    onDismiss:() -> Unit
) {

    var searchQuery by remember {
        mutableStateOf("")
    }
    val filteredItems = remember(searchQuery, items) {
        if (searchQuery.isBlank()) {
            items
        } else {
            items.filter { it.toString().lowercase().trim().contains(searchQuery.trim().lowercase().toString(), ignoreCase = false) }
        }

    }



    if (show){
        Dialog(
            onDismissRequest = onDismiss
        ) {

            BoxWithConstraints(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center,

                ) {
                val dialogWidth = maxWidth
                val dialogHeight = maxHeight * 0.9f

                Card(
                    modifier = Modifier
                        .width(dialogWidth)
                        .height(dialogHeight),
                    colors = CardDefaults.cardColors(
                        containerColor = BackgroundColor
                    ),
                    shape = RoundedCornerShape(CardCornerRadius)
                ) {
                    Column (
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(ContainerPadding),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            CustomText(
                                text = title,
                                fontSize = NormalTextSize,
                                color = TextColor,
                                modifier = Modifier
                                    .weight(1f)
                            )
                            SpaceMedium()

                            IconButton(
                                onClick = {
                                    onDismiss()
                                },
                                modifier = Modifier
                                    .size(CloseButtonSize)
                                    .padding(CloseButtonInnerPadding)
                            ) {
                                Icon(
                                    painter = painterResource(Res.drawable.close_circle),
                                    contentDescription = ""
                                )
                            }

                        }

                        SpaceMedium()

//                        CustomTextField(
//                            modifier = modifier
//                                .height(TextFieldHeight)
//                                .fillMaxWidth()
//                                .border(1.dp, PrimaryColor, RoundedCornerShape(TextFieldCornerRadius)),
//                            value = searchQuery,
//                            label = stringResource(R.string.izlash),
//                            onValueChange = {
//                                searchQuery = it
//                            },
//                            containerColor = Color.Transparent,
//                            contentColor = TextColor,
//                            leadingIcon = {
//                                Icon(
//                                    painter = painterResource(R.drawable.search_normal),
//                                    contentDescription = "Search",
//                                    tint = TextColor,
//                                    modifier = Modifier
//                                        .size(TextFieldIconSize)
//                                )
//                            },
//                            trailingIcon = {
//                                if (searchQuery.isNotEmpty()) {
//                                    IconButton(
//                                        onClick = {
//                                            searchQuery = ""
//                                        },
//                                        modifier = Modifier
//                                            .size(TextFieldIconSize)
//                                    ) {
//                                        Icon(
//                                            imageVector = Icons.Default.Close,
//                                            contentDescription = "Close",
//                                            tint = TextColor
//                                        )
//                                    }
//                                }
//                            }
//                        )
//
//                        SpaceSmall()

                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .border(1.dp, PrimaryColor, RoundedCornerShape(TextFieldCornerRadius)),

                            ) {
                            items(filteredItems){ item->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(ItemHeight)
                                        .padding(horizontal = TextFieldInnerPadding)
                                        .clickable {
                                            onItemSelected(item)
                                            onDismiss()
                                        },
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        painter = painterResource(Res.drawable.happyemoji_icon),
                                        contentDescription = "Search",
                                        tint = PrimaryColor,
                                        modifier = Modifier
                                            .padding(end = TextFieldInnerPadding)
                                            .size(TextFieldIconSize)
                                    )

                                    CustomText(
                                        text = item.toString(),
                                        fontSize = NormalTextSize,
                                        color = TextColor,
                                        modifier = Modifier
                                    )
                                }
                            }
                        }

                    }
                }
            }



        }

    }



}