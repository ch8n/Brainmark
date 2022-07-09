package dev.ch8n.common.data.model

data class Bookmark(
    val id: String,
    val url: String,
    val tagsIds: List<String>,
    val createdAt: Long,
    val remindAt: Long,
    val isReviewed: Boolean,
    val notes: String
)
