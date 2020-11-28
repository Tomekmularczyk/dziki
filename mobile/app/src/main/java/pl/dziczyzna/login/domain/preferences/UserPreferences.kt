package pl.dziczyzna.login.domain.preferences

import android.content.Context

internal class UserPreferences(context: Context) {

    private val preferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)

    fun getUserLogin(): String? {
        return preferences.getString(USER_LOGIN_PREFERENCES_KEY, null)
    }

    fun saveUserLogin(userLogin: String) {
        preferences.edit().putString(USER_LOGIN_PREFERENCES_KEY, userLogin).apply()
    }

    private companion object {
        const val PREFERENCES_NAME = "userPreferences"
        const val USER_LOGIN_PREFERENCES_KEY = "userLogin"
    }
}