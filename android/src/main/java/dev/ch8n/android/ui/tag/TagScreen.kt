package dev.ch8n.android.ui.tag

import androidx.compose.runtime.Composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Checkbox
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.ch8n.common.data.model.Bookmark
import dev.ch8n.common.data.model.Tags

@Composable
fun TagScreen() {
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

            val (tag, setTag) = remember { mutableStateOf(Tags.EMPTY) }

            TextField(
                value = tag.name,
                onValueChange = {
                    setTag.invoke(tag.copy(name = it))
                },
                label = {
                    Text("Tag", color = Color.White)
                },
            )

            Button(
                onClick = {}
            ) {
                Text("Save tag", color = Color.White)
            }
        }
    }
}