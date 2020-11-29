package pl.dziczyzna.onboarding

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import pl.dziczyzna.R

internal class OnboardingActivity : AppCompatActivity(R.layout.activity_onboarding) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewPager = findViewById<ViewPager>(R.id.viewPagerOnboarding)
        val stepper = findViewById<StepLayout>(R.id.stepLayoutOnboarding)
        val button = findViewById<Button>(R.id.buttonOnboardingNext)

        val onboardingAdapter = OnboardingPageAdapter(supportFragmentManager)
        viewPager.adapter = onboardingAdapter
        stepper.setViewAnimator(viewPager)

        button.setOnClickListener {
            if (viewPager.currentItem == onboardingAdapter.count - 1) {
                finish()
            } else {
                viewPager.currentItem = viewPager.currentItem + 1
            }
        }
    }

    companion object {

        fun newInstance(context: Context): Intent {
            return Intent(context, OnboardingActivity::class.java)
        }
    }
}