package pl.dziczyzna.login.presentation.model

internal sealed class CreateUserUi {
    object InProgress : CreateUserUi()
    data class Success(val login: String) : CreateUserUi()
    sealed class Error : CreateUserUi() {
        data class UserInvalidError(val message: String) : Error()
        data class IoError(val message: String) : Error()
    }
}