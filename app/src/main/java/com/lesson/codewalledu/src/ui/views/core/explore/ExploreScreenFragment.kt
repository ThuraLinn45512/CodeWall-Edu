package com.lesson.codewalledu.src.ui.views.core.explore


import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.lesson.codewalledu.R
import com.lesson.codewalledu.databinding.FragmentExploreScreenBinding
import com.lesson.codewalledu.src.ui.views.core.explore.blogs.BlogsFragment
import com.lesson.codewalledu.src.ui.views.core.explore.books.BooksFragment
import com.lesson.codewalledu.src.ui.views.core.explore.cheat_sheets.CheatSheetsFragment
import com.lesson.codewalledu.src.ui.views.core.explore.news.NewsFragment
import com.lesson.codewalledu.src.utils.adapters.ViewPagerAdapter
import com.lesson.codewalledu.src.utils.helpers.handleOnBackPressed
import com.lesson.codewalledu.src.utils.helpers.visible
import com.lesson.codewalledu.src.utils.setViewPagerScrollSpeed
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ExploreScreenFragment : Fragment(R.layout.fragment_explore_screen) {


    lateinit var binding: FragmentExploreScreenBinding
    private lateinit var viewPager: ViewPager2

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentExploreScreenBinding.bind(view)
        binding.exploreToolbar.toolbarTitle.text = "Explore the world"

        binding.exploreToolbar.backIcon.visible(false)

        handleOnBackPressed(R.id.action_exploreScreenFragment_to_homeScreenFragment)






        val list: ArrayList<Fragment> = ArrayList<Fragment>()
        list.add(BlogsFragment())
        list.add(NewsFragment())
        list.add(BooksFragment())
        list.add(CheatSheetsFragment())
        // Set tab titles here
        val tabTitles = listOf("Blogs", "News", "Books", "Discovers") // Create a list of titles

        val viewPagerAdapter = ViewPagerAdapter(requireActivity(),list)
        binding.viewpagerExplore.adapter = viewPagerAdapter


        TabLayoutMediator(binding.tabLayout, binding.viewpagerExplore) { tab, position ->
            tab.text = tabTitles[position]
        }.attach()


        viewPager = binding.viewpagerExplore
        setViewPagerScrollSpeed(viewPager, 600)


        viewPager.isUserInputEnabled = false





    }
}