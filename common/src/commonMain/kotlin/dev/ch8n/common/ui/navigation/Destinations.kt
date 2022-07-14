package dev.ch8n.common.ui.navigation

import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize

sealed class Destinations : Parcelable {
    @Parcelize
    object Home : Destinations()

    @Parcelize
    object Bookmark : Destinations()

    @Parcelize
    object Tag : Destinations()
}