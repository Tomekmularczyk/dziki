package pl.dziczyzna.database

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

internal class DatabaseProviderImpl : DatabaseProvider {

    override fun getUsersDatabase(): DatabaseReference {
        return FirebaseDatabase.getInstance().getReference(DATABASE_USERS)
    }

    private companion object {
        const val DATABASE_USERS = "users"
    }
}