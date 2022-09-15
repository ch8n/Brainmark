package dev.ch8n.android.ui.components

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier


@Composable
fun ScrollableColumn(
    modifier: Modifier = Modifier.fillMaxSize(),
    enabled: Boolean = true,
    flingBehavior: FlingBehavior? = null,
    reverseScrolling: Boolean = false,
    content: @Composable ColumnScope.(scrollState: ScrollState) -> Unit
) {
    val scrollState = rememberScrollState()
    Column(
        modifier = modifier
            .verticalScroll(
                state = scrollState,
                enabled = enabled,
                flingBehavior = flingBehavior,
                reverseScrolling = reverseScrolling
            ),
    ) {
        content.invoke(this, scrollState)
    }

}