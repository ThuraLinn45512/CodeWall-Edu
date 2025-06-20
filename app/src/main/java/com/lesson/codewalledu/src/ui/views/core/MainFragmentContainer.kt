package com.lesson.codewalledu.src.ui.views.core

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.lesson.codewalledu.R
import com.lesson.codewalledu.databinding.FragmentMainContainerBinding


class MainFragmentContainer : Fragment(R.layout.fragment_main_container) {


    companion object{
        lateinit var binding: FragmentMainContainerBinding
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentMainContainerBinding.bind(view)



        val navHostFragment = childFragmentManager.findFragmentById(R.id.bottom_nav_fragment_container_view) as NavHostFragment
        val navController = navHostFragment.navController
        binding.bottomNavView.setupWithNavController(navController)


        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.homeScreenFragment, // Corrected: This is your HomeScreenFragment's ID in core_nav.xml
                R.id.exploreScreenFragment,
                R.id.learnScreenFragment,
                R.id.profileScreenFragment -> { // To show BottomNav on the error test fragment
                    binding.bottomNavView.visibility = View.VISIBLE
                }
                else -> {
                    binding.bottomNavView.visibility = View.GONE
                }
            }

            if (destination.id == R.id.homeScreenFragment) {
                binding.fabLiveChat.visibility = View.VISIBLE
            } else {
                binding.fabLiveChat.visibility = View.GONE
            }
        }





    }
}