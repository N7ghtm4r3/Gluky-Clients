package com.tecknobit.gluky.ui.screens.shared.presenters

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ExperimentalComposeApi
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.tecknobit.equinoxcompose.annotations.ScreenCoordinator
import com.tecknobit.equinoxcompose.session.screens.EquinoxScreen
import com.tecknobit.equinoxcompose.utilities.responsiveMaxWidth
import com.tecknobit.equinoxcompose.viewmodels.EquinoxViewModel
import com.tecknobit.gluky.ui.theme.AppTypography
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

/**
 * The [GlukyScreenTab] class defines the behavior of a screen tab displayed by the
 * [com.tecknobit.gluky.ui.screens.home.HomeScreen], and manages the arrangement of content for
 * each tab.
 *
 * This class is intended to be used as a base for implementing individual screen tabs.
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see EquinoxScreen
 */
@ScreenCoordinator
abstract class GlukyScreenTab<V : EquinoxViewModel>(
    viewModel: V,
    private val useResponsiveWidth: Boolean = true,
    private val title: StringResource? = null,
) : EquinoxScreen<V>(
    viewModel = viewModel
) {

    /**
     * Method used to arrange the content of the screen to display
     */
    @OptIn(ExperimentalComposeApi::class)
    @Composable
    override fun ArrangeScreenContent() {
        Scaffold(
            floatingActionButton = { FABContent() },
            snackbarHost = {
                SnackbarHost(
                    hostState = viewModel.snackbarHostState!!
                )
            }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                title?.let {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth(),
                        text = stringResource(title),
                        style = AppTypography.displaySmall
                    )
                }
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .then(
                            if (useResponsiveWidth)
                                Modifier.responsiveMaxWidth()
                            else
                                Modifier
                        )
                ) {
                    ScreenContent()
                }
            }
        }
    }

    /**
     * The content of the screen customized by each tab
     */
    @Composable
    protected abstract fun ColumnScope.ScreenContent()

    /**
     * The content displayed in the `Scaffold.floatingActionButton` section
     */
    @Composable
    @NonRestartableComposable
    protected open fun FABContent() {
    }

}