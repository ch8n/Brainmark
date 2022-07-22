package dev.ch8n.android.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import dev.ch8n.android.design.BookmarkCard
import dev.ch8n.android.design.theme.codGray
import dev.ch8n.common.ui.controllers.HomeScreenController
import dev.ch8n.common.ui.navigation.Destinations

@Composable
fun HomeScreen(
    controller: HomeScreenController
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.surface),
    ) {

        val bookmarks by controller.bookmarks.collectAsState()

        LazyColumn {
            items(bookmarks) { bookmark ->
                BookmarkCard(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                        .height(400.dp),
                    bookmark = bookmark
                )
            }
        }

        Button(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            onClick = {
                controller.navigateTo(Destinations.Bookmark)
            }
        ) {
            Text("Add Bookmark")
        }
    }
}