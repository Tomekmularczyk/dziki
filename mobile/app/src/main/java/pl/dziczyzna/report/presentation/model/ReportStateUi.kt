package pl.dziczyzna.report.presentation.model

import android.graphics.Bitmap
import pl.dziczyzna.report.domain.model.PigType
import pl.dziczyzna.report.domain.model.PigCount

internal data class ReportStateUi(
    val city: String = "",
    val state: String = "",
    val latitude: Double? = null,
    val longitude: Double? = null,
    val time: String,
    val date: String,
    val type: PigType = PigType.ALIVE,
    val count: PigCount = PigCount.SINGLE,
    val image: Bitmap? = null,
    val imageUrl: String? = null
)