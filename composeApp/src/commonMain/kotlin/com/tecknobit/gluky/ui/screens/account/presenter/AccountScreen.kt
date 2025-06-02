package com.tecknobit.gluky.ui.screens.account.presenter

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AlternateEmail
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Password
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewModelScope
import com.tecknobit.equinoxcompose.annotations.ScreenSection
import com.tecknobit.equinoxcompose.components.EquinoxOutlinedTextField
import com.tecknobit.equinoxcompose.components.EquinoxTextField
import com.tecknobit.equinoxcompose.components.stepper.Step
import com.tecknobit.equinoxcompose.components.stepper.StepContent
import com.tecknobit.equinoxcompose.components.stepper.Stepper
import com.tecknobit.equinoxcompose.session.EquinoxLocalUser.ApplicationTheme
import com.tecknobit.equinoxcompose.utilities.ResponsiveClass.COMPACT_CONTENT
import com.tecknobit.equinoxcompose.utilities.ResponsiveClass.MEDIUM_CONTENT
import com.tecknobit.equinoxcompose.utilities.ResponsiveClassComponent
import com.tecknobit.equinoxcore.helpers.InputsValidator.Companion.LANGUAGES_SUPPORTED
import com.tecknobit.equinoxcore.helpers.InputsValidator.Companion.isEmailValid
import com.tecknobit.equinoxcore.helpers.InputsValidator.Companion.isPasswordValid
import com.tecknobit.gluky.SPLASHSCREEN
import com.tecknobit.gluky.bodyFontFamily
import com.tecknobit.gluky.localUser
import com.tecknobit.gluky.navigator
import com.tecknobit.gluky.ui.components.DeleteAccount
import com.tecknobit.gluky.ui.components.Logout
import com.tecknobit.gluky.ui.components.ProfilePic
import com.tecknobit.gluky.ui.screens.account.presentation.AccountScreenViewModel
import com.tecknobit.gluky.ui.screens.shared.presenters.GlukyScreenPage
import com.tecknobit.gluky.ui.theme.AppTypography
import gluky.composeapp.generated.resources.Res
import gluky.composeapp.generated.resources.account
import gluky.composeapp.generated.resources.change_email
import gluky.composeapp.generated.resources.change_language
import gluky.composeapp.generated.resources.change_password
import gluky.composeapp.generated.resources.change_theme
import gluky.composeapp.generated.resources.delete
import gluky.composeapp.generated.resources.logout
import gluky.composeapp.generated.resources.new_email
import gluky.composeapp.generated.resources.new_password
import gluky.composeapp.generated.resources.wrong_email
import gluky.composeapp.generated.resources.wrong_password
import io.github.vinceglb.filekit.dialogs.FileKitMode.Single
import io.github.vinceglb.filekit.dialogs.FileKitType.Image
import io.github.vinceglb.filekit.dialogs.compose.rememberFilePickerLauncher
import io.github.vinceglb.filekit.name
import io.github.vinceglb.filekit.readBytes
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource

class AccountScreen : GlukyScreenPage<AccountScreenViewModel>(
    viewModel = AccountScreenViewModel(),
    title = Res.string.account
) {

    @Composable
    override fun ColumnScope.ScreenPageContent() {
        UserDetails()
        Settings()
    }

    /**
     * The details of the [localUser]
     */
    @Composable
    @ScreenSection
    @ResponsiveClassComponent(
        classes = [MEDIUM_CONTENT, COMPACT_CONTENT]
    )
    private fun UserDetails() {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            ProfilePicker()
            Column(
                modifier = Modifier
                    .padding(
                        top = 10.dp
                    )
            ) {
                Text(
                    text = localUser.completeName,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = viewModel.email.value,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = AppTypography.labelMedium
                )
                ActionButtons()
            }
        }
    }

    /**
     * The profile picker to allow the [localUser] to change his/her profile picture
     */
    @Composable
    private fun ProfilePicker() {
        val launcher = rememberFilePickerLauncher(
            type = Image,
            mode = Single
        ) { profilePic ->
            profilePic?.let {
                viewModel.viewModelScope.launch {
                    viewModel.changeProfilePic(
                        profilePicBytes = profilePic.readBytes(),
                        profilePicName = profilePic.name
                    )
                }
            }
        }
        ProfilePic(
            profilePic = viewModel.profilePic.value,
            size = 100.dp,
            borderWidth = 1.5.dp,
            onClick = { launcher.launch() }
        )
    }

    /**
     * The actions can be execute on the [localUser] account such logout and delete account
     */
    @Composable
    private fun ActionButtons() {
        Row(
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            val logout = remember { mutableStateOf(false) }
            Button(
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                shape = RoundedCornerShape(
                    size = 10.dp
                ),
                onClick = { logout.value = true }
            ) {
                Text(
                    text = stringResource(Res.string.logout),
                    fontSize = 12.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
            Logout(
                viewModel = viewModel,
                show = logout
            )
            val deleteAccount = remember { mutableStateOf(false) }
            Button(
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                ),
                shape = RoundedCornerShape(
                    size = 10.dp
                ),
                onClick = { deleteAccount.value = true }
            ) {
                Text(
                    text = stringResource(Res.string.delete),
                    fontSize = 12.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            DeleteAccount(
                viewModel = viewModel,
                show = deleteAccount
            )
        }
    }

    /**
     * The settings section to customize the [localUser] experience
     */
    @Composable
    @ScreenSection
    private fun Settings() {
        val steps = remember {
            arrayOf(
                Step(
                    stepIcon = Icons.Default.AlternateEmail,
                    title = Res.string.change_email,
                    content = { ChangeEmail() },
                    dismissAction = { visible -> visible.value = false },
                    confirmAction = { visible ->
                        viewModel.changeEmail(
                            onChange = {
                                visible.value = false
                            }
                        )
                    }
                ),
                Step(
                    stepIcon = Icons.Default.Password,
                    title = Res.string.change_password,
                    content = { ChangePassword() },
                    dismissAction = { visible -> visible.value = false },
                    confirmAction = { visible ->
                        viewModel.changePassword(
                            onChange = {
                                visible.value = false
                            }
                        )
                    }
                ),
                Step(
                    stepIcon = Icons.Default.Language,
                    title = Res.string.change_language,
                    content = { ChangeLanguage() },
                    dismissAction = { visible -> visible.value = false },
                    confirmAction = { visible ->
                        viewModel.changeLanguage(
                            onChange = {
                                visible.value = false
                                navigator.navigate(SPLASHSCREEN)
                            }
                        )
                    }
                ),
                Step(
                    stepIcon = Icons.Default.Palette,
                    title = Res.string.change_theme,
                    content = { ChangeTheme() },
                    dismissAction = { visible -> visible.value = false },
                    confirmAction = { visible ->
                        viewModel.changeTheme(
                            onChange = {
                                visible.value = false
                                navigator.navigate(SPLASHSCREEN)
                            }
                        )
                    }
                )
            )
        }
        Stepper(
            containerModifier = Modifier
                .padding(
                    top = 10.dp
                )
                .navigationBarsPadding(),
            steps = steps,
            stepBackgroundColor = MaterialTheme.colorScheme.surfaceContainer
        )
    }

    /**
     * Section to change the [localUser]'s email
     */
    @StepContent(
        number = 1
    )
    @Composable
    private fun ChangeEmail() {
        val focusRequester = remember { FocusRequester() }
        LaunchedEffect(Unit) {
            focusRequester.requestFocus()
        }
        viewModel.newEmail = remember { mutableStateOf("") }
        viewModel.newEmailError = remember { mutableStateOf(false) }
        EquinoxTextField(
            modifier = Modifier
                .focusRequester(focusRequester),
            textFieldColors = TextFieldDefaults.colors(
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                errorIndicatorColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedContainerColor = Color.Transparent,
                errorContainerColor = Color.Transparent
            ),
            value = viewModel.newEmail,
            textFieldStyle = TextStyle(
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                fontFamily = bodyFontFamily
            ),
            isError = viewModel.newEmailError,
            mustBeInLowerCase = true,
            allowsBlankSpaces = false,
            validator = { isEmailValid(it) },
            errorText = Res.string.wrong_email,
            errorTextStyle = TextStyle(
                fontSize = 14.sp,
                fontFamily = bodyFontFamily
            ),
            placeholder = Res.string.new_email,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Email
            )
        )
    }

    /**
     * Section to change the [localUser]'s password
     */
    @StepContent(
        number = 2
    )
    @Composable
    private fun ChangePassword() {
        val focusRequester = remember { FocusRequester() }
        LaunchedEffect(Unit) {
            focusRequester.requestFocus()
        }
        viewModel.newPassword = remember { mutableStateOf("") }
        viewModel.newPasswordError = remember { mutableStateOf(false) }
        var hiddenPassword by remember { mutableStateOf(true) }
        EquinoxOutlinedTextField(
            modifier = Modifier
                .focusRequester(focusRequester),
            outlinedTextFieldColors = TextFieldDefaults.colors(
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                errorIndicatorColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedContainerColor = Color.Transparent,
                errorContainerColor = Color.Transparent
            ),
            value = viewModel.newPassword,
            outlinedTextFieldStyle = TextStyle(
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                fontFamily = bodyFontFamily
            ),
            isError = viewModel.newPasswordError,
            allowsBlankSpaces = false,
            trailingIcon = {
                IconButton(
                    onClick = { hiddenPassword = !hiddenPassword }
                ) {
                    Icon(
                        imageVector = if (hiddenPassword)
                            Icons.Default.Visibility
                        else
                            Icons.Default.VisibilityOff,
                        contentDescription = null
                    )
                }
            },
            visualTransformation = if (hiddenPassword)
                PasswordVisualTransformation()
            else
                VisualTransformation.None,
            validator = { isPasswordValid(it) },
            errorText = stringResource(Res.string.wrong_password),
            errorTextStyle = TextStyle(
                fontSize = 14.sp,
                fontFamily = bodyFontFamily
            ),
            placeholder = stringResource(Res.string.new_password),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Password
            )
        )
    }

    /**
     * Section to change the [localUser]'s language
     */
    @StepContent(
        number = 3
    )
    @Composable
    private fun ChangeLanguage() {
        Column(
            modifier = Modifier
                .selectableGroup()
        ) {
            LANGUAGES_SUPPORTED.entries.forEach { entry ->
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = viewModel.language.value == entry.key,
                        onClick = { viewModel.language.value = entry.key }
                    )
                    Text(
                        text = entry.value
                    )
                }
            }
        }
    }

    /**
     * Section to change the [localUser]'s theme
     */
    @StepContent(
        number = 4
    )
    @Composable
    private fun ChangeTheme() {
        Column(
            modifier = Modifier
                .selectableGroup()
        ) {
            ApplicationTheme.entries.forEach { entry ->
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = viewModel.theme.value == entry,
                        onClick = { viewModel.theme.value = entry }
                    )
                    Text(
                        text = entry.name
                    )
                }
            }
        }
    }

    /**
     * Method used to collect or instantiate the states of the screen
     */
    @Composable
    override fun CollectStates() {
        viewModel.profilePic = remember { mutableStateOf(localUser.profilePic) }
        viewModel.email = remember { mutableStateOf(localUser.email) }
        viewModel.password = remember { mutableStateOf(localUser.password) }
        viewModel.language = remember { mutableStateOf(localUser.language) }
        viewModel.theme = remember { mutableStateOf(localUser.theme) }
    }

}