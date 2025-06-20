package com.lesson.codewalledu.src.ui.views.core.learn

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.navigation.fragment.findNavController
import com.lesson.codewalledu.R
import com.lesson.codewalledu.databinding.FragmentLearnScreenBinding
import com.lesson.codewalledu.src.utils.*
import com.lesson.codewalledu.src.utils.helpers.applyGradient
import com.lesson.codewalledu.src.utils.helpers.handleOnBackPressed
import com.lesson.codewalledu.src.utils.helpers.visible

class LearnScreenFragment : Fragment(R.layout.fragment_learn_screen) {
    lateinit var binding: FragmentLearnScreenBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentLearnScreenBinding.bind(view)

        binding.myLearningCourseToolbar.toolbarTitle.text = resources.getString(R.string.profile)

        binding.myLearningCourseToolbar.backIcon.visible(false)

        handleOnBackPressed(R.id.action_learnScreenFragment_to_homeScreenFragment)
    }
}