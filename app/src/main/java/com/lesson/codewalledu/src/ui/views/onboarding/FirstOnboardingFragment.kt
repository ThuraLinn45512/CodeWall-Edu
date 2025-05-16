package com.lesson.codewalledu.src.ui.views.onboarding

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.activityViewModels
import androidx.viewpager2.widget.ViewPager2
import com.lesson.codewalledu.R
import com.lesson.codewalledu.databinding.FragmentFirstOnboardingBinding
import com.lesson.codewalledu.src.utils.APP_COLOR_BLUE
import com.lesson.codewalledu.src.utils.APP_COLOR_CYAN
import com.lesson.codewalledu.src.utils.helpers.applyGradient
import java.util.Locale


class FirstOnboardingFragment : Fragment() {
    lateinit var binding: FragmentFirstOnboardingBinding
    private val viewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_first_onboarding, container, false)
        // Inflate the layout for this fragment
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentFirstOnboardingBinding.bind(view)

        ///gradient color code
        binding.titleFirstScreenOnboarding.applyGradient(APP_COLOR_BLUE, APP_COLOR_CYAN)


//        var sharedPreferences = requireContext().getSharedPreferences("language", Context.MODE_PRIVATE)
//        var languagePosition = sharedPreferences.getInt("language_position",0)
        createSpinner(requireContext(), binding.languageSpinner)
//        binding.languageSpinner.setSelection(languagePosition)



        viewModel.position.observe(viewLifecycleOwner) {
            binding.languageSpinner.setSelection(it)
        }


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
                        0 -> {
                            "en"
                        }

                        1 -> {
                            "my"

                        }

                        2 -> {
                            "ja"

                        }

                        else -> {
                            "en"

                        }

                    }

                    setLanguage(language)

                    binding.titleFirstScreenOnboarding.text = getString(R.string.title_first_onboarding_screen)
                    binding.subtitleFirstOnboardingScreen.text =
                        getString(R.string.subtitle_first_onboarding_screen)

                }


                override fun onNothingSelected(parent: AdapterView<*>) {
                    // Do nothing
                }
            }



        ///view pager code
        val viewPager = activity?.findViewById<ViewPager2>(R.id.viewpager_onboarding)
        binding.btnSkip.setOnClickListener {
            viewPager?.currentItem = 2
        }


    }

    fun setLanguage(language: String) {
        Log.d("TAG", "setLanguage: $language")
        val resources = getResources()
        val config = resources.configuration
        val locale = Locale(language)
        Locale.setDefault(locale)


        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)

    }


}




