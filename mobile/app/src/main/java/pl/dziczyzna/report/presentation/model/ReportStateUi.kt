package pl.dziczyzna.report.presentation.model

import pl.dziczyzna.report.domain.model.PigType
import pl.dziczyzna.report.domain.model.PigCount

internal data class ReportStateUi(
    val city: String = "",
    val state: String = "",
    val time: String,
    val date: String,
    val type: PigType = PigType.ALIVE,
    val count: PigCount = PigCount.SINGLE
)