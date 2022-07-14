package dev.ch8n.common.data.model

import com.benasher44.uuid.uuid4


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

data class Bookmark(
    val id: String,
    val url: String,
    val tagsIds: List<String>,
    val createdAt: Long,
    val remindAt: Long,
    val isReviewed: Boolean,
    val notes: String,
    val meta: Meta
) {
    companion object {
        val EMPTY: Bookmark
            get() = Bookmark(
                id = uuid4().toString(),
                url = "",
                tagsIds = listOf(),
                createdAt = 0L,
                remindAt = 0L,
                isReviewed = false,
                notes = "",
                meta = Meta.default
            )

        val SAMPLE: Bookmark
            get() = Bookmark(
                id = uuid4().toString(),
                url = "https://www.lipsum.com/",
                tagsIds = listOf(),
                createdAt = 0L,
                remindAt = 0L,
                isReviewed = false,
                notes = "",
                meta = Meta.default
            )
    }
}
