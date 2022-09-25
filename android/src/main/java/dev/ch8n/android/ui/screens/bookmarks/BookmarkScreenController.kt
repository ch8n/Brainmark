package dev.ch8n.android.ui.screens.bookmarks

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import dev.ch8n.android.utils.toast
import dev.ch8n.common.ui.controllers.BookmarksController
import dev.ch8n.common.ui.navigation.NavController

class AndroidBookmarksController(
    navController: NavController
) : BookmarksController(navController) {

    @Composable
    override fun Render() {
        val context = LocalContext.current
        BookmarkScreen(
            controller = this,
            onSettingsClicked = {
                "TODO".toast(context)
            }
        )
    }
}