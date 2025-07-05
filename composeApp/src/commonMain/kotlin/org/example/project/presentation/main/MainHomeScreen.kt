// MainHomeScreen.kt
package uz.saidburxon.newedu.presentation.feature.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.example.project.presentation.task.TaskEvent
import org.example.project.presentation.task.TaskState

@Composable
fun MainHomeScreen(
    state: TaskState,
    event: (TaskEvent) -> Unit
) {

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text("state.title")
    }
}
