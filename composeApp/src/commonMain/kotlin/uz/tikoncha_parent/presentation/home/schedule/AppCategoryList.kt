package uz.tikoncha_parent.presentation.home.schedule

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import uz.tikoncha_parent.data.mapper.AppCategoryUi

@Composable
fun AppCategoryList(
    categories: List<AppCategoryUi>,
    onToggleCategory: (String) -> Unit,
    onToggleApp: (String, String, Boolean) -> Unit
){
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(categories, key = {it.id}) { category ->
            AppCategoryItem(
                category = category,
                onHeaderClick = { onToggleCategory(category.id) },
                onToggleApp = { appId, checked -> onToggleApp(category.id, appId, checked) },
                onToggleAll = { checked ->
                    category.apps.forEach { app ->
                        onToggleApp(category.id, app.id, checked)
                    }
                }
            )
        }
    }
}