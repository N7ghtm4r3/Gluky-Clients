package com.tecknobit.gluky.helpers

import com.tecknobit.equinoxcore.annotations.Returner
import com.tecknobit.glukycore.enums.GlycemicTrendPeriod
import com.tecknobit.glukycore.enums.GlycemicTrendPeriod.FOUR_MONTHS
import com.tecknobit.glukycore.enums.GlycemicTrendPeriod.ONE_MONTH
import com.tecknobit.glukycore.enums.GlycemicTrendPeriod.ONE_WEEK
import com.tecknobit.glukycore.enums.GlycemicTrendPeriod.THREE_MONTHS
import gluky.composeapp.generated.resources.Res
import gluky.composeapp.generated.resources.four_months_extended
import gluky.composeapp.generated.resources.one_month_extended
import gluky.composeapp.generated.resources.one_week_extended
import gluky.composeapp.generated.resources.three_months_extended
import org.jetbrains.compose.resources.StringResource

@Returner
fun GlycemicTrendPeriod.extendedText(): StringResource {
    return when (this) {
        ONE_WEEK -> Res.string.one_week_extended
        ONE_MONTH -> Res.string.one_month_extended
        THREE_MONTHS -> Res.string.three_months_extended
        FOUR_MONTHS -> Res.string.four_months_extended
    }
}