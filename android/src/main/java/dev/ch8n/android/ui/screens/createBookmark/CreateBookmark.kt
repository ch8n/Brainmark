package dev.ch8n.android.ui.screens.createBookmark

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.arkivanov.decompose.DefaultComponentContext
import com.google.accompanist.flowlayout.FlowRow
import dev.ch8n.android.R
import dev.ch8n.android.design.components.BookmarkCard
import dev.ch8n.android.design.components.TagChip
import dev.ch8n.common.data.model.Bookmark
import dev.ch8n.common.data.model.Tags
import dev.ch8n.common.ui.controllers.CreateBookmarkController
import dev.ch8n.common.utils.DevelopmentPreview

@Composable
fun PreviewCreateBookmark(
    componentContext: DefaultComponentContext
) {
    val controller = remember {
        CreateBookmarkController(
            componentContext = componentContext,
            navigateTo = {},
            onBack = {}
        )
    }
    DevelopmentPreview { isDark ->
        CreateBookmarkContent(
            controller = controller
        )
    }
}

@Composable
fun CreateBookmarkContent(
    controller: CreateBookmarkController,
) {

    val (bookmarkUrl, setBookmarkUrl) = remember {
        mutableStateOf("")
    }

    val (selectedTags, setSelectedTag) = remember {
        mutableStateOf(
            listOf(
                Tags.TAG_KOTLIN,
                Tags.TAG_KMP,
                Tags.TAG_KMM,
                Tags.TAG_JAVA,
                Tags.TAG_WEB_DEV,
            )
        )
    }

    val (deadLine, setDeadLine) = remember {
        mutableStateOf(System.currentTimeMillis().toString())
    }

    Box(modifier = Modifier.fillMaxSize()) {

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
                .verticalScroll(rememberScrollState())
        ) {

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
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
                    .padding(vertical = 24.dp)
                    .fillMaxWidth(),
                value = "Select from Drop Down",
                onValueChange = {},
                shape = MaterialTheme.shapes.large,
                singleLine = true,
                label = {
                    Text("Select Tags")
                },
                textStyle = MaterialTheme.typography.subtitle1,
                colors = TextFieldDefaults.textFieldColors(
                    textColor = MaterialTheme.colors.onSurface
                ),
                readOnly = true,
                trailingIcon = {
                    AsyncImage(
                        model = R.drawable.drop_down,
                        modifier = Modifier
                            .padding(horizontal = 24.dp)
                            .size(16.dp),
                        contentDescription = "",
                        contentScale = ContentScale.Fit,
                        colorFilter = ColorFilter.tint(
                            color = MaterialTheme.colors.onSurface
                        )
                    )
                }
            )

            Text(
                text = "Remove Tags",
                style = MaterialTheme.typography.h4,
                modifier = Modifier.padding(start = 8.dp, bottom = 20.dp),
                color = MaterialTheme.colors.onSurface
            )

            FlowRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(128.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                selectedTags.forEach { tag ->
                    TagChip(
                        tag = tag,
                        modifier = Modifier
                            .padding(8.dp)
                            .wrapContentWidth()
                            .height(35.dp),
                        onTagClicked = {

                        }
                    )
                }
            }

            OutlinedTextField(
                modifier = Modifier
                    .padding(vertical = 24.dp)
                    .fillMaxWidth(),
                value = deadLine,
                onValueChange = { /*TODO fix date format*/ },
                shape = MaterialTheme.shapes.large,
                singleLine = true,
                label = {
                    Text("Deadline")
                },
                textStyle = MaterialTheme.typography.subtitle1,
                colors = TextFieldDefaults.textFieldColors(
                    textColor = MaterialTheme.colors.onSurface
                ),
                readOnly = true,
                trailingIcon = {
                    AsyncImage(
                        model = R.drawable.calender,
                        modifier = Modifier
                            .padding(horizontal = 24.dp)
                            .size(16.dp),
                        contentDescription = "",
                        contentScale = ContentScale.Fit,
                        colorFilter = ColorFilter.tint(
                            color = MaterialTheme.colors.onSurface
                        )
                    )
                }
            )

            Text(
                text = "Preview",
                style = MaterialTheme.typography.h4,
                modifier = Modifier.padding(start = 8.dp, bottom = 20.dp),
                color = MaterialTheme.colors.onSurface
            )

            BookmarkCard(
                bookmark = Bookmark.SAMPLE,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp),
                onClick = {}
            )

            Spacer(Modifier.size(120.dp))

        }


        OutlinedButton(
            onClick = {

            },
            modifier = Modifier
                .padding(24.dp)
                .align(Alignment.BottomCenter)
                .size(width = 224.dp, height = 48.dp),
            shape = MaterialTheme.shapes.large,
            border = BorderStroke(4.dp, MaterialTheme.colors.onSurface)
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
