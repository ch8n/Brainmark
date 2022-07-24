package dev.ch8n.android.ui.home

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.arkivanov.decompose.DefaultComponentContext
import dev.ch8n.common.ui.controllers.HomeScreenController
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
        HomeScreen(controller)
    }
}


@Composable
fun HomeScreen(
    controller: HomeScreenController
) {

    ToolbarHome()
}

@Composable
fun ToolbarHome(
    modifier: Modifier = Modifier
) {
    AsyncImage(
        model = dev.ch8n.android.R.drawable.brain,
        modifier = Modifier
            .padding(16.dp)
            .size(36.dp),
        contentDescription = "",
        contentScale = ContentScale.Fit,
    )
}