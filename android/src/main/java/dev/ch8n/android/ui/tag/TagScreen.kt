package dev.ch8n.android.ui.tag

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.ch8n.common.ui.controllers.TagScreenController
import dev.ch8n.common.ui.navigation.AppNavigation


@Composable
fun TagScreen(controller: TagScreenController, navigation: AppNavigation) {
    val tag by controller.tag.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
    ) {
        Button(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(16.dp),
            onClick = {

            }
        ) {
            Text("goto Home Screen")
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .verticalScroll(state = rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Text(
                text = "Create Tag",
                fontSize = 46.sp,
                color = Color.White,
                modifier = Modifier
                    .padding(top = 78.dp)
            )

            TextField(
                value = tag.name,
                onValueChange = {
                    controller.onTagTextChange(it)
                },
                label = {
                    Text("Tag", color = Color.White)
                },
            )

            Button(
                onClick = {
                    controller.createNewTag(it)
                }
            ) {
                Text("Save tag", color = Color.White)
            }
        }
    }
}