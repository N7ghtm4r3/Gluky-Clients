package com.tecknobit.gluky.ui.screens.meals.data

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import com.tecknobit.gluky.ui.theme.green
import com.tecknobit.gluky.ui.theme.red
import com.tecknobit.gluky.ui.theme.yellow
import com.tecknobit.glukycore.ANNOTATION_DATE_KEY
import com.tecknobit.glukycore.CONTENT_KEY
import com.tecknobit.glukycore.GLYCEMIA_KEY
import com.tecknobit.glukycore.INSULIN_UNITS_KEY
import com.tecknobit.glukycore.POST_PRANDIAL_GLYCEMIA_KEY
import com.tecknobit.glukycore.RAW_CONTENT_KEY
import com.tecknobit.glukycore.enums.MeasurementType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.buildJsonObject
import kotlin.math.abs

@Serializable
data class Meal(
    val id: String,
    val type: MeasurementType,
    @SerialName(ANNOTATION_DATE_KEY)
    val annotationDate: Long = -1,
    @SerialName(CONTENT_KEY)
    private val _content: String = "",
    @SerialName(RAW_CONTENT_KEY)
    private val _rawContent: JsonObject = buildJsonObject { },
    @SerialName(GLYCEMIA_KEY)
    private val _glycemia: Int = -1,
    @SerialName(POST_PRANDIAL_GLYCEMIA_KEY)
    private val _postPrandialGlycemia: Int = -1,
    @SerialName(INSULIN_UNITS_KEY)
    private val _insulinUnits: Int = -1,
) {

    companion object {

        private const val NORMAL_GLYCEMIA = 70

        private const val MEDIUM_HIGH_GLYCEMIA = 180

        private const val HYPER_GLYCEMIA = 220

        @Composable
        fun Int.levelColor(): Color {
            if (this == -1)
                return MaterialTheme.colorScheme.primary
            @Suppress("ConvertTwoComparisonsToRangeCheck")
            return if (this < NORMAL_GLYCEMIA)
                red()
            else if (this in NORMAL_GLYCEMIA..MEDIUM_HIGH_GLYCEMIA)
                green()
            else if (this > MEDIUM_HIGH_GLYCEMIA && this <= HYPER_GLYCEMIA)
                yellow()
            else
                red()
        }

    }

    @Transient
    val content: MutableState<String> = mutableStateOf(_content)

    @Transient
    val rawContent: MutableState<JsonObject> = mutableStateOf(_rawContent)

    @Transient
    val glycemia: MutableState<Int> = mutableStateOf(_glycemia)

    @Transient
    val postPrandialGlycemia: MutableState<Int> = mutableStateOf(_postPrandialGlycemia)

    @Transient
    val insulinUnits: MutableState<Int> = mutableStateOf(_insulinUnits)

    val glycemiaTrend: List<Int>
        get() = if (postPrandialGlycemia.value != -1)
            listOf(glycemia.value, postPrandialGlycemia.value)
        else
            listOf(glycemia.value)

    val glycemiaGap: Int
        get() = abs(glycemia.value - postPrandialGlycemia.value)

    val isFilledYet: Boolean
        get() = annotationDate != -1L
}
