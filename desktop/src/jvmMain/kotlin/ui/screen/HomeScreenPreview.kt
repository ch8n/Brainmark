package ui.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogState
import design.components.FlowLayout
import design.components.TagChip
import dev.ch8n.common.ui.controllers.TagManagerController
import dev.ch8n.common.utils.ColorsUtils
import dev.ch8n.common.utils.DevelopmentPreview
import kotlinx.coroutines.launch

@Composable
fun TagScreenPreview(
    controller: TagManagerController,
    onSettingsClicked: () -> Unit
) {
    DevelopmentPreview(
        previewModifier = Modifier
            .width(900.dp)
            .fillMaxHeight()
    ) { isDark ->
        TagScreenManager(
            controller = controller,
            onSettingsClicked = onSettingsClicked
        )
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
    val (isColorPickerShown, setColorPickerShown) = remember {
        mutableStateOf(false)
    }

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
                onColorPickerClicked = {
                    setColorPickerShown.invoke(true)
                },
                onSaveTagClicked = {
                    scope.launch {
                        controller.saveTag()
                    }
                },
                onDeleteTagClicked = {
                    scope.launch {
                        controller.deleteTag()
                    }
                },
                onResetSelectedTag = {
                    controller.clearSelectedTag()
                },
                onRandomColorPicked = {
                    val randomColorGroup = ColorsUtils.colors.shuffled().first()
                    val (name, color) = randomColorGroup.shuffled().first()
                    controller.updateTagColor(color)
                }
            )

            Spacer(Modifier.size(40.dp))

            FlowLayout(
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


        if (isColorPickerShown) {

            Dialog(
                onCloseRequest = {
                    setColorPickerShown.invoke(false)
                },
                state = remember { DialogState() }
            ) {
                ColorPicker(
                    modifier = Modifier.fillMaxSize(0.8f),
                    onClose = {
                        setColorPickerShown.invoke(false)
                    },
                    onColorSelected = {
                        controller.updateTagColor(it)
                        setColorPickerShown.invoke(false)
                    }
                )
            }
        }
    }

}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CreateTag(
    modifier: Modifier,
    tagName: String,
    tagColor: Color,
    isLoading: Boolean,
    error: String,
    onTagNameUpdated: (name: String) -> Unit,
    onColorPickerClicked: () -> Unit,
    onRandomColorPicked: () -> Unit,
    onSaveTagClicked: () -> Unit,
    onDeleteTagClicked: () -> Unit,
    onResetSelectedTag: () -> Unit,
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
                    } else {
                        if (tagName.isNotEmpty()) {
                            Button(
                                modifier = Modifier
                                    .size(24.dp),
                                onClick = onResetSelectedTag
                            ) {
                                Text("Close")
                            }
                        }
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
                    modifier = Modifier.padding(start = 8.dp, top = 4.dp),
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

            Button(
                modifier = Modifier
                    .size(20.dp)
                    .combinedClickable(
                        onClick = onRandomColorPicked,
                        onLongClick = onColorPickerClicked,
                        enabled = !isLoading
                    ),
                onClick = { },
            ) {
                Text("Color Picker")
            }


            Button(
                modifier = Modifier
                    .size(20.dp)
                    .clickable(
                        onClick = onDeleteTagClicked,
                        enabled = !isLoading
                    ),
                onClick = { },
            ) {
                Text("Delete")
            }


            Button(
                modifier = Modifier
                    .size(20.dp)
                    .clickable(
                        onClick = onSaveTagClicked,
                        enabled = !isLoading
                    ),
                onClick = { },
            ) {
                Text("Save")
            }
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
            Text(
                text = "Tag Manager",
                style = MaterialTheme.typography.h1,
                color = MaterialTheme.colors.onSurface
            )
        }

        Button(
            modifier = Modifier
                .size(24.dp),
            onClick = onSettingsClicked,
        ) {
            Text("Settings")
        }

    }

}