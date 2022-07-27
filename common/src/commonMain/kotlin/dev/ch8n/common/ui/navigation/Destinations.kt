package dev.ch8n.common.ui.navigation

import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize

sealed class Destinations : Parcelable {
    @Parcelize
    object Home : Destinations()

    @Parcelize
    object BookmarkBrowser : Destinations()

    @Parcelize
    object TagManager : Destinations()

    @Parcelize
    object CreateBookmark : Destinations()

    @Parcelize
    object PreviewScreen : Destinations()
}