package com.tecknobit.gluky.helpers

/**
 * The `KReviewer` class is useful to manage the biometric authentication
 *
 * @author N7ghtm4r3 - Tecknobit
 */
@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
expect class KReviewer() {

    /**
     * Method used to review in-app the application
     *
     * @param flowAction The action to execute when the review form appears or not and if appeared
     * the when user dismissed the action or leaved a review
     */
    fun reviewInApp(
        flowAction: () -> Unit = {},
    )

}