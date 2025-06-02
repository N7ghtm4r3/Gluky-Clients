package com.tecknobit.gluky.ui.components

import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecknobit.equinoxcompose.components.EquinoxAlertDialog
import com.tecknobit.gluky.SPLASHSCREEN
import com.tecknobit.gluky.displayFontFamily
import com.tecknobit.gluky.navigator
import com.tecknobit.gluky.ui.screens.account.presentation.AccountScreenViewModel
import gluky.composeapp.generated.resources.Res
import gluky.composeapp.generated.resources.delete
import gluky.composeapp.generated.resources.delete_warn_text
import gluky.composeapp.generated.resources.logout
import gluky.composeapp.generated.resources.logout_warn_text

/**
 * `titleStyle` -> the style to apply to the title of the [EquinoxAlertDialog]
 */
val titleStyle = TextStyle(
    fontFamily = displayFontFamily,
    fontSize = 18.sp
)

/**
 * Alert to warn about the logout action
 *
 * @param viewModel The support viewmodel for the screen
 * @param show Whether the alert is shown
 */
@Composable
fun Logout(
    viewModel: AccountScreenViewModel,
    show: MutableState<Boolean>,
) {
    EquinoxAlertDialog(
        icon = Icons.AutoMirrored.Filled.Logout,
        modifier = Modifier
            .widthIn(
                max = 400.dp
            ),
        viewModel = viewModel,
        show = show,
        title = Res.string.logout,
        titleStyle = titleStyle,
        text = Res.string.logout_warn_text,
        confirmAction = {
            viewModel.clearSession {
                navigator.navigate(SPLASHSCREEN)
            }
        }
    )
}

/**
 * Alert to warn about the account deletion
 *
 * @param viewModel The support viewmodel for the screen
 * @param show Whether the alert is shown
 */
@Composable
fun DeleteAccount(
    viewModel: AccountScreenViewModel,
    show: MutableState<Boolean>,
) {
    EquinoxAlertDialog(
        icon = Icons.Default.Delete,
        modifier = Modifier
            .widthIn(
                max = 400.dp
            ),
        viewModel = viewModel,
        show = show,
        title = Res.string.delete,
        titleStyle = titleStyle,
        text = Res.string.delete_warn_text,
        confirmAction = {
            viewModel.deleteAccount {
                show.value = false
                navigator.navigate(SPLASHSCREEN)
            }
        }
    )
}