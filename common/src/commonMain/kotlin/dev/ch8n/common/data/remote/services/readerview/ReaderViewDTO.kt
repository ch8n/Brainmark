package dev.ch8n.common.data.remote.services.readerview

data class ReaderViewDTO(
    val title: String,
    val byline: String,
    val excerpt: String,
    val plainText: String
) {
    companion object {
        val empty = ReaderViewDTO(
            title = "",
            byline = "",
            excerpt = "",
            plainText = ""
        )
    }
}
