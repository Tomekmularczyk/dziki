package pl.dziczyzna

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import org.koin.android.ext.android.get
import pl.dziczyzna.login.domain.preferences.UserPreferences
import pl.dziczyzna.login.presentation.LoginActivity
import pl.dziczyzna.login.presentation.LoginFragment
import pl.dziczyzna.report.ReportFragment

internal class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //setSupportActionBar(findViewById(R.id.toolbar))

        /*if (savedInstanceState == null) {
            if (get<UserPreferences>().getUserLogin().isNullOrBlank()) {
                startActivityForResult(LoginActivity.newIntent(this), LOGIN_ACTIVITY_RESULT)
            }
        }*/

        supportFragmentManager.beginTransaction()
            .add(R.id.fragmentContainer, ReportFragment()).commit()
    }

    companion object {
        const val LOGIN_ACTIVITY_RESULT = 1
    }
}