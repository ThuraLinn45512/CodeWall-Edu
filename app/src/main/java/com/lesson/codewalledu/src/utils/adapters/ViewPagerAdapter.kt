package com.lesson.codewalledu.src.utils.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.lesson.codewalledu.src.ui.views.onboarding.FirstOnboardingFragment
import com.lesson.codewalledu.src.ui.views.onboarding.SecondOnboardingFragment
import com.lesson.codewalledu.src.ui.views.onboarding.ThirdOnboardingFragment

class ViewPagerAdapter(
    fragmentActivity: FragmentActivity,
    private val fragmentList: List<Fragment>, // Use List instead of ArrayList
) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int = fragmentList.size

    override fun createFragment(position: Int): Fragment = fragmentList[position]


}