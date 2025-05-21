package com.tecknobit.gluky.helpers

import android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
import com.tecknobit.equinoxcore.utilities.ContextActivityProvider

/**
 * The `ScreenMonitor` allows to handle the device screen status allowing or not the screen sleeps
 *
 * @author Tecknobit - N7ghtm4r3
 */
@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual object ScreenMonitor {

    /**
     * Method used to keep the screen awake
     */
    actual fun keepScreenAwake() {
        val currentActivity = ContextActivityProvider.getCurrentActivity()
        currentActivity?.let {
            currentActivity.window.addFlags(FLAG_KEEP_SCREEN_ON)
        }
    }

    /**
     * Method used to allow the screen to sleeps
     */
    actual fun allowScreenSleeps() {
        val currentActivity = ContextActivityProvider.getCurrentActivity()
        currentActivity?.let {
            currentActivity.window.clearFlags(FLAG_KEEP_SCREEN_ON)
        }
    }

}