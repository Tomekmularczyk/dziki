package pl.dziczyzna.report.presentation.model

internal sealed class SendReportStateUi {
    object InProgress : SendReportStateUi()
    data class Success(val city: String) : SendReportStateUi()
    data class Error(val throwable: Throwable) : SendReportStateUi()
}