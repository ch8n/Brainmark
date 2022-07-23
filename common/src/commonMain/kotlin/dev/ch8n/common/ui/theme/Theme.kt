package dev.ch8n.common.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Shapes
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.Typography

@Composable
fun BrainMarkTheme(
    isDark: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colors = if (isDark) DarkColors else LightColors,
        shapes = Shapes,
        typography = typography,
        content = content
    )
}


/**
 * Shapes
 */
private val Shapes = Shapes(
    small = RoundedCornerShape(16.dp),
    medium = RoundedCornerShape(24.dp),
    large = RoundedCornerShape(36.dp),
)


/**
 * Colors
 */
private val Scorpion = Color(0xff575757)
private val WildSand = Color(0xffF5F5F5)
val Alto = Color(0xffF8F8F8)
private val Gallery = Color(0xffEFEFEF)
private val CodGray = Color(0xff181818)
private val MineShaft = Color(0xff373333)
val Black = Color(0xff000000)

/**
 * Typography
 */
expect val PoppinsFontFamily: FontFamily

private val typography: Typography
    get() = Typography(
        h1 = TextStyle(
            fontFamily = PoppinsFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 40.sp
        ),
        h2 = TextStyle(
            fontFamily = PoppinsFontFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 32.sp
        ),
        h3 = TextStyle(
            fontFamily = PoppinsFontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 24.sp
        ),
        h4 = TextStyle(
            fontFamily = PoppinsFontFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp
        ),
        body1 = TextStyle(
            fontFamily = PoppinsFontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 10.sp
        ),
        body2 = TextStyle(
            fontFamily = PoppinsFontFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 8.sp
        ),
    )

/***
 * Color palettes
 */
private val LightColors = lightColors(
    surface = WildSand,
    onSurface = Scorpion,
    background = Black,
    onBackground = Alto,
    secondary = Gallery,
    onSecondary = CodGray
)

private val DarkColors = darkColors(
    surface = CodGray,
    onSurface = Gallery,
    background = Black,
    onBackground = Alto,
    secondary = MineShaft,
    onSecondary = Gallery
)
