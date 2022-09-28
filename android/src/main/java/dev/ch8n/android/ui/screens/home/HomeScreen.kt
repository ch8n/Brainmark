package dev.ch8n.android.ui.screens.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.arkivanov.decompose.DefaultComponentContext
import dev.ch8n.android.R
import dev.ch8n.android.design.components.BottomNavbar
import dev.ch8n.android.design.components.ContinueBookmarkCard
import dev.ch8n.android.design.components.RecommendedReadCard
import dev.ch8n.android.utils.toast
import dev.ch8n.common.data.model.Bookmark
import dev.ch8n.common.ui.controllers.HomeController
import dev.ch8n.common.ui.navigation.*
import dev.ch8n.common.utils.AndroidPreview


class AndroidHomeController(
    navController: NavController
) : HomeController(navController) {
    @Composable
    override fun Render() {
        val context = LocalContext.current
        HomeScreen(
            controller = this,
            onSettingsClicked = {
                "TODO".toast(context)
            }
        )
    }
}

@Composable
fun PreviewHomeScreen(
    componentContext: DefaultComponentContext
) {
    val context = LocalContext.current
    val controller = remember {
        AndroidHomeController(
            navController = EmptyNavController()
        )
    }
    AndroidPreview(
        isSplitView = false,
        isDark = true
    ) {
        HomeScreen(controller, onSettingsClicked = {
            "onSettingsClicked".toast(context)
        })
    }
}


@Composable
fun HomeScreen(
    controller: AndroidHomeController,
    onSettingsClicked: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.surface)
    ) {

        val lastReadBookmarks by controller.lastReadBookmarks.collectAsState()
        val readingRecommendations by controller.readingRecommendations.collectAsState()
        val revisionBookmarks by controller.revisionBookmarks.collectAsState()

        LaunchedEffect(Unit) {
            controller.getLastReadBookmarks()
            controller.getReadingRecommendations()
            controller.getRevisionBookmarks()
        }

        ToolbarHome(
            modifier = Modifier
                .padding(top = 36.dp, start = 16.dp, end = 16.dp)
                .fillMaxWidth(),
            onSettingsClicked = {
                onSettingsClicked.invoke()
            }
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 100.dp)
                .verticalScroll(rememberScrollState())
        ) {

            Spacer(modifier = Modifier.size(24.dp))

            Greet(name = "Chetan")

            Spacer(modifier = Modifier.size(12.dp))

            val lastRead = lastReadBookmarks.firstOrNull() ?: Bookmark.Empty

            AnimatedVisibility(
                visible = lastRead != Bookmark.Empty
            ) {
                ContinueBookmarkCard(
                    bookmark = lastRead,
                    modifier = Modifier
                        .padding(horizontal = 24.dp)
                        .fillMaxWidth()
                        .height(176.dp),
                    onClicked = {
                        controller.routeTo(PreviewBookmarkHomeDestination(it))
                    }
                )
            }

            AnimatedVisibility(
                visible = lastReadBookmarks.firstOrNull() == null
            ) {
                Box(
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)
                        .fillMaxWidth()
                        .height(150.dp)
                        .border(1.dp, MaterialTheme.colors.onSurface, MaterialTheme.shapes.large)
                ) {
                    Text(
                        text = "You haven't read anything.\nWhat are you waiting for?",
                        modifier = Modifier.padding(24.dp).align(Alignment.Center),
                        style = MaterialTheme.typography.h3,
                        color = MaterialTheme.colors.onSurface,
                        textAlign = TextAlign.Center
                    )
                }
            }

            Title("Reading Recommendation")

            AnimatedVisibility(
                visible = readingRecommendations.isEmpty()
            ) {
                Box(
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)
                        .fillMaxWidth()
                        .height(150.dp)
                        .border(1.dp, MaterialTheme.colors.onSurface, MaterialTheme.shapes.large)
                ) {
                    Text(
                        text = "You don't seem to have any bookmarks...",
                        modifier = Modifier.padding(24.dp).align(Alignment.Center),
                        style = MaterialTheme.typography.h3,
                        color = MaterialTheme.colors.onSurface,
                        textAlign = TextAlign.Center
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
            ) {
                readingRecommendations.forEach { bookmark ->
                    RecommendedReadCard(
                        bookmark = bookmark,
                        modifier = Modifier
                            .padding(start = 24.dp)
                            .width(216.dp)
                            .height(240.dp),
                        onMenuClicked = {

                        },
                        onClicked = {
                            controller.routeTo(PreviewBookmarkHomeDestination(it))
                        }
                    )
                }
            }

            Title("Revision?")

            AnimatedVisibility(
                visible = revisionBookmarks.isEmpty()
            ) {
                Box(
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)
                        .fillMaxWidth()
                        .height(150.dp)
                        .border(1.dp, MaterialTheme.colors.onSurface, MaterialTheme.shapes.large)
                ) {
                    Text(
                        text = "Don't forget to archive once you read a bookmark...",
                        modifier = Modifier.padding(24.dp).align(Alignment.Center),
                        style = MaterialTheme.typography.h3,
                        color = MaterialTheme.colors.onSurface,
                        textAlign = TextAlign.Center
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
            ) {
                revisionBookmarks.forEach { bookmark ->
                    RecommendedReadCard(
                        bookmark = bookmark,
                        modifier = Modifier
                            .padding(start = 24.dp)
                            .width(216.dp)
                            .height(240.dp),
                        onMenuClicked = {

                        },
                        onClicked = {
                            controller.routeTo(PreviewBookmarkHomeDestination(it))
                        }
                    )
                }
            }


            Spacer(modifier = Modifier.size(100.dp))
        }

        BottomNavbar(
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.BottomCenter)
                .width(200.dp),
            onTagClicked = {
                controller.routeTo(TagManagerDestination)
            },
            onBookmarkClicked = {
                controller.routeTo(BookmarksDestination)
            },
            onNewBookmarkClicked = {
                controller.routeTo(CreateBookmarksDestination())
            }
        )
    }

}

@Composable
fun Title(name: String) {
    Text(
        text = name,
        style = MaterialTheme.typography.h4,
        color = MaterialTheme.colors.onSurface,
        modifier = Modifier.padding(horizontal = 24.dp, vertical = 20.dp)
    )
}

@Composable
fun Greet(name: String) {
    val annotatedString = remember {
        buildAnnotatedString {
            withStyle(
                style = SpanStyle(
                    fontWeight = FontWeight.Normal,
                    fontSize = 24.sp
                )
            ) {
                append("Welcome, ")
            }
            withStyle(
                style = SpanStyle(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 24.sp
                )
            ) {
                append(name)
            }
            withStyle(
                style = SpanStyle(
                    fontWeight = FontWeight.Normal,
                    fontSize = 24.sp
                )
            ) {
                append(" 👋🏻")
            }
        }
    }

    Text(
        text = annotatedString,
        color = MaterialTheme.colors.onSurface,
        modifier = Modifier.padding(horizontal = 24.dp)
    )
}


@Composable
private fun ToolbarHome(
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
                text = "BrainMark",
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