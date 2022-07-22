package dev.ch8n.common.ui.theme

import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import dev.ch8n.brainmark.SharedRes

/**
 * Typography
 */
actual val PoppinsFontFamily: FontFamily = FontFamily(
    Font(SharedRes.fonts.Poppins.regular.fontResourceId, FontWeight.Normal),
    Font(SharedRes.fonts.Poppins.semiBold.fontResourceId, FontWeight.SemiBold),
    Font(SharedRes.fonts.Poppins.medium.fontResourceId, FontWeight.Medium),
    Font(SharedRes.fonts.Urbanist.semiBold.fontResourceId, FontWeight.Bold),
)
