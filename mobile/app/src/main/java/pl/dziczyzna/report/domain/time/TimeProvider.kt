package pl.dziczyzna.report.domain.time

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

internal class TimeProvider {

    private val timeFormat = SimpleDateFormat("HH.mm", Locale.getDefault())
    private val dateFormat = SimpleDateFormat("dd/MMMM/yyyy", Locale.getDefault())

    fun getCurrentDate(): String {
        return dateFormat.format(Date())
    }

    fun getCurrentTime(): String {
        return timeFormat.format(Date())
    }
}