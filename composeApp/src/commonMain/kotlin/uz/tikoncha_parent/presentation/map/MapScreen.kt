package uz.tikoncha_parent.presentation.map

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import uz.tikoncha_parent.platform.KmpWebView

class MapScreen2 : Screen {


    val json = """
{
  "name": "Siz",
  "lat": 40.7821,
  "lng": 72.3442,
  "children": [
    {
      "name": "Ali",
      "lat": 40.7900,
      "lng": 72.3500
    },
    {
      "name": "Bek",
      "lat": 40.7750,
      "lng": 72.3300
    }
  ]
}
""".trimIndent()

    @Composable
    override fun Content() {
        KmpWebView(
            url = "https://tikoncha.uz/map/location/",
//            url = "https://www.google.com/",
            onCreated = { controller ->
                controller.postJson(json)
            }
        )
    }


}