package dev.ch8n.android.ui.screens.createBookmark

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.arkivanov.decompose.DefaultComponentContext
import com.google.accompanist.flowlayout.FlowRow
import dev.ch8n.android.R
import dev.ch8n.android.design.components.BookmarkCard
import dev.ch8n.android.design.components.TagChip
import dev.ch8n.android.utils.clearFocusOnKeyboardDismiss
import dev.ch8n.android.utils.toast
import dev.ch8n.common.ui.controllers.CreateBookmarkController
import dev.ch8n.common.ui.navigation.Destinations
import dev.ch8n.common.utils.AndroidPreview

@Composable
fun PreviewCreateBookmark(
    componentContext: DefaultComponentContext
) {
    val context = LocalContext.current
    val controller = remember {
        CreateBookmarkController(
            componentContext = componentContext,
            navigateTo = {
                "On navigate to ${it::class.simpleName}".toast(context)
            },
            onBack = {
                "On Back".toast(context)
            }
        )
    }
    AndroidPreview(
        isSplitView = false,
        isDark = false
    ) {
        CreateBookmarkContent(
            controller = controller
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CreateBookmarkContent(
    controller: CreateBookmarkController,
) {

    val bookmarkState by controller.bookmarkState.collectAsState()
    val tags by controller.getAllTags.collectAsState(emptyList())

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.surface)
    ) {

        ToolbarCreateBookmark(
            modifier = Modifier
                .padding(start = 24.dp, end = 24.dp, top = 48.dp)
                .fillMaxWidth()
                .align(Alignment.TopCenter),
            onClose = {
                controller.onBack()
            }
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 24.dp, end = 24.dp, top = 112.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .clearFocusOnKeyboardDismiss(),
                value = bookmarkState.url,
                onValueChange = controller::onChangeBookmarkUrl,
                shape = MaterialTheme.shapes.large,
                singleLine = true,
                label = {
                    Text("Bookmark Url")
                },
                textStyle = MaterialTheme.typography.subtitle1,
                colors = TextFieldDefaults.textFieldColors(
                    textColor = MaterialTheme.colors.onSurface
                ),
                trailingIcon = {
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        if (bookmarkState.isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp)
                            )
                        }

                        if (bookmarkState.url.isNotEmpty()) {
                            AsyncImage(
                                model = R.drawable.close,
                                modifier = Modifier
                                    .size(24.dp)
                                    .clickable { controller.clearBookmarkUrl() },
                                contentDescription = "",
                                contentScale = ContentScale.Fit,
                                colorFilter = ColorFilter.tint(
                                    color = MaterialTheme.colors.onSurface
                                )
                            )
                        }
                    }
                },
                isError = bookmarkState.isError,
            )

            if (bookmarkState.isError) {
                Text(
                    "Already exist!",
                    color = Color.Red,
                    style = MaterialTheme.typography.subtitle1,
                    modifier = Modifier.padding(horizontal = 24.dp)
                )
            }

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .clearFocusOnKeyboardDismiss(),
                value = bookmarkState.bookmark.title,
                onValueChange = controller::onTitleChanged,
                shape = MaterialTheme.shapes.large,
                singleLine = true,
                label = {
                    Text("Title")
                },
                textStyle = MaterialTheme.typography.subtitle1,
                colors = TextFieldDefaults.textFieldColors(
                    textColor = MaterialTheme.colors.onSurface
                )
            )

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .clearFocusOnKeyboardDismiss(),
                value = bookmarkState.bookmark.description,
                onValueChange = controller::onDescriptionChanged,
                shape = MaterialTheme.shapes.large,
                singleLine = true,
                label = {
                    Text("Description")
                },
                textStyle = MaterialTheme.typography.subtitle1,
                colors = TextFieldDefaults.textFieldColors(
                    textColor = MaterialTheme.colors.onSurface
                )
            )

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .clearFocusOnKeyboardDismiss(),
                value = bookmarkState.bookmark.siteName,
                onValueChange = controller::onAuthorChanged,
                shape = MaterialTheme.shapes.large,
                singleLine = true,
                label = {
                    Text("Author")
                },
                textStyle = MaterialTheme.typography.subtitle1,
                colors = TextFieldDefaults.textFieldColors(
                    textColor = MaterialTheme.colors.onSurface
                )
            )

            val (isDropDownShow, setDropDownShown) = remember { mutableStateOf(false) }

            ExposedDropdownMenuBox(
                expanded = isDropDownShow,
                onExpandedChange = setDropDownShown,
                modifier = Modifier
                    .padding(top = 8.dp, start = 4.dp)
            ) {
                Row(
                    modifier = Modifier
                        .clickable { setDropDownShown.invoke(true) }
                        .border(
                            width = 2.dp,
                            color = MaterialTheme.colors.onSurface,
                            shape = RoundedCornerShape(32.dp)
                        )
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "+ Add tags",
                        color = MaterialTheme.colors.onSurface,
                        style = MaterialTheme.typography.subtitle1
                    )
                }

                ExposedDropdownMenu(
                    expanded = isDropDownShow,
                    onDismissRequest = {
                        setDropDownShown.invoke(false)
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {

                    DropdownMenuItem(
                        onClick = {
                            controller.navigateTo(Destinations.TagManager)
                            setDropDownShown.invoke(false)
                        }
                    ) {
                        Text(text = "Create a new Tag")
                    }

                    tags.forEach { tag ->
                        DropdownMenuItem(
                            onClick = {
                                controller.onTagAdded(tag)
                                setDropDownShown.invoke(false)
                            }
                        ) {
                            Text(text = tag.name)
                        }
                    }
                }
            }

            if (bookmarkState.bookmark.tagIds.isNotEmpty()) {
                Text(
                    text = "Click on tags to remove",
                    style = MaterialTheme.typography.subtitle1,
                    color = MaterialTheme.colors.onSurface,
                    modifier = Modifier.padding(start = 4.dp)
                )
            }

            FlowRow(
                modifier = Modifier
                    .padding(start = 4.dp)
                    .fillMaxWidth()
            ) {
                val selectedTags by controller.selectedTags.collectAsState(emptyList())
                selectedTags.forEach { tag ->
                    TagChip(
                        tag = tag,
                        modifier = Modifier
                            .padding(end = 8.dp, top = 8.dp, bottom = 8.dp)
                            .height(35.dp),
                        onTagClicked = { removeTag ->
                            controller.onTagRemoved(removeTag)
                        }
                    )
                }
            }

            Text(
                text = "Preview",
                style = MaterialTheme.typography.h3,
                color = MaterialTheme.colors.onSurface
            )

            BookmarkCard(
                bookmark = bookmarkState.bookmark,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp),
                onClick = {}
            )

            Spacer(Modifier.size(120.dp))

        }

        val context = LocalContext.current
        OutlinedButton(
            enabled = !bookmarkState.isLoading && !bookmarkState.isError,
            onClick = {
                controller.onClickCreateBookmark(
                    onError = {
                        it.toast(context)
                    },
                    onSuccess = {
                        "Bookmark created! $it".toast(context)
                    }
                )
            },
            modifier = Modifier
                .padding(24.dp)
                .align(Alignment.BottomCenter)
                .size(width = 224.dp, height = 48.dp),
            shape = MaterialTheme.shapes.large,
            border = BorderStroke(2.dp, MaterialTheme.colors.onSurface)
        ) {
            Text(
                text = "+ Create Bookmark",
                style = MaterialTheme.typography.subtitle1
            )
        }
    }
}


@Composable
private fun ToolbarCreateBookmark(
    modifier: Modifier = Modifier,
    onClose: () -> Unit
) {

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Text(
            text = "Create a Bookmark",
            style = MaterialTheme.typography.h3,
            color = MaterialTheme.colors.onSurface
        )


        AsyncImage(
            model = R.drawable.close,
            modifier = Modifier
                .size(24.dp)
                .clickable { onClose.invoke() },
            contentDescription = "",
            contentScale = ContentScale.Fit,
            colorFilter = ColorFilter.tint(
                color = MaterialTheme.colors.onSurface
            )
        )

    }

}
