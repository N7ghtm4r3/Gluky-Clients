package com.tecknobit.gluky.ui.screens.analyses.presentation

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.ExperimentalComposeApi
import androidx.lifecycle.viewModelScope
import com.dokar.sonner.ToasterState
import com.tecknobit.equinoxcompose.session.sessionflow.SessionFlowState
import com.tecknobit.equinoxcompose.viewmodels.EquinoxViewModel
import com.tecknobit.equinoxcore.time.TimeFormatter
import com.tecknobit.gluky.ui.screens.analyses.data.GlycemiaPoint
import com.tecknobit.gluky.ui.screens.analyses.data.GlycemiaTrendDataSet
import com.tecknobit.gluky.ui.screens.analyses.data.GlycemicTrendData
import com.tecknobit.gluky.ui.screens.shared.presentations.ToastsLauncher
import com.tecknobit.glukycore.enums.GlycemicTrendGroupingDay
import com.tecknobit.glukycore.enums.GlycemicTrendLabelType.COMPUTE_MONTH
import com.tecknobit.glukycore.enums.GlycemicTrendPeriod
import com.tecknobit.glukycore.enums.GlycemicTrendPeriod.ONE_MONTH
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

    private val _glycemicTrendData = MutableStateFlow<GlycemicTrendData?>(
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

    fun retrieveGlycemiaTrendData() {
        // TODO: TO MAKE THE REQUEST THEN APPLYING FILTERS
        viewModelScope.launch {
            delay(2000)
            _glycemicTrendData.value = if (false)
                GlycemicTrendData()
            else {
                GlycemicTrendData(
                    labelType = COMPUTE_MONTH,
                    firstSet = GlycemiaTrendDataSet(
                        maxGlycemicValue = Random.nextInt(300),
                        minGlycemicValue = Random.nextInt(100),
                        mediumGlycemicValue = Random.nextDouble(),


                        set = listOf(
                            GlycemiaPoint(
                                date = TimeFormatter.currentTimestamp(),
                                value = Random.nextInt(100),

                                ),
                            GlycemiaPoint(
                                date = TimeFormatter.currentTimestamp(),
                                value = Random.nextInt(100),

                                )
                        )
                    ),
                    secondSet = GlycemiaTrendDataSet(
                        maxGlycemicValue = Random.nextInt(300),
                        minGlycemicValue = Random.nextInt(100),
                        mediumGlycemicValue = Random.nextDouble(),


                        set = listOf(
                            GlycemiaPoint(
                                date = 1750601328000,
                                value = Random.nextInt(100),

                                ),
                            GlycemiaPoint(
                                date = 1750601328000,
                                value = Random.nextInt(100),

                                )
                        )
                    ),
                    thirdSet = GlycemiaTrendDataSet(
                        maxGlycemicValue = Random.nextInt(300),
                        minGlycemicValue = Random.nextInt(100),
                        mediumGlycemicValue = Random.nextDouble(),


                        set = listOf(
                            GlycemiaPoint(
                                date = 1753193328000,
                                value = Random.nextInt(100),

                                ),
                            GlycemiaPoint(
                                date = 1753193328000,
                                value = Random.nextInt(100),

                                ),
                            GlycemiaPoint(
                                date = 1753193328000,
                                value = Random.nextInt(100),

                                ),
                            GlycemiaPoint(
                                date = 1753193328000,
                                value = Random.nextInt(100),

                                ),
                            GlycemiaPoint(
                                date = 1753193328000,
                                value = Random.nextInt(100),

                                ),
                            GlycemiaPoint(
                                date = 1753193328000,
                                value = Random.nextInt(100),

                                ),
                            GlycemiaPoint(
                                date = 1753193328000,
                                value = Random.nextInt(100),

                                ),
                            GlycemiaPoint(
                                date = 1753193328000,
                                value = Random.nextInt(100),

                                ),
                            GlycemiaPoint(
                                date = 1753193328000,
                                value = Random.nextInt(100),

                                ),
                            GlycemiaPoint(
                                date = 1753193328000,
                                value = Random.nextInt(100),

                                ),
                            GlycemiaPoint(
                                date = 1753193328000,
                                value = Random.nextInt(100),

                                ),
                            GlycemiaPoint(
                                date = 1753193328000,
                                value = Random.nextInt(100),

                                )
                        )
                    ),
                    fourthSet = GlycemiaTrendDataSet(
                        maxGlycemicValue = Random.nextInt(300),
                        minGlycemicValue = Random.nextInt(100),
                        mediumGlycemicValue = Random.nextDouble(),


                        set = listOf(
                            GlycemiaPoint(
                                date = 1755871728000,
                                value = Random.nextInt(100),

                                ),
                            GlycemiaPoint(
                                date = 1755871728000,
                                value = Random.nextInt(100),

                                ),
                            GlycemiaPoint(
                                date = 1755871728000,
                                value = Random.nextInt(100),

                                ),
                            GlycemiaPoint(
                                date = 1755871728000,
                                value = Random.nextInt(100),

                                ),
                            GlycemiaPoint(
                                date = 1755871728000,
                                value = Random.nextInt(100),

                                ),
                            GlycemiaPoint(
                                date = 1755871728000,
                                value = Random.nextInt(100),

                                ),
                            GlycemiaPoint(
                                date = 1755871728000,
                                value = Random.nextInt(100),

                                ),
                            GlycemiaPoint(
                                date = 1755871728000,
                                value = Random.nextInt(100),

                                ),
                            GlycemiaPoint(
                                date = 1755871728000,
                                value = Random.nextInt(100),

                                ),
                            GlycemiaPoint(
                                date = 1755871728000,
                                value = Random.nextInt(100),

                                ),
                            GlycemiaPoint(
                                date = 1755871728000,
                                value = Random.nextInt(100),

                                )
                        )
                    )
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

}