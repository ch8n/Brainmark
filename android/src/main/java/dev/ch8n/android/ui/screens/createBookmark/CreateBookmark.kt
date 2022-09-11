package dev.ch8n.android.ui.screens.createBookmark

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import dev.ch8n.common.data.model.Bookmark
import dev.ch8n.common.data.model.Tags
import dev.ch8n.common.ui.controllers.CreateBookmarkController
import dev.ch8n.common.utils.AndroidPreview
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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

    val scope = rememberCoroutineScope()

    val (bookmarkUrl, setBookmarkUrl) = remember { mutableStateOf("https://chetangupta.net") }
    val (title, setTitle) = remember { mutableStateOf("") }
    val (description, setDescription) = remember { mutableStateOf("") }
    val (author, setAuthor) = remember { mutableStateOf("") }
    val (selectedTags, setSelectedTags) = remember { mutableStateOf(listOf<Tags>()) }
    val (bookmark, setBookmark) = remember { mutableStateOf<Bookmark>(Bookmark.new) }

    LaunchedEffect(bookmarkUrl) {
        scope.coroutineContext.cancelChildren()
        scope.launch {
            delay(500)
            val htmlParser = controller.htmlService
            val html = htmlParser.getHtml(bookmarkUrl)
            val meta = htmlParser.parseMeta(bookmarkUrl, html)
            setTitle(meta.title)
            setDescription(meta.description)
            setAuthor(meta.authorOrSite)
            setBookmark(
                bookmark.copy(
                    title = meta.title,
                    description = meta.description,
                    siteName = meta.authorOrSite,
                    favIcon = meta.favIcon,
                    mainImage = meta.mainImage,
                    bookmarkUrl = meta.url
                )
            )
        }
    }

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
                value = bookmarkUrl,
                onValueChange = setBookmarkUrl,
                shape = MaterialTheme.shapes.large,
                singleLine = true,
                label = {
                    Text("Bookmark Url")
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
                value = title,
                onValueChange = setTitle,
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
                value = description,
                onValueChange = setDescription,
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
                value = author,
                onValueChange = setAuthor,
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
                    .fillMaxWidth()
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
                    tags.forEach { tag ->
                        DropdownMenuItem(
                            onClick = {
                                val updated = (selectedTags + tag).toSet()
                                setSelectedTags.invoke(updated.toList())
                                setDropDownShown.invoke(false)
                            }
                        ) {
                            Text(text = tag.name)
                        }
                    }
                }
            }

            if (selectedTags.isNotEmpty()) {
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
                selectedTags.forEach { tag ->
                    TagChip(
                        tag = tag,
                        modifier = Modifier.height(35.dp),
                        onTagClicked = { removeTag ->
                            val updated = selectedTags.filter { it.id != removeTag.id }
                            setSelectedTags.invoke(updated)
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
                bookmark = bookmark,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp),
                onClick = {}
            )

            Spacer(Modifier.size(200.dp))

        }


        OutlinedButton(
            onClick = {

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
