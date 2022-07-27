package dev.ch8n.android.ui.screens.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.arkivanov.decompose.DefaultComponentContext
import dev.ch8n.android.R
import dev.ch8n.android.design.components.BottomNavbar
import dev.ch8n.android.design.components.ContinueBookmarkCard
import dev.ch8n.android.design.components.FlashCard
import dev.ch8n.android.design.components.RecommendedReadCard
import dev.ch8n.common.ui.controllers.HomeScreenController
import dev.ch8n.common.ui.navigation.Destinations
import dev.ch8n.common.utils.DevelopmentPreview


@Composable
fun PreviewHomeScreen(
    componentContext: DefaultComponentContext
) {
    val controller = remember {
        HomeScreenController(
            componentContext = componentContext,
            navigateTo = {},
            onBack = {}
        )
    }
    DevelopmentPreview { isDark ->
        HomeScreen(controller, onSettingsClicked = {})
    }
}


@Composable
fun HomeScreen(
    controller: HomeScreenController,
    onSettingsClicked: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        val bookmarks by controller.bookmarks.collectAsState()

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

            ContinueBookmarkCard(
                bookmark = bookmarks.first(),
                modifier = Modifier
                    .padding(horizontal = 24.dp)
                    .fillMaxWidth()
                    .height(176.dp)
            )

            Title("Reading Recommendation")

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
            ) {
                bookmarks.forEach { bookmark ->
                    RecommendedReadCard(
                        bookmark = bookmark,
                        modifier = Modifier
                            .padding(start = 24.dp)
                            .width(216.dp)
                            .height(240.dp),
                        onMenuClicked = {

                        }
                    )
                }
            }

            Title("Revision?")

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
            ) {
                controller.flashCardSample.forEach {
                    FlashCard(
                        flashCard = it,
                        modifier = Modifier
                            .padding(start = 24.dp)
                            .width(320.dp)
                            .height(176.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.size(100.dp))
        }

        BottomNavbar(
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.BottomCenter)
                .width(240.dp),
            onTagClicked = {
                controller.navigateTo(Destinations.TagManager)
            },
            onBookmarkClicked = {
                controller.navigateTo(Destinations.BookmarkBrowser)
            },
            onNewBookmarkClicked = {
                controller.navigateTo(Destinations.CreateBookmark)
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