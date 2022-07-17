package dev.ch8n.android.ui.home

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Checkbox
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import dev.ch8n.android.design.BookmarkCard
import dev.ch8n.common.ui.controllers.HomeScreenController
import dev.ch8n.common.ui.navigation.Destinations

@Composable
fun HomeScreen(
    controller: HomeScreenController
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
    ) {

        val bookmarks by controller.bookmarks.collectAsState()

        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
        ) {
            bookmarks.forEach { bookmark ->

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