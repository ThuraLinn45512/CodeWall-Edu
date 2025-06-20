package com.lesson.codewalledu.src.ui.views.core.explore.cheat_sheets

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.lesson.codewalledu.R
import com.lesson.codewalledu.databinding.FragmentViewCheatSheetBinding
import com.lesson.codewalledu.src.utils.adapters.CheatSheetViewPagerAdapter
import com.lesson.codewalledu.src.utils.helpers.handleOnBackPressed


class ViewCheatSheetFragment : Fragment(R.layout.fragment_view_cheat_sheet) {

    lateinit var binding: FragmentViewCheatSheetBinding
    val args: ViewCheatSheetFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentViewCheatSheetBinding.bind(view)
        val imageList = args.cheatSheet.toList()
        val position = args.position

        handleOnBackPressed(R.id.action_viewCheatSheetFragment_pop)

        // Initialize and set the new FragmentStateAdapter for ViewPager2
        val viewPagerAdapter = CheatSheetViewPagerAdapter(
            childFragmentManager, // Use childFragmentManager for nested fragments
            lifecycle,
            imageList
        )

        binding.viewpagerCheatSheet.adapter = viewPagerAdapter

        // Set the ViewPager2 to the clicked position WITHOUT a smooth scroll
        binding.viewpagerCheatSheet.setCurrentItem(position, false)

    }
}