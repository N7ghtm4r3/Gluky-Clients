package com.tecknobit.gluky.ui.screens.measurements.data.types

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import com.tecknobit.equinoxcore.annotations.Returner
import com.tecknobit.equinoxcore.json.treatsAsString
import com.tecknobit.gluky.ui.theme.green
import com.tecknobit.gluky.ui.theme.red
import com.tecknobit.gluky.ui.theme.yellow
import com.tecknobit.glukycore.ANNOTATION_DATE_KEY
import com.tecknobit.glukycore.GLYCEMIA_KEY
import com.tecknobit.glukycore.HYPER_GLYCEMIA
import com.tecknobit.glukycore.INSULIN_UNITS_KEY
import com.tecknobit.glukycore.MEDIUM_HIGH_GLYCEMIA
import com.tecknobit.glukycore.NORMAL_GLYCEMIA
import com.tecknobit.glukycore.POST_PRANDIAL_GLYCEMIA_KEY
import com.tecknobit.glukycore.RAW_CONTENT_KEY
import com.tecknobit.glukycore.enums.MeasurementType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlin.math.abs

/**
 * The `Meal` data class represents the measurement of a meal
 *
 * @property id The identifier of the meal
 * @property type The type of the meal
 * @property _annotationDate The date when the meal has been annotated
 * @property _rawContent The content of the meal "raw" formatted as json
 * @property _glycemia The value of the glycemia when annotated
 * @property _postPrandialGlycemia The value of the post-prandial glycemia when annotated
 * @property _insulinUnits The value of the administered insulin units related to the {@link #glycemia} value
 *
 * @author N7ghtm4r3 - Tecknobit
 *
 * @see GlycemicMeasurementItem
 */
@Serializable
data class Meal(
    override val id: String,
    val type: MeasurementType,
    @SerialName(ANNOTATION_DATE_KEY)
    override val _annotationDate: Long = -1,
    @SerialName(RAW_CONTENT_KEY)
    private val _rawContent: String = "",
    @SerialName(GLYCEMIA_KEY)
    override val _glycemia: Int = -1,
    @SerialName(POST_PRANDIAL_GLYCEMIA_KEY)
    private val _postPrandialGlycemia: Int = -1,
    @SerialName(INSULIN_UNITS_KEY)
    override val _insulinUnits: Int = -1,
) : GlycemicMeasurementItem() {

    companion object {

        /**
         * Method used to obtain the properly color based on the glycemia level
         *
         * @return the properly color as [Color]
         */
        @Returner
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

    /**
     * `_rawContent` state for the content of the meal "raw" formatted as json
     */
    @Transient
    val rawContent: MutableState<JsonObject> = mutableStateOf(
        Json.decodeFromString(_rawContent)
    )

    /**
     * `content` state for the content pretty formatted
     */
    @Transient
    val content: State<String> = derivedStateOf {
        rawContent.prettyText()
    }

    /**
     * Method used to format the [rawContent] into a pretty text
     *
     * @return the pretty text as [String]
     */
    @Returner
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

    /**
     * `postPrandialGlycemia` state for the value of the post-prandial glycemia when annotated
     */
    @Transient
    val postPrandialGlycemia: MutableState<Int> = mutableStateOf(_postPrandialGlycemia)

    /**
     * `glycemicTrend` the current glycemic trend annotated
     */
    val glycemicTrend: List<Int>
        get() = if (postPrandialGlycemia.value != -1)
            listOf(glycemia.value, postPrandialGlycemia.value)
        else
            listOf(glycemia.value)

    /**
     * `glycemicGap` the glycemic gap between the [postPrandialGlycemia] and [glycemia] value
     */
    val glycemicGap: Int
        get() = abs(glycemia.value - postPrandialGlycemia.value)

    /**
     * `isNotFilledYet` whether the item has been filled
     */
    override val isNotFilledYet: Boolean
        get() = annotationDate.value == -1L &&
                content.value.isEmpty() &&
                glycemia.value == -1 &&
                postPrandialGlycemia.value == -1 &&
                insulinUnits.value == -1

}
