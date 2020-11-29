package pl.dziczyzna.onboarding

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import pl.dziczyzna.R

internal class OnboardingFragment : Fragment(R.layout.fragment_onboarding) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val title = view.findViewById<TextView>(R.id.textOnboardingTitle)
        val subtitle = view.findViewById<TextView>(R.id.textOnboardingSubtitle)
        val image = view.findViewById<ImageView>(R.id.imageOnboardingImage)

        title.setText(arguments!!.getInt(TITLE))
        subtitle.setText(arguments!!.getInt(SUBTITLE))
        image.setImageResource(arguments!!.getInt(IMAGE))
    }

    companion object {
        private const val TITLE = "title"
        private const val SUBTITLE = "subtitle"
        private const val IMAGE = "IMAGE"

        fun createFragment(@StringRes title: Int, @StringRes subtitle: Int, @DrawableRes imageRes: Int): Fragment {
            val args = Bundle()
            args.putInt(TITLE, title)
            args.putInt(SUBTITLE, subtitle)
            args.putInt(IMAGE, imageRes)

            return OnboardingFragment().apply { arguments = args }
        }
    }
}