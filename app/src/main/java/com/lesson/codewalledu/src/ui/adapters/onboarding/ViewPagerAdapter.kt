package com.lesson.codewalledu.src.ui.adapters.onboarding

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.lesson.codewalledu.src.ui.views.onboarding.FirstOnboardingFragment
import com.lesson.codewalledu.src.ui.views.onboarding.SecondOnboardingFragment
import com.lesson.codewalledu.src.ui.views.onboarding.ThirdOnboardingFragment

class ViewPagerAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment =
        when (position) {
            0 -> FirstOnboardingFragment()
            1 -> SecondOnboardingFragment()
            2 -> ThirdOnboardingFragment()
            else -> throw IllegalStateException()
        }
}