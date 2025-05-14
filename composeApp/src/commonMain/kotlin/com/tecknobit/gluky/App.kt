package com.tecknobit.gluky

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import gluky.composeapp.generated.resources.Res
import gluky.composeapp.generated.resources.comicneue
import gluky.composeapp.generated.resources.fredoka
import org.jetbrains.compose.resources.Font

/**
 * `displayFontFamily` the Gluky's font family
 */
lateinit var displayFontFamily: FontFamily

/**
 * `bodyFontFamily` the Gluky's body font family
 */
lateinit var bodyFontFamily: FontFamily

@Composable
fun App() {
    displayFontFamily = FontFamily(Font(Res.font.fredoka))
    bodyFontFamily = FontFamily(Font(Res.font.comicneue))
}