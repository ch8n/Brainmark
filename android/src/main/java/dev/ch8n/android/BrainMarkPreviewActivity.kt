package dev.ch8n.android

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.arkivanov.decompose.defaultComponentContext
import dev.ch8n.android.ui.components.ScrollableColumn
import dev.ch8n.android.ui.screens.bookmarks.PreviewBookmarkScreen
import dev.ch8n.android.ui.screens.createBookmark.PreviewCreateBookmark
import dev.ch8n.common.utils.PlatformDependencies

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

            val context = defaultComponentContext()
            ScrollableColumn {
                PreviewCreateBookmark(context)
                PreviewBookmarkScreen(context)
            }

        }
    }
}

