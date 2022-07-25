package dev.ch8n.android.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import dev.ch8n.common.data.model.FlashCard
import dev.ch8n.common.ui.theme.black1
import dev.ch8n.common.ui.theme.white2
import dev.ch8n.common.utils.DevelopmentPreview

@Composable
fun PreviewFlashCard() {
    DevelopmentPreview { isDark ->
        FlashCard(
            flashCard = FlashCard.SAMPLE,
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth()
                .height(176.dp)
        )
    }
}

@Composable
fun FlashCard(
    modifier: Modifier,
    flashCard: FlashCard
) {
    Box(
        modifier = modifier
    ) {

        Box(
            modifier = Modifier
                .fillMaxSize()
                .offset((-2).dp, 2.dp)
                .background(
                    color = MaterialTheme.colors.onSecondary,
                    shape = MaterialTheme.shapes.medium
                )
        )


        // background
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(MaterialTheme.shapes.medium)
                .background(black1)
                .alpha(0.4f)
        ) {
            AsyncImage(
                model = flashCard.mainImage,
                modifier = Modifier.fillMaxSize(),
                contentDescription = "",
                contentScale = ContentScale.Crop
            )
        }

        // content
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(24.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = flashCard.question,
                style = MaterialTheme.typography.h3,
                color = white2,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center
            )
        }

        // next action
        Row(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = "Next",
                style = MaterialTheme.typography.subtitle1.copy(color = white2),
            )

            AsyncImage(
                model = dev.ch8n.android.R.drawable.next,
                modifier = Modifier.size(24.dp),
                contentDescription = "",
                contentScale = ContentScale.Fit,
            )
        }
    }
}
