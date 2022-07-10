package dev.ch8n.android.ui.bookmark

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.ch8n.common.data.model.Bookmark

@Composable
fun BookmarkScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
    ) {
        Button(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(16.dp),
            onClick = {

            }
        ) {
            Text("goto Home Screen")
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .verticalScroll(state = rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Text(
                text = "Create Bookmark",
                fontSize = 46.sp,
                color = Color.White,
                modifier = Modifier
                    .padding(top = 78.dp)
            )

            val (bookmark, setBookmark) = remember { mutableStateOf(Bookmark.EMPTY) }

            TextField(
                value = bookmark.url,
                onValueChange = {
                    setBookmark.invoke(bookmark.copy(url = it))
                },
                label = {
                    Text("Bookmark url", color = Color.White)
                },

                )

            Text(
                text = bookmark.tagsIds.joinToString(","),
                color = Color.White,
            )

            Button(
                onClick = {}
            ) {
                Text("Create tag", color = Color.White)
            }

            Row {
                Text(
                    text = "Reminder picker : ${bookmark.remindAt}", color = Color.White,
                )
                Spacer(modifier = Modifier.padding(16.dp))
                Button(
                    onClick = {}
                ) {
                    Text("Selected date", color = Color.White)
                }
            }

            TextField(
                value = bookmark.notes,
                onValueChange = {
                    setBookmark.invoke(bookmark.copy(notes = it))
                },
                label = {
                    Text("Notes", color = Color.White)
                }
            )

            Row {
                Text("isReviwed", color = Color.White)
                Spacer(modifier = Modifier.padding(16.dp))
                Checkbox(
                    checked = bookmark.isReviewed,
                    onCheckedChange = {
                        setBookmark.invoke(bookmark.copy(isReviewed = it))
                    }
                )
            }

            Button(
                modifier = Modifier,
                onClick = {

                }
            ) {
                Text("Save Bookmark")
            }

        }
    }
}