package pl.dziczyzna.login.presentation

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import pl.dziczyzna.R

internal class LoginActivity : AppCompatActivity(R.layout.activity_login) {

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, LoginActivity::class.java)
        }
    }
}