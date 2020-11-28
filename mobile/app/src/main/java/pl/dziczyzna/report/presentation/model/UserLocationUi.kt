package pl.dziczyzna.report.presentation.model

internal sealed class UserLocationUi {

    object InProgress : UserLocationUi()
    object Success : UserLocationUi()
    data class Error(val throwable: Throwable) : UserLocationUi()
}