package pl.dziczyzna.onboarding

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import pl.dziczyzna.R

internal class OnboardingPageAdapter(fragmentManager: FragmentManager) : FragmentStatePagerAdapter(
    fragmentManager,
    BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
) {

    private val items: MutableList<Fragment> = mutableListOf()

    init {
        items.add(
            OnboardingFragment.createFragment(
                R.string.onboarding_one_title,
                R.string.onboarding_one_subtitle,
                R.drawable.onboarding_one
            )
        )
        items.add(
            OnboardingFragment.createFragment(
                R.string.onboarding_two_title,
                R.string.onboarding_two_subtitle,
                R.drawable.onboarding_two
            )
        )
        items.add(
            OnboardingFragment.createFragment(
                R.string.onboarding_three_title,
                R.string.onboarding_three_subtitle,
                R.drawable.onboarding_three
            )
        )
    }

    override fun getItem(position: Int) = items[position]

    override fun getCount() = items.size
}