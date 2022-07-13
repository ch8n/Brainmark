package dev.ch8n.android.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.ch8n.common.ui.controllers.HomeScreenController
import dev.ch8n.common.ui.navigation.Destinations

@Composable
fun HomeScreen(
    controller: HomeScreenController
) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
    ) {

        Text(
            text = "Home Screen",
            fontSize = 46.sp,
            color = Color.White,
            modifier = Modifier.align(Alignment.Center)
        )


        Button(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            onClick = {
                controller.navigateTo(Destinations.Bookmark)
            }
        ) {
            Text("Add Bookmark")
        }
    }
}