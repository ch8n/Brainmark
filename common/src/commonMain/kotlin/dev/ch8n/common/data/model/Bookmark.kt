package dev.ch8n.common.data.model

import androidx.compose.runtime.Immutable
import com.benasher44.uuid.uuid4
import kotlinx.datetime.Clock


data class FlashCard(
    val id: String,
    val bookmarkId: String,
    val mainImage: String,
    val question: String,
    val linkOnWeb: String,
    val otherNotes: String
) {
    companion object {
        val SAMPLE: FlashCard
            get() = FlashCard(
                id = uuid4().toString(),
                bookmarkId = "",
                question = "What is LoreIpsome?",
                linkOnWeb = "https://www.lipsum.com/#:~:text=But%20I%20must%20explain%20to",
                otherNotes = "",
                // to be from unsplash images
                mainImage = "https://samplelib.com/lib/preview/png/sample-boat-400x300.png"
            )
    }
}

@Immutable
data class Bookmark(
    val id: String,
    val tagIds: List<String>,
    val createdAt: Long,
    val isArchived: Boolean,
    val mainImage: String,
    val title: String,
    val description: String,
    val siteName: String,
    val favIcon: String,
    val bookmarkUrl: String,
    val flashCardIds: List<String>,
    val notes: String
) {
    companion object {


        // TODO create a func
        val new: Bookmark
            get() = Bookmark(
                id = uuid4().toString(),
                tagIds = emptyList(),
                createdAt = Clock.System.now().epochSeconds,
                title = "",
                isArchived = false,
                mainImage = "",
                description = "",
                siteName = "",
                favIcon = "",
                bookmarkUrl = "",
                flashCardIds = emptyList(),
                notes = ""
            )
        val SAMPLE: Bookmark
            get() = Bookmark(
                id = uuid4().toString(),
                tagIds = emptyList(),
                createdAt = 0L,
                title = "What is LoreIpsome?",
                isArchived = false,
                mainImage = "https://samplelib.com/lib/preview/png/sample-boat-400x300.png",
                description = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book.",
                siteName = "https://www.lipsum.com/",
                favIcon = "https://www.lipsum.com/favicon.ico",
                bookmarkUrl = "https://www.lipsum.com/",
                flashCardIds = emptyList(),
                notes = ""
            )
    }
}
