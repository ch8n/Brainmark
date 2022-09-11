package dev.ch8n.android

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.ch8n.common.data.remote.services.htmlParser.HtmlParserService
import dev.ch8n.common.utils.PlatformDependencies
import io.ktor.client.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BrainMarkPreviewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        brainMarkApp()
    }

    private fun brainMarkApp() {
        PlatformDependencies.setApplicationContext(applicationContext)
        setContent {
            //PreviewBrowserScreen(defaultComponentContext())
//            PreviewTagManagerScreen(
//                defaultComponentContext()
//            )
//            PreviewCreateBookmark(
//                defaultComponentContext()
//            )

            Column(
                Modifier.fillMaxSize().verticalScroll(rememberScrollState())
            ) {

                val scope = rememberCoroutineScope()
                var result by remember { mutableStateOf("") }
                var meta by remember { mutableStateOf("") }
                Button(onClick = {
                    scope.launch(Dispatchers.IO) {
                        val parser = HtmlParserService(HttpClient())
                        val url = "https://stackoverflow.com/questions/58544781/how-to-check-if-a-sequence-is-empty-in-kotlin"
                        val html = parser.getHtml(url)
                        val metaData = parser.parseMeta(url, html)
                        meta = metaData.toString()
                        result = html
                    }
                }) {
                    Text("Parse")
                }

                Text(meta, fontSize = 16.sp)
                Divider(Modifier.fillMaxWidth(), thickness = 10.dp)
                Text(result, fontSize = 16.sp)
            }

        }
    }
}

