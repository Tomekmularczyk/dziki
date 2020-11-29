package pl.dziczyzna.report.domain.report

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.GenericTypeIndicator
import com.google.firebase.database.ValueEventListener
import io.reactivex.Completable
import pl.dziczyzna.database.DatabaseProvider
import pl.dziczyzna.login.domain.preferences.UserPreferences
import pl.dziczyzna.report.domain.model.PigCount
import pl.dziczyzna.report.domain.model.PigType
import pl.dziczyzna.report.presentation.model.ReportStateUi
import java.lang.IllegalStateException

internal class SendReport(
    private val userPreferences: UserPreferences,
    private val database: DatabaseProvider
) {

    fun sendReport(reportStateUi: ReportStateUi): Completable {
        return Completable.create { emitter ->
            val data = Report(
                count = reportStateUi.count.toString(),
                latitude = reportStateUi.latitude,
                longitude = reportStateUi.longitude,
                photoUrl = reportStateUi.imageUrl.orEmpty(),
                status = "NEW",
                timeStamp = System.currentTimeMillis(),
                title = "",
                type = reportStateUi.type.toString(),
                user = userPreferences.getUserLogin().orEmpty(),
                city = reportStateUi.city,
                state = reportStateUi.state
            )

            val database = database.getReportsDatabase()
            database.addListenerForSingleValueEvent(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {
                    val reports = snapshot.getValue(object : GenericTypeIndicator<ArrayList<Report>>() {}) ?: mutableListOf()
                    reports.add(data)
                    database.setValue(reports)
                        .addOnSuccessListener { emitter.onComplete() }
                        .addOnCanceledListener { emitter.onError(IllegalStateException("Failed to add report")) }
                        .addOnFailureListener { exception -> emitter.onError(exception) }
                }

                override fun onCancelled(error: DatabaseError) {
                    emitter.onError(error.toException())
                }
            })
        }
    }

    private data class Report(
        val count: String = PigCount.SINGLE.toString(),
        val latitude: Double? = null,
        val longitude: Double? = null,
        val photoUrl: String = "",
        val status: String = "NEW",
        val timeStamp: Long = System.currentTimeMillis(),
        val title: String = "",
        val type: String = PigType.ALIVE.toString(),
        val user: String = "",
        val city: String = "",
        val state: String = ""
    )
}