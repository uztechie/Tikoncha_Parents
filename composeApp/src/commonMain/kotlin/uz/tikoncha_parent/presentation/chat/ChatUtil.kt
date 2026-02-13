package uz.tikoncha_parent.presentation.chat

import androidx.compose.runtime.Composable
import org.jetbrains.compose.resources.stringResource
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.bugun
import tikoncha_parents.composeapp.generated.resources.kecha
import uz.tikoncha_parent.presentation.chat.model.ChatDateLabel

object ChatUtil {
    fun getInitials(firstName: String?, lastName: String?): String {
        val first = firstName?.trim()?.firstOrNull()?.uppercaseChar()
        val last = lastName?.trim()?.firstOrNull()?.uppercaseChar()

        return listOfNotNull(first, last).joinToString("")
    }

    fun getInitials(fullName: String?): String {
        return fullName
            ?.trim()
            ?.split(Regex("[\\s-]+")) // ⬅️ space + dash
            ?.take(2)
            ?.mapNotNull { it.firstOrNull()?.uppercaseChar() }
            ?.joinToString("")
            .orEmpty()
    }



    @Composable
    fun ChatDateLabel.asText(): String = when (this) {
        is ChatDateLabel.Time -> hhmm
        ChatDateLabel.Yesterday -> stringResource(Res.string.kecha)
        is ChatDateLabel.Date -> ddMonth
        ChatDateLabel.Today -> stringResource(Res.string.bugun)
        ChatDateLabel.Unknown -> ""
    }
}