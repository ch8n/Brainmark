package design.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout

@Composable
fun FlowLayout(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    val measurePolicy = flowLayoutMeasurePolicy()
    Layout(
        measurePolicy = measurePolicy,
        content = content,
        modifier = modifier
    )
}