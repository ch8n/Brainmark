package dev.ch8n.android.ui.screens.browser

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.arkivanov.decompose.DefaultComponentContext
import dev.ch8n.android.R
import dev.ch8n.android.design.components.BottomNavbar
import dev.ch8n.common.data.model.Tags
import dev.ch8n.common.ui.controllers.BrowserController
import dev.ch8n.common.ui.navigation.Destinations
import dev.ch8n.common.utils.DevelopmentPreview


@Composable
fun PreviewBrowserScreen(
    componentContext: DefaultComponentContext
) {
    val controller = remember {
        BrowserController(
            componentContext = componentContext,
            navigateTo = {},
            onBack = {}
        )
    }
    DevelopmentPreview { isDark ->
        BrowserScreen(controller, {})
    }
}


@Composable
fun BrowserScreen(
    controller: BrowserController,
    onSettingsClicked: () -> Unit
) {

    val tags by controller.tag.collectAsState()
    val (selectedTag, setSelectedTag) = remember { mutableStateOf(Tags.New) }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        ToolbarBrowser(
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
                text = "Complete UI",
                color = MaterialTheme.colors.onSurface,
                modifier = Modifier.padding(horizontal = 24.dp),
                style = MaterialTheme.typography.h3
            )

            Spacer(Modifier.size(18.dp))


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
    selectedTag: Tags,
    updatedSelectedTag: (updatedTag: Tags) -> Unit,
    saveTag: (updatedTag: Tags) -> Unit,
    onDeleteTag: (tag: Tags) -> Unit,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(0.6f),
            value = selectedTag.name,
            onValueChange = {
                updatedSelectedTag.invoke(selectedTag.copy(name = it))
            },
            shape = MaterialTheme.shapes.large,
            singleLine = true,
            label = {
                Text("Tag Name")
            },
            textStyle = MaterialTheme.typography.subtitle1,
            colors = TextFieldDefaults.textFieldColors(
                textColor = MaterialTheme.colors.onSurface
            )
        )

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
                        onClick = {
                            // TODO update color
                            updatedSelectedTag.invoke(selectedTag)
                        }
                    ),
                tint = MaterialTheme.colors.onSurface,
            )

            Icon(
                painter = painterResource(R.drawable.delete),
                contentDescription = "",
                modifier = Modifier
                    .size(20.dp)
                    .clickable(
                        onClick = {
                            onDeleteTag.invoke(selectedTag)
                        }
                    ),
                tint = MaterialTheme.colors.onSurface,
            )

            Icon(
                painter = painterResource(R.drawable.save),
                contentDescription = "",
                modifier = Modifier
                    .size(20.dp)
                    .clickable(
                        onClick = {
                            saveTag.invoke(selectedTag)
                        }
                    ),
                tint = MaterialTheme.colors.onSurface,
            )
        }
    }
}

@Composable
private fun ToolbarBrowser(
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
                text = "WIP Browser Preview",
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