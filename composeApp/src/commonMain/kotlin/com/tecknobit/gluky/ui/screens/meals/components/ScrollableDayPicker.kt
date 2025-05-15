package com.tecknobit.gluky.ui.screens.meals.components

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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.LastBaseline
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tecknobit.equinoxcompose.utilities.ResponsiveClass.COMPACT_CONTENT
import com.tecknobit.equinoxcompose.utilities.ResponsiveClass.MEDIUM_EXPANDED_CONTENT
import com.tecknobit.equinoxcompose.utilities.ResponsiveClassComponent
import com.tecknobit.equinoxcore.annotations.FutureEquinoxApi
import com.tecknobit.equinoxcore.time.TimeFormatter.toDateString
import com.tecknobit.equinoxcore.time.TimeFormatter.toLocalDate
import com.tecknobit.gluky.ui.screens.meals.presentation.MealsScreenViewModel
import com.tecknobit.gluky.ui.theme.AppTypography
import gluky.composeapp.generated.resources.Res
import gluky.composeapp.generated.resources.fri
import gluky.composeapp.generated.resources.mon
import gluky.composeapp.generated.resources.sat
import gluky.composeapp.generated.resources.sun
import gluky.composeapp.generated.resources.thu
import gluky.composeapp.generated.resources.tue
import gluky.composeapp.generated.resources.wed
import kotlinx.coroutines.launch
import kotlinx.datetime.DayOfWeek
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import kotlin.math.absoluteValue
import kotlin.math.min

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@ResponsiveClassComponent(
    classes = [MEDIUM_EXPANDED_CONTENT, COMPACT_CONTENT]
)
fun ScrollableDayPicker(
    viewModel: MealsScreenViewModel,
    day: Long,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary)
    ) {
        val state = rememberPagerState(
            initialPage = 5,
            pageCount = { 10 }
        )
        PickerControls(
            state = state,
            panel = {
                PickerPanel(
                    state = state,
                    viewModel = viewModel,
                    day = day
                )
            }
        )
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.70f)
                .align(Alignment.BottomCenter),
            shape = BottomSheetDefaults.ExpandedShape,
            elevation = CardDefaults.cardElevation(
                defaultElevation = 6.dp
            )
        ) {
            MealDay(
                viewModel = viewModel
            )
        }
    }
}

@Composable
private fun PickerControls(
    state: PagerState,
    panel: @Composable () -> Unit,
) {
    val scope = rememberCoroutineScope()
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
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
        }
        Column(
            modifier = Modifier
                .weight(5f)
        ) {
            panel()
        }
        Column(
            modifier = Modifier
                .weight(1f)
        ) {
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
}

@Composable
private fun PickerPanel(
    state: PagerState,
    viewModel: MealsScreenViewModel,
    day: Long,
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
                day = day
            )
        }
    }
}

@Composable
private fun DayIndicator(
    day: Long,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        Text(
            modifier = Modifier
                .alignBy(
                    alignmentLine = LastBaseline
                ),
            text = day.toDateString(
                pattern = "dd"
            ),
            style = AppTypography.displayLarge,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        Text(
            modifier = Modifier
                .alignBy(
                    alignmentLine = LastBaseline
                ),
            text = day.toDateString(
                pattern = "MM"
            ),
            style = AppTypography.labelLarge,
            color = Color.White
        )
    }
    Text(
        text = stringResource(day.asDayOfWeek()),
        style = AppTypography.titleLarge,
        color = Color.White
    )
}

@FutureEquinoxApi(
    protoBehavior = """
        Actually is implemented to return just in for languages but will be extended to all the 
        languages supported by Equinox
    """,
    releaseVersion = "1.1.3",
    additionalNotes = """
        - Warn about is temporary method until the official kotlinx's one will be released
        - Check whether the best name is this or something like toDayOfWeek or anyone else
        - Check if overload this method with the @Composable one to return directly the day as String
        - To allow to return uppercase and lowercase 
    """
)
private fun Long.asDayOfWeek(): StringResource {
    val dayOfWeek = this.toLocalDate().dayOfWeek
    return when (dayOfWeek) {
        DayOfWeek.MONDAY -> Res.string.mon
        DayOfWeek.TUESDAY -> Res.string.tue
        DayOfWeek.WEDNESDAY -> Res.string.wed
        DayOfWeek.THURSDAY -> Res.string.thu
        DayOfWeek.FRIDAY -> Res.string.fri
        DayOfWeek.SATURDAY -> Res.string.sat
        else -> Res.string.sun
    }
}