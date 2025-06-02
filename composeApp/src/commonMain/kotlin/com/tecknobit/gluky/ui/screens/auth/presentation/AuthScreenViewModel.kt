package com.tecknobit.gluky.ui.screens.auth.presentation

import androidx.compose.material3.SnackbarHostState
import com.tecknobit.equinoxcompose.utilities.getCurrentLocaleLanguage
import com.tecknobit.equinoxcompose.viewmodels.EquinoxAuthViewModel
import com.tecknobit.equinoxcore.annotations.CustomParametersOrder
import com.tecknobit.equinoxcore.helpers.InputsValidator.Companion.isEmailValid
import com.tecknobit.equinoxcore.helpers.InputsValidator.Companion.isNameValid
import com.tecknobit.equinoxcore.helpers.InputsValidator.Companion.isPasswordValid
import com.tecknobit.equinoxcore.helpers.InputsValidator.Companion.isSurnameValid
import com.tecknobit.equinoxcore.helpers.LANGUAGE_KEY
import com.tecknobit.gluky.localUser
import com.tecknobit.gluky.requester

class AuthScreenViewModel : EquinoxAuthViewModel(
    snackbarHostState = SnackbarHostState(),
    requester = requester,
    localUser = localUser
) {

    override fun signUpFormIsValid(): Boolean {
        var isValid: Boolean = isNameValid(name.value)
        if (!isValid) {
            nameError.value = true
            return false
        }
        isValid = isSurnameValid(surname.value)
        if (!isValid) {
            surnameError.value = true
            return false
        }
        isValid = isEmailValid(email.value)
        if (!isValid) {
            emailError.value = true
            return false
        }
        isValid = isPasswordValid(password.value)
        if (!isValid) {
            passwordError.value = true
            return false
        }
        return true
    }

    override fun signInFormIsValid(): Boolean {
        var isValid: Boolean = isEmailValid(email.value)
        if (!isValid) {
            emailError.value = true
            return false
        }
        isValid = isPasswordValid(password.value)
        if (!isValid) {
            passwordError.value = true
            return false
        }
        return true
    }

    // TODO: TO REMOVE WHEN LANGUAGE PARAMETER IMPLEMENTED BUILT-IN
    @CustomParametersOrder(LANGUAGE_KEY)
    override fun getSignInCustomParameters(): Array<out Any?> {
        return arrayOf(getCurrentLocaleLanguage().substring(0, 2))
    }

}