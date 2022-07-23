package dev.ch8n.common.data.model

import androidx.compose.runtime.Immutable
import com.benasher44.uuid.uuid4


/**
 * TODO
 * 1. save meta details to DB
 * 2. save primary tag and secondary tag in DB
 * 3. save color of tag in DB
 */
data class Meta(
    val title: String,
    val image: String,
    val description: String,
    val siteName: String,
    val favIcon: String
) {
    companion object {
        val default = Meta(
            title = "What is Lorem Ipsum?",
            image = "https://loremflickr.com/640/360",
            description = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.",
            siteName = "lipsum.com",
            favIcon = "https://www.lipsum.com/favicon.ico"
        )
    }
}

@Immutable
data class Bookmark(
    val id: String,
    val tagIds: List<String>,
    val createdAt: Long,
    val remindAt: Long,
    val isArchived: Boolean,
    val title: String,
    val mainImage: String,
    val description: String,
    val siteName: String,
    val favIcon: String,
    val bookmarkUrl: String,
    val flashCardIds: List<String>,
) {
    companion object {
        val SAMPLE: Bookmark
            get() = Bookmark(
                id = uuid4().toString(),
                tagIds = emptyList(),
                createdAt = 0L,
                remindAt = 0L,
                title = "What is LoreIpsome?",
                isArchived = false,
                mainImage = "https://samplelib.com/lib/preview/png/sample-boat-400x300.png",
                description = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book.",
                siteName = "https://www.lipsum.com/",
                favIcon = "https://www.lipsum.com/favicon.ico",
                bookmarkUrl = "https://www.lipsum.com/",
                flashCardIds = emptyList()
            )
    }
}
