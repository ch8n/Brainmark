package ui.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.jetbrains.lifecycle.LifecycleController
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import design.components.FlowLayout
import design.components.TagChip
import dev.ch8n.common.ui.controllers.TagManagerController
import dev.ch8n.common.ui.theme.StringRes
import dev.ch8n.common.utils.ColorsUtils
import dev.ch8n.common.utils.DesktopPreview


@OptIn(ExperimentalDecomposeApi::class)
fun main() = application {
    val lifecycle = LifecycleRegistry()
    val defaultComponentContext = DefaultComponentContext(lifecycle = lifecycle)
    Window(
        onCloseRequest = ::exitApplication,
        resizable = false,
        state = WindowState(size = DpSize(1200.dp, 800.dp))
    ) {
        val windowState = rememberWindowState()
        LifecycleController(lifecycle, windowState)
        PreviewTagManagerScreen(defaultComponentContext)
    }
}

@Composable
fun PreviewTagManagerScreen(componentContext: DefaultComponentContext) {
    val controller = remember {
        TagManagerController(
            componentContext = componentContext,
            navigateTo = {

            },
            onBack = {

            }
        )
    }
    DesktopPreview(
        isSplitView = false,
    ) {
        TagScreenManager(controller, onSettingsClicked = {})
    }
}


@Composable
fun TagScreenManager(
    controller: TagManagerController,
    onSettingsClicked: () -> Unit
) {

    val viewState by controller.state.collectAsState()
    val (isColorPickerShown, setColorPickerShown) = remember {
        mutableStateOf(false)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.surface)
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
                text = StringRes.TagManagerScreen.title_manage_your_tags,
                color = MaterialTheme.colors.onSurface,
                modifier = Modifier.padding(horizontal = 24.dp),
                style = MaterialTheme.typography.h3
            )

            Text(
                text = StringRes.TagManagerScreen.subtitle_manage_tags,
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
                onSaveTagClicked = controller::saveTag,
                onDeleteTagClicked = controller::deleteTag,
                onResetSelectedTag = controller::clearSelectedTag,
                onRandomColorPicked = {
                    controller.updateTagColor(ColorsUtils.randomColor)
                }
            )

            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
                    .height(1.dp)
                    .background(
                        shape = RoundedCornerShape(24.dp),
                        color = MaterialTheme.colors.onSurface
                    )
            )

            val tags by controller.getAllTags.collectAsState(emptyList())

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
            ) {
                Spacer(modifier = Modifier.size(16.dp))
                FlowLayout(
                    modifier = Modifier
                        .padding(start = 24.dp, end = 24.dp)
                        .fillMaxWidth()
                ) {
                    tags.forEach { tag ->
                        TagChip(
                            tag = tag,
                            modifier = Modifier
                                .padding(8.dp)
                                .height(35.dp),
                            onTagClicked = controller::selectTag
                        )
                    }
                }

                Spacer(
                    Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                )
            }
        }

        AnimatedVisibility(isColorPickerShown) {
            ColorPicker(
                modifier = Modifier
                    .fillMaxSize(0.8f)
                    .align(Alignment.Center),
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

        Column(
            modifier = Modifier.fillMaxWidth(0.6f)
        ) {
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth(),
                value = tagName,
                onValueChange = onTagNameUpdated,
                shape = MaterialTheme.shapes.large,
                singleLine = true,
                maxLines = 1,
                label = {
                    Text(StringRes.TagManagerScreen.edit_tag_name)
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
                            Icon(
                                painter = painterResource("images/app_logo.xml"),
                                contentDescription = "",
                                modifier = Modifier
                                    .size(24.dp)
                                    .clickable(onClick = onResetSelectedTag),
                                tint = MaterialTheme.colors.onSurface,
                            )
                        }
                    }
                },
                enabled = !isLoading,
                isError = error.isNotEmpty(),
                keyboardActions = KeyboardActions(
                    onDone = { onSaveTagClicked.invoke() },

                    )
            )

            Text(
                text = error,
                style = MaterialTheme.typography.subtitle1,
                color = Color.Red,
                modifier = Modifier
                    .padding(start = 8.dp, top = 4.dp)
                    .fillMaxWidth(),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Icon(
                painter = painterResource("images/app_logo.xml"),
                contentDescription = "",
                modifier = Modifier
                    .size(20.dp)
                    .combinedClickable(
                        onClick = onRandomColorPicked,
                        onLongClick = onColorPickerClicked,
                        enabled = !isLoading
                    ),
                tint = tagColor,
            )

//            Icon(
//                painter = painterResource("images/app_logo.xml"),
//                contentDescription = "",
//                modifier = Modifier
//                    .size(20.dp)
//                    .clickable(
//                        onClick = onDeleteTagClicked,
//                        enabled = !isLoading
//                    ),
//                tint = MaterialTheme.colors.onSurface,
//            )

//            Icon(
//                painter = painterResource("images/app_logo.xml"),
//                contentDescription = "",
//                modifier = Modifier
//                    .size(20.dp)
//                    .clickable(
//                        onClick = onSaveTagClicked,
//                        enabled = !isLoading
//                    ),
//                tint = MaterialTheme.colors.onSurface,
//            )
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
//            AsyncImage(
//                model = R.drawable.brain,
//                modifier = Modifier.size(40.dp),
//                contentDescription = "",
//                contentScale = ContentScale.Fit,
//                colorFilter = ColorFilter.tint(
//                    color = MaterialTheme.colors.onSurface
//                )
//            )

            Text(
                text = StringRes.TagManagerScreen.toolbar_tag_manager,
                style = MaterialTheme.typography.h1,
                color = MaterialTheme.colors.onSurface
            )
        }


//        AsyncImage(
//            model = R.drawable.settings,
//            modifier = Modifier
//                .size(24.dp)
//                .clickable { onSettingsClicked.invoke() },
//            contentDescription = "",
//            contentScale = ContentScale.Fit,
//            colorFilter = ColorFilter.tint(
//                color = MaterialTheme.colors.onSurface
//            )
//        )

    }

}