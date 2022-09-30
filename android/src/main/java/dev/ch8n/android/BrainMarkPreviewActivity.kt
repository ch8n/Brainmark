package dev.ch8n.android

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.arkivanov.decompose.defaultComponentContext
import dev.ch8n.android.design.components.PreviewBottomNav
import dev.ch8n.common.foundations.ApplicationContext

class BrainMarkPreviewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ApplicationContext.setContext(applicationContext)
        brainMarkApp()
    }

    private fun brainMarkApp() {
        setContent {
            //PreviewBrowserScreen(defaultComponentContext())
//            PreviewTagManagerScreen(
//                defaultComponentContext()
//            )

            val context = defaultComponentContext()
            //PreviewCreateBookmark(context)
            //PreviewBookmarkScreen(context)
            //PreviewBrowserScreen(context)
            // ReaderView()

            PreviewBottomNav()
        }
    }
}

