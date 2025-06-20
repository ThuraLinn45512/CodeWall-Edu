package com.lesson.codewalledu.src.ui.views.onboarding

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.lesson.codewalledu.R
import com.lesson.codewalledu.databinding.FragmentSplashBinding
import com.lesson.codewalledu.src.utils.APP_COLOR_BLUE
import com.lesson.codewalledu.src.utils.APP_COLOR_CYAN
import com.lesson.codewalledu.src.utils.UserPreferences
import com.lesson.codewalledu.src.utils.helpers.applyGradient
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch


class SplashFragment : Fragment() {


    private lateinit var binding: FragmentSplashBinding
    private val COLOR_BLUE = 0xFF0079BD.toInt()
    private val COLOR_CYAN = 0xFF28D4DF.toInt()
    private lateinit var userPreferences: UserPreferences


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        binding = FragmentSplashBinding.bind(view)
        userPreferences = UserPreferences(requireContext())

//        val text = binding.appNameTvSplashScreen
//        val paint = text.paint
//        val textWidth = paint.measureText(text.text.toString())
//        val shader = LinearGradient(
//            10f, 20f, textWidth, text.textSize,
//            intArrayOf(
//                Color.parseColor("#0079BD"),
//                Color.parseColor("#28D4DF"),
//            ),
//            null,
//            Shader.TileMode.CLAMP)
//        paint.shader = shader

//        binding.appNameTvSplashScreen.applyGradient(COLOR_BLUE,COLOR_CYAN)


        binding.appNameTvSplashScreen.applyGradient(APP_COLOR_BLUE, APP_COLOR_CYAN)

        Handler(Looper.getMainLooper()).postDelayed(
            {
                lifecycleScope.launch {
                    // Combine onboarding and login status flows
                    combine(
                        userPreferences.isOnboardingFinishedFlow,
                        userPreferences.isUserLoggedInFlow
                    ) { isOnboardingFinished, isLoggedIn ->
                        // Navigation logic based on status
                        when {
                            isLoggedIn -> {
                                findNavController().navigate(R.id.action_splashFragment_to_mainFragment)
                            }
                            isOnboardingFinished -> {
                                findNavController().navigate(R.id.action_splashFragment_to_loginFragment)
                            }
                            else -> {
                                findNavController().navigate(R.id.action_splashFragment_to_viewPagerFragment)
                            }
                        }
                    }.collect() // Start collecting the combined flow
                }

            },
            1000)
    }

    private fun onBoardingFinished():Boolean{
        val sharedPreference = activity?.getSharedPreferences("onBoarding", Context.MODE_PRIVATE)
        return sharedPreference!!.getBoolean("Finished",false)
    }



}