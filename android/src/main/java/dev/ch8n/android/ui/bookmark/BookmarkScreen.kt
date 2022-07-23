package dev.ch8n.android.ui.bookmark

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.ch8n.common.ui.controllers.BookmarkScreenController
import dev.ch8n.common.ui.navigation.Destinations

@Composable
fun BookmarkScreen(
    controller: BookmarkScreenController
) {
    val bookmark by controller.bookmark.collectAsState()

//    Box(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(MaterialTheme.colors.background),
//    ) {
//
//
//        Column(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(16.dp)
//                .verticalScroll(state = rememberScrollState()),
//            verticalArrangement = Arrangement.spacedBy(16.dp),
//        ) {
//            Text(
//                text = "Create Bookmark",
//                fontSize = 46.sp,
//                color = Color.White,
//                modifier = Modifier
//                    .padding(top = 78.dp)
//            )
//
//
//            TextField(
//                value = bookmark.url,
//                onValueChange = {
//                    controller.onChangeBookmarkUrl(it)
//                },
//                label = {
//                    Text("Bookmark url", color = Color.White)
//                },
//            )
//
//            Text(
//                text = bookmark.run {
//                    primaryTagId + secondaryTagIds.joinToString(",")
//                },
//                color = Color.White,
//            )
//
//            Button(
//                onClick = {
//                    controller.navigateTo(Destinations.Tag)
//                }
//            ) {
//                Text("Create tag", color = Color.White)
//            }
//
//            Row {
//                Text(
//                    text = "Reminder picker : ${bookmark.remindAt}", color = Color.White,
//                )
//                Spacer(modifier = Modifier.padding(16.dp))
//                Button(
//                    onClick = {
//                        controller.onChangeReminderTime()
//                    }
//                ) {
//                    Text("Selected date", color = Color.White)
//                }
//            }
//
//            TextField(
//                value = bookmark.notes,
//                onValueChange = {
//                    controller.onChangeNotes(it)
//                },
//                label = {
//                    Text("Notes", color = Color.White)
//                }
//            )
//
//            Row {
//                Text("isReviwed", color = Color.White)
//                Spacer(modifier = Modifier.padding(16.dp))
//                Checkbox(
//                    checked = bookmark.isArchived,
//                    onCheckedChange = {
//                        controller.onChangeReviewed(it)
//                    }
//                )
//            }
//
//            Button(
//                modifier = Modifier,
//                onClick = {
//                    controller.onCreateBookmark()
//                }
//            ) {
//                Text("Save Bookmark")
//            }
//
//        }
//
//        Button(
//            modifier = Modifier
//                .align(Alignment.TopStart)
//                .padding(16.dp),
//            onClick = {
//                controller.navigateBack()
//            }
//        ) {
//            Text("goto Home Screen")
//        }
//    }
}