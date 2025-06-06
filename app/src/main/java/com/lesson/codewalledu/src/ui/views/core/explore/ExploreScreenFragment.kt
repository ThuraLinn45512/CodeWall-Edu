package com.lesson.codewalledu.src.ui.views.core.explore

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import com.lesson.codewalledu.R
import com.lesson.codewalledu.databinding.FragmentExploreScreenBinding

import com.lesson.codewalledu.src.ui.views.core.explore.blogs.BlogsFragment
import com.lesson.codewalledu.src.ui.views.core.explore.books.BooksFragment
import com.lesson.codewalledu.src.ui.views.core.explore.cheat_sheets.CheatSheetsFragment
import com.lesson.codewalledu.src.ui.views.core.explore.news.NewsFragment
import com.lesson.codewalledu.src.utils.adapters.ViewPagerAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ExploreScreenFragment : Fragment(R.layout.fragment_explore_screen) {


    lateinit var binding: FragmentExploreScreenBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentExploreScreenBinding.bind(view)
        binding.blogToolbar.toolbarTitle.text = "Explore the world"

        binding.blogToolbar.toolbarTitle.setOnClickListener {
//            findNavController().navigate(R.id.action_exploreScreenFragment_to_fragmentErrorTestFragment)


        }


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





    }
}