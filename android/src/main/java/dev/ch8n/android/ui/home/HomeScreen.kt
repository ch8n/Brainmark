package dev.ch8n.android.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import dev.ch8n.android.ui.components.*
import dev.ch8n.common.data.model.Bookmark
import dev.ch8n.common.ui.controllers.HomeScreenController
import dev.ch8n.common.utils.DevelopmentPreview

@Composable
fun HomeScreen(
    controller: HomeScreenController
) {

    DevelopmentPreview { isDark ->
        BottomNavbar(
            modifier = Modifier
                .padding(24.dp)
                .width(240.dp)
        )
    }

}