package com.tecknobit.gluky.ui.screens.auth.presentation

import androidx.compose.material3.SnackbarHostState
import com.tecknobit.equinoxcompose.utilities.getCurrentLocaleLanguage
import com.tecknobit.equinoxcompose.viewmodels.EquinoxAuthViewModel
import com.tecknobit.equinoxcore.annotations.CustomParametersOrder
import com.tecknobit.equinoxcore.annotations.RequiresSuperCall
import com.tecknobit.equinoxcore.helpers.InputsValidator.Companion.isEmailValid
import com.tecknobit.equinoxcore.helpers.InputsValidator.Companion.isNameValid
import com.tecknobit.equinoxcore.helpers.InputsValidator.Companion.isPasswordValid
import com.tecknobit.equinoxcore.helpers.InputsValidator.Companion.isSurnameValid
import com.tecknobit.equinoxcore.helpers.LANGUAGE_KEY
import com.tecknobit.gluky.HOME_SCREEN
import com.tecknobit.gluky.localUser
import com.tecknobit.gluky.navigator
import com.tecknobit.gluky.requester
import kotlinx.serialization.json.JsonObject

/**
 * The `AuthScreenViewModel` class is the support class used to execute the authentication requests
 * to the backend
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see androidx.lifecycle.ViewModel
 * @see com.tecknobit.equinoxcompose.session.Retriever.RetrieverWrapper
 * @see com.tecknobit.equinoxcompose.session.screens.EquinoxScreen
 * @see EquinoxAuthViewModel
 */
class AuthScreenViewModel : EquinoxAuthViewModel(
    snackbarHostState = SnackbarHostState(),
    requester = requester,
    localUser = localUser
) {

    /**
     * Method used to validate the inputs for the [signUp] request
     *
     * @return whether the inputs are valid as [Boolean]
     */
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

    /**
     * Method used to validate the inputs for the [signIn] request
     *
     * @return whether the inputs are valid as [Boolean]
     */
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
    /**
     * Method used to get the list of the custom parameters to use in the [signIn] request.
     *
     * The order of the custom parameters must be the same of that specified in your customization of the
     * [getSignUpValuesKeys()](https://github.com/N7ghtm4r3/Equinox/blob/main/src/main/java/com/tecknobit/equinox/environment/helpers/services/EquinoxUsersHelper.java#L133)
     * method
     *
     **/
    @CustomParametersOrder(LANGUAGE_KEY)
    override fun getSignInCustomParameters(): Array<out Any?> {
        return arrayOf(getCurrentLocaleLanguage().substring(0, 2))
    }

    /**
     * Method used to launch the application after the authentication request, will be instantiated with the user details
     * both the [requester] and the [localUser]
     *
     * @param response The response of the authentication request
     * @param name The name of the user
     * @param surname The surname of the user
     * @param language The language of the user
     * @param custom The custom parameters added in a customization of the equinox user
     */
    @RequiresSuperCall
    override fun launchApp(
        response: JsonObject,
        name: String,
        surname: String,
        language: String,
        vararg custom: Any?,
    ) {
        super.launchApp(response, name, surname, language, *custom)
        navigator.navigate(HOME_SCREEN)
    }

}