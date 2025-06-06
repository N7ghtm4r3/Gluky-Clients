package com.tecknobit.gluky.ui.screens.shared.presentations

import com.dokar.sonner.Toast
import com.dokar.sonner.ToastType
import com.dokar.sonner.ToasterState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.getString

/**
 * The `ToastsLauncher` interface allows to handle the toasts launches
 *
 * @author N7ghtm4r3 - Tecknobit
 *
 * @see [Toast]
 * @see [ToasterState]
 */
interface ToastsLauncher {

    /**
     * `toasterState` the state used to launch the toasts messages
     */
    var toasterState: ToasterState

    /**
     * `toasterState` the state used to launch the toasts messages
     */
    var scope: CoroutineScope

    /**
     * Method used to toast an error message
     *
     * @param error The error message to display
     * @param formatArgs The arguments used to dynamically format the error message if needed
     */
    fun toastError(
        error: StringResource,
        vararg formatArgs: Any,
    ) {
        scope.launch {
            val errorMessage = getString(
                resource = error,
                *formatArgs
            )
            toasterState.show(
                toast = Toast(
                    message = errorMessage,
                    type = ToastType.Error
                )
            )
        }
    }

}