package pl.dziczyzna.login.domain.repository

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.GenericTypeIndicator
import com.google.firebase.database.ValueEventListener
import io.reactivex.Completable
import pl.dziczyzna.database.DatabaseProvider
import pl.dziczyzna.login.domain.error.LoginAlreadyTakenException
import java.lang.IllegalStateException

internal class UserRepository(private val database: DatabaseProvider) {

    fun createUser(login: String): Completable {
        return Completable.create { emitter ->
            val database = database.getUsersDatabase()
            database.addListenerForSingleValueEvent(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.children.any { it.value == login }) {
                        emitter.onError(LoginAlreadyTakenException())
                    } else {
                        val users = snapshot.getValue(object : GenericTypeIndicator<ArrayList<String>>() {}) ?: mutableListOf()
                        users.add(login)
                        database.setValue(users)
                            .addOnSuccessListener { emitter.onComplete() }
                            .addOnCanceledListener { emitter.onError(IllegalStateException("Failed to add firebase user")) }
                            .addOnFailureListener { exception -> emitter.onError(exception) }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    emitter.onError(error.toException())
                }
            })
        }
    }
}