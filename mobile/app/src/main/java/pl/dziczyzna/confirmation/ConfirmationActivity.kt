package pl.dziczyzna.confirmation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import pl.dziczyzna.R

internal class ConfirmationActivity : AppCompatActivity(R.layout.activity_confirmation) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        findViewById<Toolbar>(R.id.toolbar).setNavigationOnClickListener { finish() }
        findViewById<TextView>(R.id.textGiftInfo).text = getString(R.string.gift_info, intent.getStringExtra(CITY))
    }

    companion object {

        private const val CITY = "city"

        fun newInstance(context: Context, city: String): Intent {
            return Intent(context, ConfirmationActivity::class.java).apply {
                putExtra(CITY, city)
            }
        }
    }
}