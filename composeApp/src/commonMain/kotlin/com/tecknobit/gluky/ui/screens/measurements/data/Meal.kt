package com.tecknobit.gluky.ui.screens.measurements.data

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import com.tecknobit.equinoxcore.json.treatsAsString
import com.tecknobit.gluky.ui.theme.green
import com.tecknobit.gluky.ui.theme.red
import com.tecknobit.gluky.ui.theme.yellow
import com.tecknobit.glukycore.ANNOTATION_DATE_KEY
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
    override val id: String,
    val type: MeasurementType,
    @SerialName(ANNOTATION_DATE_KEY)
    override val _annotationDate: Long = -1,
    @SerialName(RAW_CONTENT_KEY)
    private val _rawContent: JsonObject = buildJsonObject { },
    @SerialName(GLYCEMIA_KEY)
    override val _glycemia: Int = -1,
    @SerialName(POST_PRANDIAL_GLYCEMIA_KEY)
    private val _postPrandialGlycemia: Int = -1,
    @SerialName(INSULIN_UNITS_KEY)
    override val _insulinUnits: Int = -1,
) : GlycemicMeasurementItem() {

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
    val rawContent: MutableState<JsonObject> = mutableStateOf(_rawContent)

    @Transient
    val content: State<String> = derivedStateOf {
        rawContent.prettyText()
    }

    private fun MutableState<JsonObject>.prettyText(): String {
        val prettyText = StringBuilder()
        val lastKey = value.entries.lastOrNull()?.key
        lastKey?.let {
            value.forEach { entry ->
                val key = entry.key
                prettyText.append("- $key (${entry.value.treatsAsString()})")
                if (key != lastKey)
                    prettyText.append("\n")
            }
        }
        return prettyText.toString()
    }

    @Transient
    val postPrandialGlycemia: MutableState<Int> = mutableStateOf(_postPrandialGlycemia)

    val glycemiaTrend: List<Int>
        get() = if (postPrandialGlycemia.value != -1)
            listOf(glycemia.value, postPrandialGlycemia.value)
        else
            listOf(glycemia.value)

    val glycemiaGap: Int
        get() = abs(glycemia.value - postPrandialGlycemia.value)

    override val isNotFilledYet: Boolean
        get() = annotationDate.value == -1L &&
                content.value.isEmpty() &&
                glycemia.value == -1 &&
                postPrandialGlycemia.value == -1 &&
                insulinUnits.value == -1

}
