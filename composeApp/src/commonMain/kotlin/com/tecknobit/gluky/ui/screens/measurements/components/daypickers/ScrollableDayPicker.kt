package com.tecknobit.gluky.ui.screens.measurements.components.daypickers

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import com.tecknobit.equinoxcompose.utilities.ResponsiveClass.COMPACT_CONTENT
import com.tecknobit.equinoxcompose.utilities.ResponsiveClass.MEDIUM_EXPANDED_CONTENT
import com.tecknobit.equinoxcompose.utilities.ResponsiveClassComponent
import com.tecknobit.gluky.ui.screens.measurements.presentation.MeasurementsScreenViewModel
import com.tecknobit.gluky.ui.screens.measurements.presentation.MeasurementsScreenViewModel.Companion.INITIAL_SELECTED_DAY
import com.tecknobit.gluky.ui.screens.measurements.presentation.MeasurementsScreenViewModel.Companion.MAX_LOADABLE_DAYS
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue
import kotlin.math.min

/**
 * Custom picker to select a day displayed on small and medium screen classes device
 *
 * @param viewModel The support viewmodel of the screen
 * @param currentDay The current selected day
 * @param content The content to display based on the selected day
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
@ResponsiveClassComponent(
    classes = [MEDIUM_EXPANDED_CONTENT, COMPACT_CONTENT]
)
fun ScrollableDayPicker(
    viewModel: MeasurementsScreenViewModel,
    currentDay: Long,
    content: @Composable () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary)
    ) {
        val state = rememberPagerState(
            initialPage = INITIAL_SELECTED_DAY,
            pageCount = { MAX_LOADABLE_DAYS }
        )
        PickerControls(
            state = state,
            panel = {
                PickerPanel(
                    state = state,
                    viewModel = viewModel,
                    currentDay = currentDay
                )
            }
        )
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.72f)
                .align(Alignment.BottomCenter),
            shape = BottomSheetDefaults.ExpandedShape,
            elevation = CardDefaults.cardElevation(
                defaultElevation = 6.dp
            ),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.background
            ),
            content = { content() }
        )
    }
}

/**
 * The controls of the [ScrollableDayPicker]
 *
 * @param state The state which manages the picker behavior
 * @param panel The content of the panel to display
 */
@Composable
private fun PickerControls(
    state: PagerState,
    panel: @Composable () -> Unit,
) {
    val scope = rememberCoroutineScope()
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        AnimatedVisibility(
            visible = state.canScrollBackward
        ) {
            IconButton(
                onClick = {
                    scope.launch {
                        state.animateScrollToPage(
                            page = state.settledPage - 1
                        )
                    }
                }
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBackIosNew,
                    contentDescription = null,
                    tint = Color.White
                )
            }
        }
        Column(
            modifier = Modifier
                .weight(1f)
        ) {
            panel()
        }
        AnimatedVisibility(
            visible = state.canScrollForward
        ) {
            IconButton(
                onClick = {
                    scope.launch {
                        state.animateScrollToPage(
                            page = state.settledPage + 1
                        )
                    }
                }
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                    contentDescription = null,
                    tint = Color.White
                )
            }
        }
    }
}

/**
 * Core component of the picker where the user can select the day with controls or scrolling
 *
 * @param state The state which manages the picker behavior
 * @param viewModel The support viewmodel of the screen
 * @param currentDay The current selected day
 */
@Composable
private fun PickerPanel(
    state: PagerState,
    viewModel: MeasurementsScreenViewModel,
    currentDay: Long,
) {
    HorizontalPager(
        state = state
    ) {
        LaunchedEffect(state.settledPage) {
            viewModel.computeDayValue(
                page = state.settledPage
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.3f)
                .graphicsLayer {
                    val pageOffset = state.getOffsetDistanceInPages(it)
                    val offScreenRight = pageOffset < 0f
                    val deg = 105f
                    val interpolated = FastOutLinearInEasing.transform(pageOffset.absoluteValue)
                    rotationY = min(interpolated * if (offScreenRight) deg else -deg, 90f)

                    transformOrigin = TransformOrigin(
                        pivotFractionX = if (offScreenRight) 0f else 1f,
                        pivotFractionY = .5f
                    )
                },
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            DayIndicator(
                dayInMillis = currentDay
            )
        }
    }
}