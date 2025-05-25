package com.tecknobit.gluky.helpers

import com.google.android.play.core.review.ReviewManager
import com.google.android.play.core.review.ReviewManagerFactory
import com.tecknobit.equinoxcore.utilities.ContextActivityProvider

/**
 * The `KReviewer` class is useful to manage the biometric authentication
 *
 * @author N7ghtm4r3 - Tecknobit
 */
@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class KReviewer actual constructor() {

    /**
     * `activity` the activity where the review helper has been instantiated
     */
    private val activity = ContextActivityProvider.getCurrentActivity()!!

    /**
     * `reviewManager` the manager used to allow the user to review the application in app
     */
    private var reviewManager: ReviewManager = ReviewManagerFactory.create(activity)

    /**
     * Method used to review in-app the application
     *
     * @param flowAction The action to execute when the review form appears or not and if appeared
     * the when user dismissed the action or leaved a review
     */
    actual fun reviewInApp(
        flowAction: () -> Unit,
    ) {
        val request = reviewManager.requestReviewFlow()
        request.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val flow = reviewManager.launchReviewFlow(activity, task.result)
                flow.addOnCompleteListener {
                    flowAction()
                }
                flow.addOnCanceledListener {
                    flowAction()
                }
            } else
                flowAction()
        }
    }

}