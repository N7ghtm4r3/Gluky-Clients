@file:OptIn(ExperimentalMaterial3Api::class)

package com.tecknobit.gluky.ui.screens.analyses.presentation

import androidx.compose.material3.DateRangePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.ExperimentalComposeApi
import androidx.lifecycle.viewModelScope
import com.dokar.sonner.ToasterState
import com.tecknobit.equinoxcompose.session.sessionflow.SessionFlowState
import com.tecknobit.equinoxcompose.viewmodels.EquinoxViewModel
import com.tecknobit.equinoxcore.annotations.Validator
import com.tecknobit.equinoxcore.time.TimeFormatter
import com.tecknobit.gluky.helpers.KReviewer
import com.tecknobit.gluky.ui.screens.analyses.data.GlycemiaPoint
import com.tecknobit.gluky.ui.screens.analyses.data.GlycemicTrendData
import com.tecknobit.gluky.ui.screens.analyses.data.GlycemicTrendDataContainer
import com.tecknobit.gluky.ui.screens.shared.presentations.ToastsLauncher
import com.tecknobit.glukycore.enums.GlycemicTrendGroupingDay
import com.tecknobit.glukycore.enums.GlycemicTrendLabelType.COMPUTE_MONTH
import com.tecknobit.glukycore.enums.GlycemicTrendPeriod
import com.tecknobit.glukycore.enums.GlycemicTrendPeriod.ONE_MONTH
import gluky.composeapp.generated.resources.Res
import gluky.composeapp.generated.resources.open
import gluky.composeapp.generated.resources.report_created
import gluky.composeapp.generated.resources.wrong_custom_range
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.random.Random

class AnalysesScreenViewModel : EquinoxViewModel(
    snackbarHostState = SnackbarHostState()
), ToastsLauncher {

    @OptIn(ExperimentalComposeApi::class)
    lateinit var sessionFlowState: SessionFlowState

    override lateinit var toasterState: ToasterState

    override var scope: CoroutineScope = viewModelScope

    private val _glycemicTrendData = MutableStateFlow<GlycemicTrendDataContainer?>(
        value = null
    )
    val glycemicTrendData = _glycemicTrendData.asStateFlow()

    private val _glycemicTrendPeriod = MutableStateFlow(
        value = ONE_MONTH
    )
    val glycemicTrendPeriod = _glycemicTrendPeriod.asStateFlow()

    private val _glycemicTrendGroupingDay = MutableStateFlow<GlycemicTrendGroupingDay?>(
        value = null
    )
    val glycemicTrendGroupingDay = _glycemicTrendGroupingDay.asStateFlow()

    private val _creatingReport = MutableStateFlow(
        value = false
    )
    val creatingReport = _creatingReport.asStateFlow()

    fun retrieveGlycemiaTrendData(
        from: Long? = null,
        to: Long? = null,
    ) {
        // TODO: TO MAKE THE REQUEST AND APPLYING FILTERS
        viewModelScope.launch {
            delay(2000)
            _glycemicTrendData.value = if (false)
                GlycemicTrendDataContainer()
            else {
                GlycemicTrendDataContainer(
                    breakfast = GlycemicTrendData(
                        labelType = COMPUTE_MONTH,
                        higherGlycemia = GlycemiaPoint(
                            date = TimeFormatter.currentTimestamp(),
                            value = Random.nextInt(100).toDouble(),
                        ),
                        lowerGlycemia = GlycemiaPoint(
                            date = TimeFormatter.currentTimestamp(),
                            value = Random.nextInt(100).toDouble(),
                        ),
                        averageGlycemia = GlycemiaPoint(
                            date = TimeFormatter.currentTimestamp(),
                            value = Random.nextInt(100).toDouble(),
                        ),
                        firstSet = listOf(
                                GlycemiaPoint(
                                    date = TimeFormatter.currentTimestamp(),
                                    value = Random.nextInt(100).toDouble(),
                                    ),
                            GlycemiaPoint(
                                date = TimeFormatter.currentTimestamp(),
                                value = Random.nextInt(100).toDouble(),

                                )
                        ),
                        secondSet = listOf(
                            GlycemiaPoint(
                                date = 1750780858000,
                                value = Random.nextInt(100).toDouble(),
                            ),
                            GlycemiaPoint(
                                date = 1750780858000,
                                value = Random.nextInt(100).toDouble()
                            )
                        ),
                        thirdSet = listOf(
                            GlycemiaPoint(
                                date = 1753372858000,
                                value = Random.nextInt(100).toDouble(),
                            ),
                            GlycemiaPoint(
                                date = 1753372858000,
                                value = Random.nextInt(100).toDouble()
                            )
                        ),
                        fourthSet = listOf(
                            GlycemiaPoint(
                                date = 1756051258000,
                                value = Random.nextInt(100).toDouble(),
                            ),
                            GlycemiaPoint(
                                date = 1756051258000,
                                value = Random.nextInt(100).toDouble()
                            )
                        )
                    ),
                    morningSnack = GlycemicTrendData(
                        labelType = COMPUTE_MONTH,
                        higherGlycemia = GlycemiaPoint(
                            date = TimeFormatter.currentTimestamp(),
                            value = Random.nextInt(100).toDouble(),
                        ),
                        lowerGlycemia = GlycemiaPoint(
                            date = TimeFormatter.currentTimestamp(),
                            value = Random.nextInt(100).toDouble(),
                        ),
                        averageGlycemia = GlycemiaPoint(
                            date = TimeFormatter.currentTimestamp(),
                            value = Random.nextInt(100).toDouble(),
                        ),
                        firstSet = listOf(
                            GlycemiaPoint(
                                date = TimeFormatter.currentTimestamp(),
                                value = Random.nextInt(100).toDouble(),
                            ),
                            GlycemiaPoint(
                                date = TimeFormatter.currentTimestamp(),
                                value = Random.nextInt(100).toDouble(),

                                )
                        ),
                        secondSet = listOf(
                            GlycemiaPoint(
                                date = 1750780858000,
                                value = Random.nextInt(100).toDouble(),
                            ),
                            GlycemiaPoint(
                                date = 1750780858000,
                                value = Random.nextInt(100).toDouble()
                            )
                        ),
                        thirdSet = listOf(
                            GlycemiaPoint(
                                date = 1753372858000,
                                value = Random.nextInt(100).toDouble(),
                            ),
                            GlycemiaPoint(
                                date = 1753372858000,
                                value = Random.nextInt(100).toDouble()
                            )
                        ),
                        fourthSet = listOf(
                            GlycemiaPoint(
                                date = 1756051258000,
                                value = Random.nextInt(100).toDouble(),
                            ),
                            GlycemiaPoint(
                                date = 1756051258000,
                                value = Random.nextInt(100).toDouble()
                            )
                        )
                    ),
                    afternoonSnack = GlycemicTrendData(
                        labelType = COMPUTE_MONTH,
                        higherGlycemia = GlycemiaPoint(
                            date = TimeFormatter.currentTimestamp(),
                            value = Random.nextInt(100).toDouble(),
                        ),
                        lowerGlycemia = GlycemiaPoint(
                            date = TimeFormatter.currentTimestamp(),
                            value = Random.nextInt(100).toDouble(),
                        ),
                        averageGlycemia = GlycemiaPoint(
                            date = TimeFormatter.currentTimestamp(),
                            value = Random.nextInt(100).toDouble(),
                        ),
                        firstSet = listOf(
                            GlycemiaPoint(
                                date = TimeFormatter.currentTimestamp(),
                                value = Random.nextInt(100).toDouble(),
                            ),
                            GlycemiaPoint(
                                date = TimeFormatter.currentTimestamp(),
                                value = Random.nextInt(100).toDouble(),

                                )
                        ),
                        secondSet = listOf(
                            GlycemiaPoint(
                                date = 1750780858000,
                                value = Random.nextInt(100).toDouble(),
                            ),
                            GlycemiaPoint(
                                date = 1750780858000,
                                value = Random.nextInt(100).toDouble()
                            )
                        ),
                        thirdSet = listOf(
                            GlycemiaPoint(
                                date = 1753372858000,
                                value = Random.nextInt(100).toDouble(),
                            ),
                            GlycemiaPoint(
                                date = 1753372858000,
                                value = Random.nextInt(100).toDouble()
                            )
                        ),
                        fourthSet = listOf(
                            GlycemiaPoint(
                                date = 1756051258000,
                                value = Random.nextInt(100).toDouble(),
                            ),
                            GlycemiaPoint(
                                date = 1756051258000,
                                value = Random.nextInt(100).toDouble()
                            )
                        )
                    ),
                    dinner = GlycemicTrendData(
                        labelType = COMPUTE_MONTH,
                        higherGlycemia = GlycemiaPoint(
                            date = TimeFormatter.currentTimestamp(),
                            value = Random.nextInt(100).toDouble(),
                        ),
                        lowerGlycemia = GlycemiaPoint(
                            date = TimeFormatter.currentTimestamp(),
                            value = Random.nextInt(100).toDouble(),
                        ),
                        averageGlycemia = GlycemiaPoint(
                            date = TimeFormatter.currentTimestamp(),
                            value = Random.nextInt(100).toDouble(),
                        ),
                        firstSet = listOf(
                            GlycemiaPoint(
                                date = TimeFormatter.currentTimestamp(),
                                value = Random.nextInt(100).toDouble(),
                            ),
                            GlycemiaPoint(
                                date = TimeFormatter.currentTimestamp(),
                                value = Random.nextInt(100).toDouble(),

                                )
                        ),
                        secondSet = listOf(
                            GlycemiaPoint(
                                date = 1750780858000,
                                value = Random.nextInt(100).toDouble(),
                            ),
                            GlycemiaPoint(
                                date = 1750780858000,
                                value = Random.nextInt(100).toDouble()
                            )
                        ),
                        thirdSet = listOf(
                            GlycemiaPoint(
                                date = 1753372858000,
                                value = Random.nextInt(100).toDouble(),
                            ),
                            GlycemiaPoint(
                                date = 1753372858000,
                                value = Random.nextInt(100).toDouble()
                            )
                        ),
                        fourthSet = listOf(
                            GlycemiaPoint(
                                date = 1756051258000,
                                value = Random.nextInt(100).toDouble(),
                            ),
                            GlycemiaPoint(
                                date = 1756051258000,
                                value = Random.nextInt(100).toDouble()
                            )
                        )
                    ),

                    basalInsulin = GlycemicTrendData(
                        labelType = COMPUTE_MONTH,
                        higherGlycemia = GlycemiaPoint(
                            date = TimeFormatter.currentTimestamp(),
                            value = Random.nextInt(100).toDouble(),
                        ),
                        lowerGlycemia = GlycemiaPoint(
                            date = TimeFormatter.currentTimestamp(),
                            value = Random.nextInt(100).toDouble(),
                        ),
                        averageGlycemia = GlycemiaPoint(
                            date = TimeFormatter.currentTimestamp(),
                            value = Random.nextInt(100).toDouble(),
                        ),
                        firstSet = listOf(
                            GlycemiaPoint(
                                date = TimeFormatter.currentTimestamp(),
                                value = Random.nextInt(100).toDouble(),
                            ),
                            GlycemiaPoint(
                                date = TimeFormatter.currentTimestamp(),
                                value = Random.nextInt(100).toDouble(),

                                )
                        ),
                        secondSet = listOf(
                            GlycemiaPoint(
                                date = 1750780858000,
                                value = Random.nextInt(100).toDouble(),
                            ),
                            GlycemiaPoint(
                                date = 1750780858000,
                                value = Random.nextInt(100).toDouble()
                            )
                        ),
                        thirdSet = listOf(
                            GlycemiaPoint(
                                date = 1753372858000,
                                value = Random.nextInt(100).toDouble(),
                            ),
                            GlycemiaPoint(
                                date = 1753372858000,
                                value = Random.nextInt(100).toDouble()
                            )
                        ),
                        fourthSet = listOf(
                            GlycemiaPoint(
                                date = 1756051258000,
                                value = Random.nextInt(100).toDouble(),
                            ),
                            GlycemiaPoint(
                                date = 1756051258000,
                                value = Random.nextInt(100).toDouble()
                            )
                        )
                    ),
                )
            }
        }
    }

    fun selectGlycemicGroupingDay(
        groupingDay: GlycemicTrendGroupingDay,
    ) {
        _glycemicTrendGroupingDay.value = groupingDay
        retrieveGlycemiaTrendData()
    }

    fun selectGlycemicTrendPeriod(
        period: GlycemicTrendPeriod,
    ) {
        _glycemicTrendPeriod.value = period
        retrieveGlycemiaTrendData()
    }

    fun applyCustomTrendPeriod(
        state: DateRangePickerState,
        allowedPeriod: String,
        onApply: () -> Unit,
    ) {
        if (!isCustomPeriodValid(state)) {
            toastError(
                error = Res.string.wrong_custom_range,
                allowedPeriod
            )
            return
        }
        retrieveGlycemiaTrendData(
            from = state.selectedStartDateMillis,
            to = state.selectedEndDateMillis
        )
        onApply()
    }

    @Validator
    private fun isCustomPeriodValid(
        state: DateRangePickerState,
    ): Boolean {
        val initialDate = state.selectedStartDateMillis ?: 0
        val endDate = state.selectedEndDateMillis ?: 0
        return ((endDate - initialDate) <= _glycemicTrendPeriod.value.millis)
    }

    fun createReport(
        onCreated: () -> Unit,
    ) {
        // TODO: MAKE THE REQUEST THEN DOWNLOAD FROM THE URL RETURNED
        _creatingReport.value = true
        viewModelScope.launch {
            delay(2000L) // TODO: TO REMOVE
            _creatingReport.value = false
            onCreated()
            showSnackbarMessage(
                message = Res.string.report_created,
                actionLabel = Res.string.open,
                onActionPerformed = {
                    val kReviewer = KReviewer()
                    kReviewer.reviewInApp {
                        // TODO: OPEN THE FILE
                    }
                }
            )
        }
    }

}