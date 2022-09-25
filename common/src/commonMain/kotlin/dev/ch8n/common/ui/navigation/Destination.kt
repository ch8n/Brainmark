package dev.ch8n.common.ui.navigation

import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import dev.ch8n.common.data.model.Bookmark

sealed interface Destinations : Parcelable

@Parcelize
object HomeDestination : Destinations

@Parcelize
object TagManagerDestination : Destinations

@Parcelize
object BookmarksDestination : Destinations

@Parcelize
object CreateBookmarksDestination : Destinations

@Parcelize
data class PreviewBookmarkHomeDestination(val bookmark: Bookmark) : Destinations

@Parcelize
data class PreviewBookmarkReaderModeDestination(val bookmark: Bookmark) : Destinations

@Parcelize
data class PreviewBookmarkChromeTabDestination(val bookmark: Bookmark) : Destinations

@Parcelize
data class PreviewBookmarkEmbeddedWebDestination(val bookmark: Bookmark) : Destinations

