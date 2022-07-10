package dev.ch8n.android

import dev.ch8n.common.App
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.MaterialTheme
import dev.ch8n.android.ui.bookmark.BookmarkScreen
import dev.ch8n.android.ui.tag.TagScreen

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                TagScreen()
            }
        }
    }
}