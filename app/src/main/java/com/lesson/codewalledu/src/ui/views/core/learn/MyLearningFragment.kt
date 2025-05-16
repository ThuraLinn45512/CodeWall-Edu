package com.lesson.codewalledu.src.ui.views.core.learn

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import com.lesson.codewalledu.R
import com.lesson.codewalledu.databinding.FragmentLearnBinding
import com.lesson.codewalledu.src.utils.*
import com.lesson.codewalledu.src.utils.helpers.applyGradient

class MyLearningFragment : Fragment(R.layout.fragment_learn) {
    lateinit var binding: FragmentLearnBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentLearnBinding.bind(view)

        binding.myLearningCourseToolbar.toolbarTitle.text = resources.getString(R.string.learn)
        binding.myLearningCourseToolbar.toolbarTitle.applyGradient(APP_COLOR_BLUE, APP_COLOR_CYAN)
    }
}