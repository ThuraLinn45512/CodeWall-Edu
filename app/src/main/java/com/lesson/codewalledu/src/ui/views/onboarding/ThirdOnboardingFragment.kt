package com.lesson.codewalledu.src.ui.views.onboarding

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.lesson.codewalledu.R
import com.lesson.codewalledu.databinding.FragmentThirdOnboardingBinding
import com.lesson.codewalledu.src.utils.APP_COLOR_BLUE
import com.lesson.codewalledu.src.utils.APP_COLOR_CYAN
import com.lesson.codewalledu.src.utils.UserPreferences
import com.lesson.codewalledu.src.utils.helpers.applyGradient
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class ThirdOnboardingFragment : Fragment() {

    lateinit var binding: FragmentThirdOnboardingBinding
    private val viewModel: SharedViewModel by activityViewModels()

    @Inject
    lateinit var userPreferences: UserPreferences


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_third_onboarding, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentThirdOnboardingBinding.bind(view)
        binding.titleThirdScreenOnboarding.applyGradient(APP_COLOR_BLUE, APP_COLOR_CYAN)

//        var sharedPreferences = requireContext().getSharedPreferences("language", Context.MODE_PRIVATE)
//        var languagePosition = sharedPreferences.getInt("language_position",0)



        createSpinner(requireContext(), binding.languageSpinner)
//        binding.languageSpinner.setSelection(languagePosition)

        viewModel.position.observe(viewLifecycleOwner) {
            binding.languageSpinner.setSelection(it)
        }

        createSpinner(
            requireContext(),
            binding.languageSpinner,
        )



        binding.languageSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View?,
                    position: Int,
                    id: Long
                ) {

                    viewModel.position.value = position


//                    val editor =
//                        sharedPreferences.edit()
//                    editor.putInt("language_position", position)
//                    editor.apply()

                    var language = when (position) {
                        0 -> "en"

                        1 -> "my"

                        2 -> "ja"

                        else -> "en"

                    }

                    setLanguage(language)

                    binding.titleThirdScreenOnboarding.text =
                        getString(R.string.title_third_onboarding_screen)
                    binding.subtitleThirdOnboardingScreen.text =
                        getString(R.string.subtitle_third_onboarding_screen)

                }


                override fun onNothingSelected(parent: AdapterView<*>) {
                    // Do nothing
                }
            }


        ///onboarding finish
        binding.btnGetStarted.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {
                userPreferences.setOnboardingFinished(true)
            }
            findNavController().navigate(R.id.action_viewPagerFragment_to_loginFragment)
//            onBoardingFinish()
        }
    }

//    private fun onBoardingFinish() {
//        val sharedPref = requireActivity().getSharedPreferences("onBoarding", Context.MODE_PRIVATE)
//        val editor = sharedPref.edit()
//        editor.putBoolean("Finished", true)
//        editor.apply()
//    }

    fun setLanguage(language: String) {
        val resources = resources
        val config = resources.configuration
        val locale = Locale(language)
        Locale.setDefault(locale)


        config.setLocale(locale)
        @Suppress("DEPRECATION")
        resources.updateConfiguration(config, resources.displayMetrics)
    }


}