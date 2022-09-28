package dev.ch8n.android.ui.screens.bookmarks

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.arkivanov.decompose.DefaultComponentContext
import dev.ch8n.android.R
import dev.ch8n.android.design.components.BookmarkCard
import dev.ch8n.android.utils.clearFocusOnKeyboardDismiss
import dev.ch8n.android.utils.rememberMutableState
import dev.ch8n.android.utils.toast
import dev.ch8n.common.data.model.Tags
import dev.ch8n.common.ui.navigation.EmptyNavController
import dev.ch8n.common.ui.navigation.PreviewBookmarkHomeDestination
import dev.ch8n.common.ui.navigation.TagManagerDestination
import dev.ch8n.common.utils.AndroidPreview


@Composable
fun PreviewBookmarkScreen(
    componentContext: DefaultComponentContext
) {
    val context = LocalContext.current
    val controller = AndroidBookmarksController(
        navController = EmptyNavController()
    )
    AndroidPreview(
        isSplitView = false,
        isDark = true
    ) {
        BookmarkScreen(controller, onSettingsClicked = {
            "Settings clicked".toast(context)
        })
    }
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BookmarkScreen(
    controller: AndroidBookmarksController,
    onSettingsClicked: () -> Unit
) {
    val screenState by controller.screenState.collectAsState()
    val bookmarks by controller.bookmarks.collectAsState()
    val pagingInvalidateKey by controller.pagingInvalidateKey.collectAsState()
    val allTags by controller.allTags.collectAsState(emptyList())
    var isTagDropDownShow by rememberMutableState(false)

    LaunchedEffect(bookmarks, pagingInvalidateKey) {
        if (bookmarks.isEmpty()) {
            controller.nextBookmark()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.surface)
    ) {

        Column(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth(),
        ) {

            ExposedDropdownMenuBox(
                expanded = isTagDropDownShow,
                onExpandedChange = { isTagDropDownShow = it },
                modifier = Modifier.fillMaxWidth()
            ) {

                ToolbarBookmark(
                    selectedTag = screenState.selectedTag,
                    onSettingsClicked = onSettingsClicked,
                    onSelectTag = { isTagDropDownShow = true },
                    modifier = Modifier.fillMaxWidth()
                )

                ExposedDropdownMenu(
                    expanded = isTagDropDownShow,
                    onDismissRequest = { isTagDropDownShow = false },
                    modifier = Modifier.fillMaxWidth()
                ) {

                    DropdownMenuItem(
                        modifier = Modifier.background(
                            MaterialTheme.colors.onSurface
                        ),
                        onClick = {
                            isTagDropDownShow = false
                            controller.routeTo(TagManagerDestination)
                        }
                    ) {
                        Text(
                            text = "Create a new Tag",
                            color = MaterialTheme.colors.surface
                        )
                    }

                    DropdownMenuItem(
                        onClick = {
                            isTagDropDownShow = false
                            controller.allTagSelected()
                        }
                    ) {
                        Text(text = "All Tag")
                    }

                    DropdownMenuItem(
                        onClick = {
                            isTagDropDownShow = false
                            controller.onUntaggedSelected()
                        }
                    ) {
                        Text(text = "Untagged")
                    }

                    allTags.forEach { tag ->
                        DropdownMenuItem(
                            onClick = {
                                isTagDropDownShow = false
                                controller.onTagSelected(tag)
                            }
                        ) {
                            Text(text = tag.name)
                        }
                    }
                }
            }

            Spacer(Modifier.size(16.dp))

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .clearFocusOnKeyboardDismiss(),
                value = screenState.searchQuery,
                onValueChange = {
                    controller.onSearchQueryUpdated(it)
                },
                shape = MaterialTheme.shapes.large,
                singleLine = true,
                label = {
                    Text("Search")
                },
                placeholder = {
                    Text(
                        text = "Search each for keyword  in title and description",
                        style = MaterialTheme.typography.subtitle1,
                        color = MaterialTheme.colors.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                },
                textStyle = MaterialTheme.typography.subtitle1,
                colors = TextFieldDefaults.textFieldColors(
                    textColor = MaterialTheme.colors.onSurface
                ),
                trailingIcon = {
                    if (screenState.searchQuery.isEmpty()) {
                        AsyncImage(
                            model = R.drawable.search,
                            modifier = Modifier
                                .padding(horizontal = 24.dp)
                                .size(24.dp),
                            contentDescription = "",
                            contentScale = ContentScale.Fit,
                            colorFilter = ColorFilter.tint(
                                color = MaterialTheme.colors.onSurface
                            )
                        )
                    } else {
                        AsyncImage(
                            model = R.drawable.close,
                            modifier = Modifier
                                .padding(horizontal = 24.dp)
                                .clickable {
                                    controller.onClearSearchQuery()
                                }
                                .size(24.dp),
                            contentDescription = "",
                            contentScale = ContentScale.Fit,
                            colorFilter = ColorFilter.tint(
                                color = MaterialTheme.colors.onSurface
                            )
                        )
                    }
                }
            )

            Spacer(Modifier.size(16.dp))

            Divider(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .align(Alignment.CenterHorizontally),
                color = MaterialTheme.colors.onSurface,
                thickness = 0.5.dp
            )

            Spacer(Modifier.size(8.dp))

            AnimatedVisibility(
                visible = bookmarks.isEmpty()
            ) {
                Box(
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)
                        .fillMaxWidth()
                        .height(150.dp)
                        .border(1.dp, MaterialTheme.colors.onSurface, MaterialTheme.shapes.large)
                ) {
                    Text(
                        text = "You haven't saved any bookmarks...",
                        modifier = Modifier.padding(24.dp).align(Alignment.Center),
                        style = MaterialTheme.typography.h3,
                        color = MaterialTheme.colors.onSurface,
                        textAlign = TextAlign.Center
                    )
                }
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 126.dp)
            ) {
                itemsIndexed(bookmarks) { index, _bookmark ->
                    BookmarkCard(
                        bookmark = _bookmark,
                        modifier = Modifier
                            .padding(vertical = 16.dp)
                            .fillMaxWidth()
                            .height(176.dp),
                        onClick = {
                            controller.routeTo(
                                PreviewBookmarkHomeDestination(it)
                            )
                        }
                    )

                    LaunchedEffect(index) {
                        if (index == bookmarks.lastIndex) {
                            controller.nextBookmark()
                        }
                    }
                }

            }

        }
    }
}

@Composable
private fun ToolbarBookmark(
    selectedTag: Tags,
    onSettingsClicked: () -> Unit,
    onSelectTag: () -> Unit,
    modifier: Modifier = Modifier
) {

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            AsyncImage(
                model = R.drawable.brain,
                modifier = Modifier.size(40.dp),
                contentDescription = "",
                contentScale = ContentScale.Fit,
                colorFilter = ColorFilter.tint(
                    color = MaterialTheme.colors.onSurface
                )
            )

            Column(
                modifier = Modifier
                    .offset(y = 4.dp)
                    .clickable {
                        onSelectTag.invoke()
                    }
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = selectedTag.name,
                        style = MaterialTheme.typography.h1,
                        color = MaterialTheme.colors.onSurface
                    )

                    AsyncImage(
                        model = R.drawable.drop_down,
                        modifier = Modifier
                            .size(16.dp),
                        contentDescription = "",
                        contentScale = ContentScale.Fit,
                        colorFilter = ColorFilter.tint(
                            color = MaterialTheme.colors.onSurface
                        )
                    )

                }

                Text(
                    text = "Select from tag dropdown",
                    style = MaterialTheme.typography.body2,
                    color = MaterialTheme.colors.onSurface
                )
            }
        }


        AsyncImage(
            model = R.drawable.settings,
            modifier = Modifier
                .size(24.dp)
                .clickable { onSettingsClicked.invoke() },
            contentDescription = "",
            contentScale = ContentScale.Fit,
            colorFilter = ColorFilter.tint(
                color = MaterialTheme.colors.onSurface
            )
        )

    }

}