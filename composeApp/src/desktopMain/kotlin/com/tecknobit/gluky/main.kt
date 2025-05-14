package com.tecknobit.gluky

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Gluky",
    ) {
        App()
    }
}