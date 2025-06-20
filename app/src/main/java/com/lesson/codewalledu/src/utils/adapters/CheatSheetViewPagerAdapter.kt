// In src/main/java/com/lesson/codewalledu/src/ui/views/core/explore/cheat_sheets/CheatSheetViewPagerAdapter.kt
package com.lesson.codewalledu.src.utils.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.lesson.codewalledu.src.data.models.core.explore.DiscoveryContent
import com.lesson.codewalledu.src.ui.views.core.explore.cheat_sheets.CheatSheetImageFragment

class CheatSheetViewPagerAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
    private val cheatSheetContents: List<DiscoveryContent>
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int {
        return cheatSheetContents.size
    }

    override fun createFragment(position: Int): Fragment {
        val imageUrl = cheatSheetContents[position].imageUrl
        return CheatSheetImageFragment.newInstance(imageUrl)
    }
}