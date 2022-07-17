package dev.ch8n.android.design

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import dev.ch8n.android.R.drawable
import dev.ch8n.android.utils.parseColor
import dev.ch8n.common.data.model.Bookmark
import dev.ch8n.common.data.model.Tags
import dev.ch8n.common.domain.di.DomainInjector
import kotlinx.coroutines.flow.firstOrNull

@Composable
fun BookmarkCard(
    modifier: Modifier,
    bookmark: Bookmark,
) {

    val (tags, setTags) = remember { mutableStateOf(emptyList<Tags>()) }

    LaunchedEffect(bookmark) {
        val tagIds = mutableListOf(bookmark.primaryTagId)
        tagIds.addAll(bookmark.secondaryTagIds)
        val getAllTagsUseCase = DomainInjector.tagUseCase.getTagsByIdsUseCase
        val allTags = getAllTagsUseCase.invoke(tagIds).firstOrNull() ?: emptyList()
        // TODO fix remove sample
        val sampleTags = mutableListOf<Tags>()
        sampleTags.add(Tags.TAG_JAVA)
        sampleTags.add(Tags.TAG_KOTLIN)
        setTags.invoke(sampleTags)
    }

    val primaryTag = tags.firstOrNull()
    val secondaryTags = tags.drop(1)
    val primaryColor = primaryTag?.color?.parseColor() ?: Color.Gray
    val borderShape = RoundedCornerShape(16.dp)

    Box(
        modifier = modifier
            .background("#373333".parseColor())
            .clip(borderShape)
            .border(
                width = 2.dp,
                color = primaryColor,
                shape = borderShape
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp, start = 24.dp, end = 24.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(208.dp)
            ) {

                AsyncImage(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(borderShape)
                        .border(
                            width = 2.dp,
                            color = primaryColor,
                            shape = borderShape
                        ),
                    model = bookmark.meta.image,
                    contentDescription = null,
                    contentScale = ContentScale.Crop
                )
                val cardColor = remember { "#161515".parseColor().copy(alpha = 0.48f) }
                Box(
                    modifier = Modifier
                        .padding(24.dp)
                        .fillMaxSize()
                        .clip(borderShape)
                        .background(color = cardColor)
                ) {
                    Text(
                        text = bookmark.meta.title,
                        style = TextStyle(
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        ),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .padding(24.dp)
                            .align(Alignment.Center)
                    )
                }
            }

            Text(
                text = bookmark.meta.description,
                style = TextStyle(
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal
                ),
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 8.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = bookmark.meta.siteName,
                    style = TextStyle(
                        color = Color.Cyan,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        textDecoration = TextDecoration.Underline
                    ),
                )

                Text(
                    text = System.currentTimeMillis().toString(),
                    style = TextStyle(
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    ),
                )
            }


            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    if (primaryTag != null) {
                        Chip(
                            modifier = Modifier,
                            title = primaryTag.name,
                            borderColor = primaryColor
                        )
                    }

                    if (secondaryTags.isNotEmpty()) {
                        Chip(
                            modifier = Modifier,
                            title = "+${secondaryTags.size} more..",
                            borderColor = primaryColor
                        )
                    }
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    IconChip(
                        modifier = Modifier,
                        resourceId = drawable.clock,
                        borderColor = primaryColor
                    )

                    IconChip(
                        modifier = Modifier,
                        resourceId = drawable.edit,
                        borderColor = primaryColor
                    )
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(16.dp)
                .align(Alignment.BottomCenter)
                .background(primaryColor)
                .clip(RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp))
        )

    }
}
