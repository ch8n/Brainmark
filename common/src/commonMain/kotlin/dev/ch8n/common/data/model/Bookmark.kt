package dev.ch8n.common.data.model

data class Bookmark(
    val id: String,
    val url: String,
    val tags: List<Tags>,
    val createAt: Long,
    val deadLine: Long,
    val isReviewed: Boolean,
    val summaryUrl: String,
    val notes: String
)
