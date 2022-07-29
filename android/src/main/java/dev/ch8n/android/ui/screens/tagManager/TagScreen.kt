package dev.ch8n.android.ui.screens.tagManager

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.arkivanov.decompose.DefaultComponentContext
import com.google.accompanist.flowlayout.FlowRow
import dev.ch8n.android.R
import dev.ch8n.android.design.components.BottomNavbar
import dev.ch8n.android.design.components.TagChip
import dev.ch8n.android.utils.parseColor
import dev.ch8n.common.ui.controllers.TagManagerController
import dev.ch8n.common.ui.navigation.Destinations
import dev.ch8n.common.utils.DevelopmentPreview
import kotlinx.coroutines.launch


@Composable
fun PreviewTagManagerScreen(
    componentContext: DefaultComponentContext
) {
    val controller = remember {
        TagManagerController(
            componentContext = componentContext,
            navigateTo = {},
            onBack = {}
        )
    }
    DevelopmentPreview { isDark ->
        TagScreenManager(controller, {})
    }
}


@Composable
fun TagScreenManager(
    controller: TagManagerController,
    onSettingsClicked: () -> Unit
) {

    val tags by controller.getAllTags.collectAsState(emptyList())
    val viewState by controller.state.collectAsState()
    val scope = rememberCoroutineScope()

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        ToolbarTagManager(
            modifier = Modifier
                .padding(top = 36.dp, start = 16.dp, end = 16.dp)
                .fillMaxWidth()
                .align(Alignment.TopCenter),
            onSettingsClicked = onSettingsClicked
        )

        Column(
            modifier = Modifier
                .padding(top = 100.dp)
                .fillMaxWidth()
        ) {
            Spacer(Modifier.size(24.dp))

            Text(
                text = "Manage your tags",
                color = MaterialTheme.colors.onSurface,
                modifier = Modifier.padding(horizontal = 24.dp),
                style = MaterialTheme.typography.h3
            )

            Text(
                text = "Create, update and delete your tags. Use color picker to apply unique color to your tags.",
                color = MaterialTheme.colors.onSurface,
                modifier = Modifier.padding(horizontal = 24.dp),
                style = MaterialTheme.typography.subtitle1,
            )

            Spacer(Modifier.size(18.dp))

            CreateTag(
                tagName = viewState.tagName,
                tagColor = viewState.tagColor,
                isLoading = viewState.isLoading,
                error = viewState.errorMsg,
                modifier = Modifier
                    .padding(horizontal = 24.dp)
                    .fillMaxWidth(),
                onTagNameUpdated = controller::updateTagName,
                onColorPickerClicked = {},
                onSaveTagClicked = {
                    scope.launch {
                        controller.saveTag()
                    }
                },
                onDeleteTagClicked = {
                    scope.launch {
                        controller.deleteTag()
                    }
                }
            )

            Spacer(Modifier.size(40.dp))

            LazyColumn {
                item {
                    FlowRow(
                        modifier = Modifier.padding(start = 24.dp, end = 24.dp)
                    ) {
                        tags.forEach { tag ->
                            TagChip(
                                tag = tag,
                                modifier = Modifier
                                    .padding(8.dp)
                                    .wrapContentWidth()
                                    .height(35.dp),
                                onTagClicked = controller::selectTag
                            )
                        }
                    }
                }

                item {
                    Spacer(Modifier.size(100.dp))
                }
            }
        }


        BottomNavbar(
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.BottomCenter)
                .width(240.dp),
            onTagClicked = {
                // do nothing...
            },
            onBookmarkClicked = {
                controller.navigateTo(Destinations.BookmarkBrowser)
            },
            onNewBookmarkClicked = {}
        )
    }

}


@Composable
fun CreateTag(
    modifier: Modifier,
    tagName: String,
    tagColor: String,
    isLoading: Boolean,
    error: String,
    onTagNameUpdated: (name: String) -> Unit,
    onColorPickerClicked: () -> Unit,
    onSaveTagClicked: () -> Unit,
    onDeleteTagClicked: () -> Unit,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Column {

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(0.6f),
                value = tagName,
                onValueChange = onTagNameUpdated,
                shape = MaterialTheme.shapes.large,
                singleLine = true,
                label = {
                    Text("Tag Name")
                },
                textStyle = MaterialTheme.typography.subtitle1,
                colors = TextFieldDefaults.textFieldColors(
                    textColor = MaterialTheme.colors.onSurface
                ),
                trailingIcon = {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp)
                        )
                    }
                },
                enabled = !isLoading,
                isError = error.isNotEmpty(),
            )

            if (error.isNotEmpty()) {
                Text(
                    text = error,
                    style = MaterialTheme.typography.body1,
                    color = Color.Red,
                    modifier = Modifier.padding(start = 24.dp, top = 4.dp),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }



        Row(
            modifier = Modifier.width(240.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Icon(
                painter = painterResource(R.drawable.color_picker),
                contentDescription = "",
                modifier = Modifier
                    .size(20.dp)
                    .clickable(
                        onClick = onColorPickerClicked
                    ),
                tint = tagColor.parseColor(),
            )

            Icon(
                painter = painterResource(R.drawable.delete),
                contentDescription = "",
                modifier = Modifier
                    .size(20.dp)
                    .clickable(
                        onClick = onDeleteTagClicked
                    ),
                tint = MaterialTheme.colors.onSurface,
            )

            Icon(
                painter = painterResource(R.drawable.save),
                contentDescription = "",
                modifier = Modifier
                    .size(20.dp)
                    .clickable(
                        onClick = onSaveTagClicked
                    ),
                tint = MaterialTheme.colors.onSurface,
            )
        }
    }
}

@Composable
private fun ToolbarTagManager(
    modifier: Modifier = Modifier,
    onSettingsClicked: () -> Unit
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

            Text(
                text = "Tag Manager",
                style = MaterialTheme.typography.h1,
                color = MaterialTheme.colors.onSurface
            )
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