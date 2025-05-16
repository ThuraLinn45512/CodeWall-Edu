package com.lesson.codewalledu.src.ui.adapters.core

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.lesson.codewalledu.src.ui.views.core.explore.ExploreFragment
import com.lesson.codewalledu.src.ui.views.core.tools.ToolsFragment
import com.lesson.codewalledu.src.ui.views.core.learn.MyLearningFragment


class BottomNavigationViewPagerAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int = 4

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0-> com.lesson.codewalledu.src.ui.views.core.home.HomeFragment()
            1-> MyLearningFragment()
            2-> ExploreFragment()
            3-> ToolsFragment()
            else -> throw IllegalStateException()
        }
    }
}