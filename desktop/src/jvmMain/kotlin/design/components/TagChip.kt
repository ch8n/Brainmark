package design.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import dev.ch8n.common.data.model.Tags
import dev.ch8n.common.utils.DevelopmentPreview
import dev.ch8n.common.utils.toColor


@Composable
fun PreviewTagChip() {
    DevelopmentPreview { isDark ->
        TagChip(
            tag = Tags.TAG_KOTLIN,
            modifier = Modifier
                .padding(24.dp)
                .wrapContentWidth()
                .height(35.dp),
            onTagClicked = {

            }
        )
    }
}

@Composable
fun TagChip(
    modifier: Modifier,
    tag: Tags,
    onTagClicked: (tag: Tags) -> Unit,
) {
    Box(
        modifier = modifier.clickable {
            onTagClicked.invoke(tag)
        }
    ) {

        Row(
            modifier = Modifier
                .clip(MaterialTheme.shapes.large)
                .background(MaterialTheme.colors.surface)
                .border(
                    width = 2.dp,
                    color = MaterialTheme.colors.secondaryVariant,
                    shape = MaterialTheme.shapes.large
                )
                .padding(vertical = 8.dp, horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${tag.name}  |",
                style = MaterialTheme.typography.subtitle1,
                color = MaterialTheme.colors.onSurface
            )

            Box(
                modifier = Modifier
                    .padding(start = 8.dp)
                    .size(12.dp)
                    .offset(y = (-1).dp)
                    .background(tag.color.toColor(), CircleShape)
                    .border(1.5.dp, MaterialTheme.colors.onSurface, CircleShape)
            )
        }
    }

}
