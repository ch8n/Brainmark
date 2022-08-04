package design.components

import androidx.compose.ui.layout.MeasurePolicy

fun flowLayoutMeasurePolicy() = MeasurePolicy { measurables, constraints ->
    layout(constraints.maxWidth, constraints.maxHeight) {
        val placeables = measurables.map { measurable ->
            measurable.measure(constraints)
        }
        var yPos = 0
        var xPos = 0
        var maxY = 0
        placeables.forEach { placeable ->
            if (xPos + placeable.width >
                constraints.maxWidth
            ) {
                xPos = 0
                yPos += maxY
                maxY = 0
            }
            placeable.placeRelative(
                x = xPos,
                y = yPos
            )
            xPos += placeable.width
            if (maxY < placeable.height) {
                maxY = placeable.height
            }
        }
    }
}