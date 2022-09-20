package dev.ch8n.common.ui.navigation

import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize

sealed class Destinations : Parcelable {
    @Parcelize
    object Home : Destinations()

    @Parcelize
    object Bookmarks : Destinations()

    @Parcelize
    object TagManager : Destinations()

    @Parcelize
    object CreateBookmark : Destinations()

    @Parcelize
    data class PreviewBookmark(val bookmarkId: String) : Destinations()

    @Parcelize
    data class ReaderScreen(val url: String) : Destinations()

    @Parcelize
    data class WebView(val url: String) : Destinations()

}