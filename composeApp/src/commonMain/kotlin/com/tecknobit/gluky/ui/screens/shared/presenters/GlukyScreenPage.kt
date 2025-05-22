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

@ScreenCoordinator
abstract class GlukyScreenPage<V : EquinoxViewModel>(
    viewModel: V,
    private val useResponsiveWidth: Boolean = true,
    private val title: StringResource? = null,
) : EquinoxScreen<V>(
    viewModel = viewModel
) {

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
                    ScreenPageContent()
                }
            }
        }
    }

    @Composable
    protected abstract fun ColumnScope.ScreenPageContent()

    @Composable
    @NonRestartableComposable
    protected open fun FABContent() {
    }

}