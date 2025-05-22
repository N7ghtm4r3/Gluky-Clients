package com.tecknobit.gluky.helpers

import com.tecknobit.equinoxcore.annotations.FutureEquinoxApi
import com.tecknobit.equinoxcore.time.TimeFormatter.toLocalDate
import gluky.composeapp.generated.resources.Res
import gluky.composeapp.generated.resources.april
import gluky.composeapp.generated.resources.august
import gluky.composeapp.generated.resources.december
import gluky.composeapp.generated.resources.february
import gluky.composeapp.generated.resources.fri
import gluky.composeapp.generated.resources.january
import gluky.composeapp.generated.resources.july
import gluky.composeapp.generated.resources.june
import gluky.composeapp.generated.resources.march
import gluky.composeapp.generated.resources.may
import gluky.composeapp.generated.resources.mon
import gluky.composeapp.generated.resources.november
import gluky.composeapp.generated.resources.october
import gluky.composeapp.generated.resources.sat
import gluky.composeapp.generated.resources.september
import gluky.composeapp.generated.resources.sun
import gluky.composeapp.generated.resources.thu
import gluky.composeapp.generated.resources.tue
import gluky.composeapp.generated.resources.wed
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.Month
import org.jetbrains.compose.resources.StringResource

@FutureEquinoxApi(
    protoBehavior = """
        Actually is implemented to return just in for languages but will be extended to all the 
        languages supported by Equinox
    """,
    releaseVersion = "1.1.3",
    additionalNotes = """
        - Warn about is temporary method until the official kotlinx's one will be released
        - Check whether the best name is this or something like toDayOfWeek or anyone else
        - Check if overload this method with the @Composable one to return directly the currentDay as String
        - To allow to return uppercase and lowercase 
    """
)
fun Long.asDayOfWeek(): StringResource {
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

@FutureEquinoxApi(
    protoBehavior = """
        Actually is implemented to return just in for languages but will be extended to all the 
        languages supported by Equinox
    """,
    releaseVersion = "1.1.3",
    additionalNotes = """
        - Warn about is temporary method until the official kotlinx's one will be released
        - Check whether the best name is this or something like asMonth or anyone else
        - Check if overload this method with the @Composable one to return directly the currentDay as String
        - To allow to return uppercase and lowercase 
    """
)
fun Long.asMonth(): StringResource {
    val month = this.toLocalDate().month
    return when (month) {
        Month.JANUARY -> Res.string.january
        Month.FEBRUARY -> Res.string.february
        Month.MARCH -> Res.string.march
        Month.APRIL -> Res.string.april
        Month.MAY -> Res.string.may
        Month.JUNE -> Res.string.june
        Month.JULY -> Res.string.july
        Month.AUGUST -> Res.string.august
        Month.SEPTEMBER -> Res.string.september
        Month.OCTOBER -> Res.string.october
        Month.NOVEMBER -> Res.string.november
        else -> Res.string.december
    }
}