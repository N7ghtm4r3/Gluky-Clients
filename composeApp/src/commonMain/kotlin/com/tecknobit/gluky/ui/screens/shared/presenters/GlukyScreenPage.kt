package com.tecknobit.gluky.ui.screens.shared.presenters

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ExperimentalComposeApi
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.tecknobit.equinoxcompose.annotations.ScreenCoordinator
import com.tecknobit.equinoxcompose.session.screens.EquinoxScreen
import com.tecknobit.equinoxcompose.utilities.responsiveMaxWidth
import com.tecknobit.equinoxcompose.viewmodels.EquinoxViewModel

@ScreenCoordinator
abstract class GlukyScreenPage<V : EquinoxViewModel>(
    viewModel: V,
    private val useResponsiveWidth: Boolean = true,
) : EquinoxScreen<V>(
    viewModel = viewModel
) {

    @OptIn(ExperimentalComposeApi::class)
    @Composable
    override fun ArrangeScreenContent() {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
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

    @Composable
    protected abstract fun ColumnScope.ScreenPageContent()

}