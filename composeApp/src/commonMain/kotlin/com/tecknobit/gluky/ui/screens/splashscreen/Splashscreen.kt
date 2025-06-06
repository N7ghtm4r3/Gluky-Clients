package com.tecknobit.gluky.ui.screens.splashscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import com.tecknobit.equinoxcompose.session.screens.EquinoxNoModelScreen
import com.tecknobit.gluky.CheckForUpdatesAndLaunch
import com.tecknobit.gluky.ui.theme.AppTypography
import com.tecknobit.gluky.ui.theme.GlukyTheme
import gluky.composeapp.generated.resources.Res
import gluky.composeapp.generated.resources.app_name
import org.jetbrains.compose.resources.stringResource

/**
 * The [Splashscreen] class is used to retrieve and load the session data and enter into the
 * application's workflow
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see EquinoxNoModelScreen
 */
class Splashscreen : EquinoxNoModelScreen() {

    /**
     * Method used to arrange the content of the screen to display
     */
    @Composable
    override fun ArrangeScreenContent() {
        GlukyTheme {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.primary),
                contentAlignment = Alignment.Center
            ) {
                Column {
                    Text(
                        text = stringResource(Res.string.app_name),
                        style = AppTypography.displayLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "by Tecknobit",
                        color = Color.White
                    )
                }
            }
        }
        CheckForUpdatesAndLaunch()
    }

    /**
     * Method used to collect or instantiate the states of the screen
     */
    @Composable
    override fun CollectStates() {
    }

}