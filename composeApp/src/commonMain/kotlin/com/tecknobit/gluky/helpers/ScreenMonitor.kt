package com.tecknobit.gluky.helpers

/**
 * The `ScreenMonitor` allows to handle the device screen status allowing or not the screen sleeps
 *
 * @author Tecknobit - N7ghtm4r3
 */
@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
// TODO: TO REMOVE 
expect object ScreenMonitor {

    /**
     * Method used to keep the screen awake
     */
    fun keepScreenAwake()

    /**
     * Method used to allow the screen to sleeps
     */
    fun allowScreenSleeps()

}