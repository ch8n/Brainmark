package dev.ch8n.common.ui.navigation

import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize

sealed class Destination : Parcelable {
    @Parcelize
    object Home : Destination()

    @Parcelize
    object Bookmarks : Destination()

    @Parcelize
    object TagManager : Destination()

    @Parcelize
    object CreateBookmark : Destination()

    @Parcelize
    data class PreviewBookmark(val bookmarkId: String) : Destination()

    @Parcelize
    data class ReaderScreen(val url: String) : Destination()

    @Parcelize
    data class WebView(val url: String) : Destination()

}