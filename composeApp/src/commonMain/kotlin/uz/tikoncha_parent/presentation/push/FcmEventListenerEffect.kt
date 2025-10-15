package uz.tikoncha_parent.presentation.push

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import uz.tikoncha_parent.domain.model.AppBlock
import uz.tikoncha_parent.domain.model.MessageBlock
import uz.tikoncha_parent.domain.model.NewsBlock
import uz.tikoncha_parent.domain.model.TodoBlock

@Composable
fun FcmEventListenerEffect(
    onApp: (AppBlock) -> Unit = {},
    onTodo: (TodoBlock, String?, String?) -> Unit = { _, _, _ -> },
    onNews: (NewsBlock, String?, String?) -> Unit = { _, _, _ -> },
    onChat: (MessageBlock, String?, String?) -> Unit = { _, _, _ -> },
    onGeneral: (String?, String?) -> Unit = { _,_ -> }
){
    DisposableEffect(Unit) {
        val l = object : FcmEventListener {
            override fun onAppRule(app: AppBlock) {
                onApp(app)
            }

            override fun onTodo(todo: TodoBlock, title: String?, message: String?) {
                onTodo(todo, title, message)
            }

            override fun onNews(news: NewsBlock, title: String?, message: String?) = onNews(news, title, message)
            override fun onChat(msg: MessageBlock, title: String?, message: String?) = onChat(msg, title, message)
            override fun onGeneral(title: String?, message: String?) = onGeneral(title, message)

        }
        FcmEventBus.add(l)

        onDispose {
            FcmEventBus.remove(l)
        }
    }
}