package com.tecknobit.gluky.helpers

import platform.UIKit.UIApplication

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
        UIApplication.sharedApplication.idleTimerDisabled = true
    }

    /**
     * Method used to allow the screen to sleeps
     */
    actual fun allowScreenSleeps() {
        UIApplication.sharedApplication.idleTimerDisabled = false
    }

}