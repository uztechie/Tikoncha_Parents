package org.example.project.presentation.profile.language

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun LanguageSelector() {
    var selected by remember { mutableStateOf("uz") } // boshlanishi uzbek

    Row {
        Button(onClick = {
            selected = "uz"
            LanguageManager.setLanguage("uz")
        }) {
            Text("O‘zbekcha")
        }

        Spacer(modifier = Modifier.width(8.dp))

        Button(onClick = {
            selected = "ru"
            LanguageManager.setLanguage("ru")
        }) {
            Text("Русский")
        }
    }
}
