package dev.ch8n.common.data.model

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.benasher44.uuid.uuid4


data class Tags(
    val id: String,
    val name: String,
    val color: Int,
) {
    companion object {

        val New: Tags
            get() = Tags(
                id = uuid4().toString(),
                name = "",
                color = Color.Cyan.toArgb()
            )

        val TAG_KOTLIN: Tags
            get() = Tags(
                id = uuid4().toString(),
                name = "Kotlin",
                color = Color.Magenta.toArgb()
            )

        val TAG_JAVA: Tags
            get() = Tags(
                id = uuid4().toString(),
                name = "Java",
                color = Color.Green.toArgb()
            )

        val TAG_WEB_DEV: Tags
            get() = Tags(
                id = uuid4().toString(),
                name = "WebDev",
                color = Color.Red.toArgb()
            )

        val TAG_KMM: Tags
            get() = Tags(
                id = uuid4().toString(),
                name = "Kotlin Multiplatform",
                color = Color.Yellow.toArgb()
            )
    }
}