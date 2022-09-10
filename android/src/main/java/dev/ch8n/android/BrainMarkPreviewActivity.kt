package dev.ch8n.android

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
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
                Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {

                val scope = rememberCoroutineScope()
                var result by remember { mutableStateOf("") }
                Button(onClick = {
                    scope.launch(Dispatchers.IO) {
                        val parser = HtmlParserService(HttpClient())
                        val html = parser.getHtml("https://stackoverflow.com/")
                        result = html
                    }
                }) {
                    Text("Parse")
                }

                Text(result)

            }

        }
    }
}

