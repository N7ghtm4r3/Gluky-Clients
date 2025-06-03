package com.tecknobit.gluky

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import gluky.composeapp.generated.resources.Res
import gluky.composeapp.generated.resources.app_name
import gluky.composeapp.generated.resources.logo
import io.github.vinceglb.filekit.FileKit
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

fun main() = application {
    FileKit.init(
        appId = "Gluky"
    )
    Window(
        onCloseRequest = ::exitApplication,
        title = stringResource(Res.string.app_name),
        state = WindowState(
            placement = WindowPlacement.Maximized
        ),
        icon = painterResource(Res.drawable.logo)
    ) {
        App()
    }
}