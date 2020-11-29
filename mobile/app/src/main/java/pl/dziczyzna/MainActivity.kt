package pl.dziczyzna

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import pl.dziczyzna.login.domain.preferences.UserPreferences
import pl.dziczyzna.login.presentation.LoginActivity
import pl.dziczyzna.onboarding.OnboardingActivity
import pl.dziczyzna.report.ReportActivity

internal class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<View>(R.id.fabAdd).setOnClickListener { startActivity(ReportActivity.newInstance(this)) }

        init()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when {
            requestCode == LOGIN_ACTIVITY_RESULT && resultCode == RESULT_CANCELED -> init()
            requestCode == LOGIN_ACTIVITY_RESULT && resultCode == RESULT_OK       -> startOnboarding()

        }
    }

    private fun init() {
        val userPreferences = UserPreferences(this)

        if (!userPreferences.getUserLogin().isNullOrBlank()) {
            startActivityForResult(LoginActivity.newIntent(this), LOGIN_ACTIVITY_RESULT)
        }
    }

    private fun startOnboarding() {
        startActivity(OnboardingActivity.newInstance(this))
    }

    private companion object {
        const val LOGIN_ACTIVITY_RESULT = 1
    }
}