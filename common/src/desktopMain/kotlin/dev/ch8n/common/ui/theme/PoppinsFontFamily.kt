package dev.ch8n.common.ui.theme


import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.platform.Font
import dev.ch8n.brainmark.SharedRes

/**
 * Typography
 */
actual val PoppinsFontFamily: FontFamily = FontFamily(
    Font(SharedRes.fonts.Poppins.regular.file, FontWeight.Normal),
    Font(SharedRes.fonts.Poppins.semiBold.file, FontWeight.SemiBold),
    Font(SharedRes.fonts.Poppins.medium.file, FontWeight.Medium),
    Font(SharedRes.fonts.Urbanist.semiBold.file, FontWeight.Bold),
)
