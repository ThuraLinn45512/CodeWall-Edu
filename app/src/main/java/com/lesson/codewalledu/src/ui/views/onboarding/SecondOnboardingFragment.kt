package com.lesson.codewalledu.src.ui.views.onboarding

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.AdapterView
import androidx.fragment.app.activityViewModels
import androidx.viewpager2.widget.ViewPager2
import com.lesson.codewalledu.R
import com.lesson.codewalledu.databinding.FragmentSecondOnboardingBinding
import com.lesson.codewalledu.src.utils.APP_COLOR_BLUE
import com.lesson.codewalledu.src.utils.APP_COLOR_CYAN
import com.lesson.codewalledu.src.utils.helpers.applyGradient
import java.util.Locale

class SecondOnboardingFragment : Fragment(R.layout.fragment_second_onboarding) {

    lateinit var binding: FragmentSecondOnboardingBinding
    private val viewModel: SharedViewModel by activityViewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentSecondOnboardingBinding.bind(view)
        binding.titleSecondScreenOnboarding.applyGradient(APP_COLOR_BLUE, APP_COLOR_CYAN)


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

                    binding.titleSecondScreenOnboarding.text =
                        getString(R.string.title_second_onboarding_screen)
                    binding.subtitleSecondOnboardingScreen.text =
                        getString(R.string.subtitle_second_onboarding_screen)

                }


                override fun onNothingSelected(parent: AdapterView<*>) {
                    // Do nothing
                }
            }


        val viewPager = activity?.findViewById<ViewPager2>(R.id.viewpager_onboarding)


        binding.btnSkip2.setOnClickListener {
            viewPager?.currentItem = 2
        }
    }

    fun setLanguage(language: String) {
        val resources = resources
        val config = resources.configuration
        val locale = Locale(language)
        Locale.setDefault(locale)


        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)
    }


}


