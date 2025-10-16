package uz.tikoncha_parent.data.service

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import uz.tikoncha_parent.MainActivity

object NotificationIntentFactory {

    fun chatPendingIntent(ctx: Context, chatId: String, chatTitle: String?, chatType: String?): PendingIntent {
        val uri = Uri.Builder()
            .scheme("tikoncha_parent")
            .authority("chat")
            .appendQueryParameter("chatId", chatId)
            .appendQueryParameter("chatTitle", chatTitle ?: "")
            .appendQueryParameter("chatType", chatType ?: "")
            .build()

        val intent = Intent(Intent.ACTION_VIEW, uri, ctx, MainActivity::class.java)
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val flags = PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        return PendingIntent.getActivity(ctx, chatId.hashCode(), intent, flags)
    }
}