package pl.dziczyzna

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import pl.dziczyzna.onboarding.OnboardingActivity

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

        /*supportFragmentManager.beginTransaction()
            .add(R.id.fragmentContainer, ReportFragment()).commit()*/

        startActivity(OnboardingActivity.newInstance(this))
    }

    companion object {
        const val LOGIN_ACTIVITY_RESULT = 1
    }
}