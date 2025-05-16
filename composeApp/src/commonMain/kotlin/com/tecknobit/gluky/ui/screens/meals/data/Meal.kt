package com.tecknobit.gluky.ui.screens.meals.data

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.tecknobit.gluky.ui.theme.green
import com.tecknobit.gluky.ui.theme.red
import com.tecknobit.gluky.ui.theme.yellow
import com.tecknobit.glukycore.ANNOTATION_DATE_KEY
import com.tecknobit.glukycore.INSULIN_UNITS_KEY
import com.tecknobit.glukycore.POST_PRANDIAL_GLYCEMIA_KEY
import com.tecknobit.glukycore.enums.MeasurementType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.math.abs

@Serializable
data class Meal(
    val id: String,
    val type: MeasurementType,
    @SerialName(ANNOTATION_DATE_KEY)
    val annotationDate: Long,
    val content: String,
    val glycemia: Int,
    @SerialName(POST_PRANDIAL_GLYCEMIA_KEY)
    val postPrandialGlycemia: Int = -1,
    @SerialName(INSULIN_UNITS_KEY)
    val insulinUnits: Int? = null,
) {

    companion object {

        private const val NORMAL_GLYCEMIA = 70

        private const val MEDIUM_HIGH_GLYCEMIA = 180

        private const val HYPER_GLYCEMIA = 220

        @Composable
        fun Int.levelColor(): Color {
            if (this == -1)
                return MaterialTheme.colorScheme.primary
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

    val glycemiaTrend = if (postPrandialGlycemia != -1)
        listOf(glycemia, postPrandialGlycemia)
    else
        listOf(glycemia)

    val glycemiaGap = abs(glycemia - postPrandialGlycemia)

}
