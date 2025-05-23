package com.tecknobit.gluky.ui.screens.shared.presentations

import com.dokar.sonner.Toast
import com.dokar.sonner.ToastType
import com.dokar.sonner.ToasterState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.getString

interface ToastsLauncher {

    var toasterState: ToasterState

    var scope: CoroutineScope

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