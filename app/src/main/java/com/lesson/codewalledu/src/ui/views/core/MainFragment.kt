package com.lesson.codewalledu.src.ui.views.core

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.lesson.codewalledu.R
import com.lesson.codewalledu.databinding.FragmentMainBinding


class MainFragment : Fragment(R.layout.fragment_main) {


    companion object{
        lateinit var binding: FragmentMainBinding
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentMainBinding.bind(view)



        val navHostFragment = childFragmentManager.findFragmentById(R.id.bottom_nav_fragment_container_view) as NavHostFragment
        val navController = navHostFragment.navController
        binding.bottomNavView.setupWithNavController(navController)


        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.homeFragment || destination.id == R.id.learnFragment || destination.id == R.id.blogsFragment || destination.id ==R.id.profileFragment) { // Replace with your details_fragment ID
                binding.bottomNavView.visibility = View.VISIBLE} else {
                binding.bottomNavView.visibility = View.GONE
            }

            if (destination.id == R.id.homeFragment) {
                binding.fabLiveChat.visibility = View.VISIBLE
            } else {
                binding.fabLiveChat.visibility = View.GONE
            }
        }





    }
}