package pl.dziczyzna.onboarding

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.viewpager.widget.ViewPager
import pl.dziczyzna.R

class StepLayout : LinearLayout {

    private var viewPager: ViewPager? = null
    private var onPageChangeListener: ViewPager.OnPageChangeListener? = null
    private val activeColor: Int
    private val notActiveColor: Int

    private var adapterPosition = -1

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        this.activeColor = ContextCompat.getColor(context, R.color.color_primary)
        this.notActiveColor = ContextCompat.getColor(context, R.color.disable_icon)

        gravity = Gravity.CENTER
        orientation = HORIZONTAL
    }

    fun setViewAnimator(viewPager: ViewPager) {
        if (viewPager.adapter == null) {
            throw IllegalStateException("Adapter has not set adapter, set adapter first.")
        }

        this.viewPager?.removeOnPageChangeListener(onPageChangeListener!!)
        this.viewPager = viewPager
        this.onPageChangeListener = createOnPageChangedListener()
        this.viewPager?.addOnPageChangeListener(onPageChangeListener!!)
        this.adapterPosition = viewPager.currentItem

        drawSteps()
    }

    private fun drawSteps() {
        removeAllViews()

        val size = viewPager?.adapter?.count ?: 0
        val layoutInflater = LayoutInflater.from(context)

        for (index in 0 until size) {
            val stepView = layoutInflater.inflate(R.layout.view_step, this, false)
            DrawableCompat.setTint(stepView.background, if (index == adapterPosition) activeColor else notActiveColor)
            addView(stepView)
        }
    }



    private fun createOnPageChangedListener() = object : ViewPager.SimpleOnPageChangeListener() {

        override fun onPageSelected(position: Int) {
            if (adapterPosition != -1) {
                DrawableCompat.setTint(getChildAt(adapterPosition).background, notActiveColor)
            }

            adapterPosition = position
            DrawableCompat.setTint(getChildAt(adapterPosition).background, activeColor)
        }
    }
}