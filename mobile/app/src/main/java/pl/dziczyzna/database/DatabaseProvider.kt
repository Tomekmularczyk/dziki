package pl.dziczyzna.database

import com.google.firebase.database.DatabaseReference

internal interface DatabaseProvider {

    fun getUsersDatabase(): DatabaseReference

    fun getReportsDatabase() : DatabaseReference
}