package com.lesson.codewalledu.src.ui.views.core.learn

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import com.lesson.codewalledu.R
import com.lesson.codewalledu.databinding.FragmentLearnScreenBinding
import com.lesson.codewalledu.src.utils.*
import com.lesson.codewalledu.src.utils.helpers.applyGradient

class LearnScreenFragment : Fragment(R.layout.fragment_learn_screen) {
    lateinit var binding: FragmentLearnScreenBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentLearnScreenBinding.bind(view)

        binding.myLearningCourseToolbar.toolbarTitle.text = resources.getString(R.string.learn)
        binding.myLearningCourseToolbar.toolbarTitle.applyGradient(APP_COLOR_BLUE, APP_COLOR_CYAN)
    }
}