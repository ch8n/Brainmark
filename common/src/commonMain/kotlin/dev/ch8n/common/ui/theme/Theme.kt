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
        subtitle1 = TextStyle(
            fontFamily = PoppinsFontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 12.sp
        )
    )

/**
 * Colors
 */

private val white1 = Color(0xffF5F5F5)
val white2 = Color(0xffF8F8F8)
private val white3 = Color(0xffEFEFEF)
val black1 = Color(0xff000000)
private val black2 = Color(0xff181818)
private val gray1 = Color(0xff575757)
private val gray2 = Color(0xff373333)

/**
 * Color palettes
 */
private val LightColors = lightColors(
    surface = white1,
    onSurface = gray1,
    background = black1,
    onBackground = white2,
    secondary = white3,
    onSecondary = black2,
    secondaryVariant = black2
)

private val DarkColors = darkColors(
    surface = black2,
    onSurface = white3,
    background = black1,
    onBackground = white2,
    secondary = gray2,
    onSecondary = white3,
    secondaryVariant = white3
)
