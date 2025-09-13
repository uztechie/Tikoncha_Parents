package uz.tikoncha_parent.presentation.common

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import uz.tikoncha_parent.presentation.base.Loading
import uz.tikoncha_parent.ui.CloseButtonInnerPadding
import uz.tikoncha_parent.ui.CloseButtonSize
import uz.tikoncha_parent.ui.ContainerPadding
import uz.tikoncha_parent.ui.ItemHeight
import uz.tikoncha_parent.ui.NormalTextSize
import uz.tikoncha_parent.ui.PrimaryColor
import uz.tikoncha_parent.ui.SpaceMedium
import uz.tikoncha_parent.ui.TextFieldCornerRadius
import uz.tikoncha_parent.ui.TextFieldIconSize
import uz.tikoncha_parent.ui.TextFieldInnerPadding
import org.jetbrains.compose.resources.painterResource
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.close_circle
import tikoncha_parents.composeapp.generated.resources.happyemoji_icon
import uz.saidburxon.newedu.presentation.base.CustomText
import uz.tikoncha_parent.ui.theme.extendedColor

@Composable
fun <T>CustomListDialog(
    modifier: Modifier = Modifier,
    title:String,
    items:List<T>,
    show:Boolean = true,
    loading: Boolean = false,
    errorMessage: String = "",
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
                        containerColor = MaterialTheme.extendedColor.backgroundColor
                    ),
                    shape = RoundedCornerShape(TextFieldCornerRadius)
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



                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .border(1.dp, MaterialTheme.extendedColor.primaryColor, RoundedCornerShape(TextFieldCornerRadius)),
                            contentAlignment = Alignment.Center
                        ){
                            if (loading || errorMessage.isNotEmpty()){
                                Loading(loading)
                                CustomText(
                                    text = errorMessage,
                                    fontSize = NormalTextSize,
                                    modifier = Modifier
                                )
                            }
                            else{
                                LazyColumn(
                                    modifier = Modifier
                                        .fillMaxSize(),
                                )
                                {
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
                                                tint = MaterialTheme.extendedColor.primaryColor,
                                                modifier = Modifier
                                                    .padding(end = TextFieldInnerPadding)
                                                    .size(TextFieldIconSize)
                                            )

                                            CustomText(
                                                text = item.toString(),
                                                fontSize = NormalTextSize,
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
    }
}