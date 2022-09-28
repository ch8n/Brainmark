package dev.ch8n.common.data.model

import androidx.compose.runtime.Stable
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import com.benasher44.uuid.uuid4

@Stable
@Parcelize
data class Bookmark(
    val id: String,
    val tagIds: List<String>,
    val createdAt: Long,
    val lastReadAt: Long,
    val isArchived: Boolean,
    val mainImage: String,
    val title: String,
    val description: String,
    val siteName: String,
    val favIcon: String,
    val bookmarkUrl: String,
    val notes: String
) : Parcelable {
    companion object {

        val Empty: Bookmark
            get() = Bookmark(
                id = "0",
                tagIds = emptyList(),
                createdAt = 0L,
                lastReadAt = 0L,
                title = "",
                isArchived = false,
                mainImage = "",
                description = "",
                siteName = "",
                favIcon = "",
                bookmarkUrl = "",
                notes = ""
            )
        val SAMPLE: Bookmark
            get() = Bookmark(
                id = uuid4().toString(),
                tagIds = emptyList(),
                createdAt = 0L,
                lastReadAt = 0L,
                title = "What is LoreIpsome?",
                isArchived = false,
                mainImage = "https://samplelib.com/lib/preview/png/sample-boat-400x300.png",
                description = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book.",
                siteName = "https://www.lipsum.com/",
                favIcon = "https://www.lipsum.com/favicon.ico",
                bookmarkUrl = "https://www.lipsum.com/",
                notes = ""
            )
    }
}
