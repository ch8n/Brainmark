package dev.ch8n.common.data.remote.services.htmlParser

data class MetaDTO(
    val title: String,
    val description: String,
    val mediaType: String,
    val mainImage: String,
    val favIcon: String,
    val authorOrSite: String,
    val url: String
) {
    companion object {
        val empty = MetaDTO(
            title = "",
            description = "",
            mediaType = "",
            mainImage = "",
            favIcon = "",
            authorOrSite = "",
            url = ""
        )
    }
}
