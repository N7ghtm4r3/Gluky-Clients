package com.tecknobit.gluky.ui.screens.analyses.presentation

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.ExperimentalComposeApi
import androidx.lifecycle.viewModelScope
import com.tecknobit.equinoxcompose.session.sessionflow.SessionFlowState
import com.tecknobit.equinoxcompose.viewmodels.EquinoxViewModel
import com.tecknobit.equinoxcore.time.TimeFormatter
import com.tecknobit.gluky.ui.screens.analyses.data.GlycemiaPoint
import com.tecknobit.gluky.ui.screens.analyses.data.GlycemiaTrendDataSet
import com.tecknobit.gluky.ui.screens.analyses.data.GlycemicTrendData
import com.tecknobit.glukycore.enums.GlycemicTrendGroupingDay.ALL
import com.tecknobit.glukycore.enums.GlycemicTrendPeriod
import com.tecknobit.glukycore.enums.GlycemicTrendPeriod.ONE_WEEK
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.random.Random

class AnalysesScreenViewModel : EquinoxViewModel(
    snackbarHostState = SnackbarHostState()
) {

    @OptIn(ExperimentalComposeApi::class)
    lateinit var sessionFlowState: SessionFlowState

    private val _glycemicTrendData = MutableStateFlow<GlycemicTrendData?>(
        value = null
    )
    val glycemicTrendData = _glycemicTrendData.asStateFlow()

    private val _glycemicTrendPeriod = MutableStateFlow(
        value = ONE_WEEK
    )
    val glycemicTrendPeriod = _glycemicTrendPeriod.asStateFlow()

    private val _glycemicTrendGroupingDay = MutableStateFlow(
        value = ALL
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
                    firstSet = GlycemiaTrendDataSet(
                        maxGlycemicValue = Random.nextInt(300),
                        minGlycemicValue = Random.nextInt(100),
                        mediumGlycemicValue = Random.nextDouble(),
                        maxInsulinValue = Random.nextInt(300),
                        minInsulinValue = Random.nextInt(150),
                        mediumInsulinValue = Random.nextDouble(),
                        set = listOf(
                            GlycemiaPoint(
                                date = TimeFormatter.currentTimestamp(),
                                value = Random.nextInt(100),
                                insulinUnits = Random.nextInt(20)
                            ),
                            GlycemiaPoint(
                                date = TimeFormatter.currentTimestamp(),
                                value = Random.nextInt(100),
                                insulinUnits = Random.nextInt(20)
                            )
                        )
                    ),
                    secondSet = GlycemiaTrendDataSet(
                        maxGlycemicValue = Random.nextInt(300),
                        minGlycemicValue = Random.nextInt(100),
                        mediumGlycemicValue = Random.nextDouble(),
                        maxInsulinValue = Random.nextInt(300),
                        minInsulinValue = Random.nextInt(150),
                        mediumInsulinValue = Random.nextDouble(),
                        set = listOf(
                            GlycemiaPoint(
                                date = TimeFormatter.currentTimestamp(),
                                value = Random.nextInt(100),
                                insulinUnits = Random.nextInt(20)
                            ),
                            GlycemiaPoint(
                                date = TimeFormatter.currentTimestamp(),
                                value = Random.nextInt(100),
                                insulinUnits = Random.nextInt(20)
                            )
                        )
                    ),
                    thirdSet = GlycemiaTrendDataSet(
                        maxGlycemicValue = Random.nextInt(300),
                        minGlycemicValue = Random.nextInt(100),
                        mediumGlycemicValue = Random.nextDouble(),
                        maxInsulinValue = Random.nextInt(300),
                        minInsulinValue = Random.nextInt(150),
                        mediumInsulinValue = Random.nextDouble(),
                        set = listOf(
                            GlycemiaPoint(
                                date = TimeFormatter.currentTimestamp(),
                                value = Random.nextInt(100),
                                insulinUnits = Random.nextInt(20)
                            ),
                            GlycemiaPoint(
                                date = TimeFormatter.currentTimestamp(),
                                value = Random.nextInt(100),
                                insulinUnits = Random.nextInt(20)
                            ),
                            GlycemiaPoint(
                                date = TimeFormatter.currentTimestamp(),
                                value = Random.nextInt(100),
                                insulinUnits = Random.nextInt(20)
                            ),
                            GlycemiaPoint(
                                date = TimeFormatter.currentTimestamp(),
                                value = Random.nextInt(100),
                                insulinUnits = Random.nextInt(20)
                            ),
                            GlycemiaPoint(
                                date = TimeFormatter.currentTimestamp(),
                                value = Random.nextInt(100),
                                insulinUnits = Random.nextInt(20)
                            ),
                            GlycemiaPoint(
                                date = TimeFormatter.currentTimestamp(),
                                value = Random.nextInt(100),
                                insulinUnits = Random.nextInt(20)
                            ),
                            GlycemiaPoint(
                                date = TimeFormatter.currentTimestamp(),
                                value = Random.nextInt(100),
                                insulinUnits = Random.nextInt(20)
                            ),
                            GlycemiaPoint(
                                date = TimeFormatter.currentTimestamp(),
                                value = Random.nextInt(100),
                                insulinUnits = Random.nextInt(20)
                            ),
                            GlycemiaPoint(
                                date = TimeFormatter.currentTimestamp(),
                                value = Random.nextInt(100),
                                insulinUnits = Random.nextInt(20)
                            ),
                            GlycemiaPoint(
                                date = TimeFormatter.currentTimestamp(),
                                value = Random.nextInt(100),
                                insulinUnits = Random.nextInt(20)
                            ),
                            GlycemiaPoint(
                                date = TimeFormatter.currentTimestamp(),
                                value = Random.nextInt(100),
                                insulinUnits = Random.nextInt(20)
                            ),
                            GlycemiaPoint(
                                date = TimeFormatter.currentTimestamp(),
                                value = Random.nextInt(100),
                                insulinUnits = Random.nextInt(20)
                            )
                        )
                    ),
                    fourthSet = GlycemiaTrendDataSet(
                        maxGlycemicValue = Random.nextInt(300),
                        minGlycemicValue = Random.nextInt(100),
                        mediumGlycemicValue = Random.nextDouble(),
                        maxInsulinValue = Random.nextInt(300),
                        minInsulinValue = Random.nextInt(150),
                        mediumInsulinValue = Random.nextDouble(),
                        set = listOf(
                            GlycemiaPoint(
                                date = TimeFormatter.currentTimestamp(),
                                value = Random.nextInt(100),
                                insulinUnits = Random.nextInt(20)
                            ),
                            GlycemiaPoint(
                                date = TimeFormatter.currentTimestamp(),
                                value = Random.nextInt(100),
                                insulinUnits = Random.nextInt(20)
                            ),
                            GlycemiaPoint(
                                date = TimeFormatter.currentTimestamp(),
                                value = Random.nextInt(100),
                                insulinUnits = Random.nextInt(20)
                            ),
                            GlycemiaPoint(
                                date = TimeFormatter.currentTimestamp(),
                                value = Random.nextInt(100),
                                insulinUnits = Random.nextInt(20)
                            ),
                            GlycemiaPoint(
                                date = TimeFormatter.currentTimestamp(),
                                value = Random.nextInt(100),
                                insulinUnits = Random.nextInt(20)
                            ),
                            GlycemiaPoint(
                                date = TimeFormatter.currentTimestamp(),
                                value = Random.nextInt(100),
                                insulinUnits = Random.nextInt(20)
                            ),
                            GlycemiaPoint(
                                date = TimeFormatter.currentTimestamp(),
                                value = Random.nextInt(100),
                                insulinUnits = Random.nextInt(20)
                            ),
                            GlycemiaPoint(
                                date = TimeFormatter.currentTimestamp(),
                                value = Random.nextInt(100),
                                insulinUnits = Random.nextInt(20)
                            ),
                            GlycemiaPoint(
                                date = TimeFormatter.currentTimestamp(),
                                value = Random.nextInt(100),
                                insulinUnits = Random.nextInt(20)
                            ),
                            GlycemiaPoint(
                                date = TimeFormatter.currentTimestamp(),
                                value = Random.nextInt(100),
                                insulinUnits = Random.nextInt(20)
                            ),
                            GlycemiaPoint(
                                date = TimeFormatter.currentTimestamp(),
                                value = Random.nextInt(100),
                                insulinUnits = Random.nextInt(20)
                            )
                        )
                    )
                )
            }
        }
    }

    fun selectGlycemicTrendPeriod(
        period: GlycemicTrendPeriod,
    ) {
        _glycemicTrendPeriod.value = period
        retrieveGlycemiaTrendData()
    }

}