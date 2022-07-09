package dev.ch8n.common.data.model

import com.benasher44.uuid.uuid4

data class Bookmark(
    val id: String,
    val url: String,
    val tagsIds: List<String>,
    val createdAt: Long,
    val remindAt: Long,
    val isReviewed: Boolean,
    val notes: String
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
                notes = ""
            )
    }
}
