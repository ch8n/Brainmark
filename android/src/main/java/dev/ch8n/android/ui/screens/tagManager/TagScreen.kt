package dev.ch8n.android.ui.screens.tagManager

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.arkivanov.decompose.DefaultComponentContext
import dev.ch8n.android.R
import dev.ch8n.android.ui.components.BottomNavbar
import dev.ch8n.common.data.model.Tags
import dev.ch8n.common.domain.usecases.CreateTagUseCase
import dev.ch8n.common.ui.controllers.TagScreenController
import dev.ch8n.common.ui.navigation.Destinations
import dev.ch8n.common.utils.DevelopmentPreview

@Composable
fun PreviewTagManagerScreen(
    componentContext: DefaultComponentContext
) {
    val controller = remember {
        TagScreenController(
            componentContext = componentContext,
            navigateTo = {},
            onBack = {

            }
        )
    }
    DevelopmentPreview { isDark ->
        TagScreenManager(controller)
    }
}


@Composable
fun TagScreenManager(
    controller: TagScreenController
) {

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        ToolbarTagManager(
            modifier = Modifier
                .padding(top = 36.dp, start = 16.dp, end = 16.dp)
                .fillMaxWidth()
                .align(Alignment.TopCenter),
            onSettingsClicked = {

            }
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
                modifier = Modifier
                    .padding(horizontal = 24.dp)
                    .fillMaxWidth(),
                onDeleteTag = {},
                onSaveTag = {}
            )

            Spacer(Modifier.size(40.dp))

            Text(
                text = "TODO add tags in flow layout.. https://google.github.io/accompanist/flowlayout/",
                modifier = Modifier.padding(24.dp)
            )

            Spacer(Modifier.size(100.dp))
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
                controller.navigateTo(Destinations.Bookmark)
            },
            onNewBookmarkClicked = {}
        )
    }

}


@Composable
fun CreateTag(
    modifier: Modifier,
    tag: Tags = Tags.New,
    onSaveTag: (tag: Tags) -> Unit,
    onDeleteTag: (tag: Tags) -> Unit,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        val (updatedTag, setUpdatedTag) = remember { mutableStateOf(tag) }

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(0.6f),
            value = updatedTag.name,
            onValueChange = {
                setUpdatedTag.invoke(updatedTag.copy(name = it))
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
                            onSaveTag.invoke(tag)
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
                            onDeleteTag.invoke(tag)
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
                            onSaveTag(tag)
                        }
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