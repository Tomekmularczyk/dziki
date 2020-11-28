package pl.dziczyzna.login.presentation.mapper

import android.content.res.Resources
import pl.dziczyzna.R
import pl.dziczyzna.login.domain.error.LoginAlreadyTakenException
import pl.dziczyzna.login.presentation.model.CreateUserUi.Error

internal class CreateUserErrorMapper(private val resources: Resources) {

    fun mapError(throwable: Throwable): Error {
        return when (throwable) {
            is LoginAlreadyTakenException -> Error.UserInvalidError(resources.getString(R.string.login_error))
            else                          -> Error.UserInvalidError(resources.getString(R.string.io_error))
        }
    }
}