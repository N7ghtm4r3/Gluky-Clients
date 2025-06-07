package com.tecknobit.gluky

import androidx.compose.ui.window.ComposeUIViewController
import com.tecknobit.ametistaengine.AmetistaEngine

/**
 * Method to start the of `Glider` iOs application
 */
fun MainViewController() {
    AmetistaEngine.intake()
    ComposeUIViewController { App() }
}