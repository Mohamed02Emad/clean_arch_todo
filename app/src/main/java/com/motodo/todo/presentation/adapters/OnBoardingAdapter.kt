package com.motodo.todo.presentation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter
import com.motodo.todo.R
import com.motodo.todo.domain.models.OnBoarding

class OnBoardingAdapter(private val onBoardings: List<OnBoarding>) : PagerAdapter() {

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val itemView = LayoutInflater.from(container.context).inflate(
            R.layout.onboarding_card, container, false
        )
        val onBoarding = onBoardings[position]
        val imgView = itemView.findViewById<ImageView>(R.id.iv_onboarding)
        val txtViewUpper = itemView.findViewById<TextView>(R.id.tv_upperText)
        val txtViewLower = itemView.findViewById<TextView>(R.id.tv_lowerText)

        imgView.setImageResource(onBoarding.image)
        txtViewUpper.text = onBoarding.title
        txtViewLower.text = onBoarding.description
        container.addView(itemView)
        return itemView
    }


    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun getCount(): Int {
        return onBoardings.size
    }


}
