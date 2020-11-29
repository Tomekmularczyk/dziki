package pl.dziczyzna.report.presentation.model

internal sealed class SendReportStateUi {
    object InProgress : SendReportStateUi()
    object Success : SendReportStateUi()
    data class Error(val throwable: Throwable) : SendReportStateUi()
}