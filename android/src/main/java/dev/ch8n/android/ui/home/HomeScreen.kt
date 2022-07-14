package dev.ch8n.android.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Checkbox
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
                Card(
                    modifier = Modifier
                        .padding(24.dp)
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    contentColor = Color.White,
                    elevation = 2.dp
                ) {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // TODO loading image --> https://github.com/alialbaali/Kamel#multiplatform
                        Text(
                            text = bookmark.meta.title,
                            style = TextStyle(
                                fontWeight = FontWeight.SemiBold,
                                color = Color.Black,
                                fontSize = 18.sp
                            ),
                            maxLines = 3,
                            overflow = TextOverflow.Ellipsis
                        )

                        Text(
                            text = bookmark.meta.description,
                            style = TextStyle(
                                fontWeight = FontWeight.Normal,
                                color = Color.Black,
                                fontSize = 16.sp,
                            ),
                            maxLines = 4,
                            overflow = TextOverflow.Ellipsis
                        )

                        Text(
                            text = bookmark.meta.siteName,
                            style = TextStyle(
                                textDecoration = TextDecoration.Underline,
                                fontWeight = FontWeight.Medium,
                                color = Color.Black,
                                fontSize = 12.sp
                            ),
                        )

                        Button(
                            modifier = Modifier.align(Alignment.End),
                            onClick = {},
                        ) {
                            Text(
                                text = "Schedule Review",
                                style = TextStyle(
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 14.sp
                                ),
                            )
                        }
                    }

                }
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