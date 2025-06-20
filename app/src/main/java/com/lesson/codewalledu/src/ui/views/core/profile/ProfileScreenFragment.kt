package com.lesson.codewalledu.src.ui.views.core.profile

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.lesson.codewalledu.R
import com.lesson.codewalledu.databinding.FragmentLearnScreenBinding
import com.lesson.codewalledu.databinding.FragmentProfileScreenBinding
import com.lesson.codewalledu.src.utils.APP_COLOR_BLUE
import com.lesson.codewalledu.src.utils.APP_COLOR_CYAN
import com.lesson.codewalledu.src.utils.helpers.applyGradient
import com.lesson.codewalledu.src.utils.helpers.handleOnBackPressed
import com.lesson.codewalledu.src.utils.helpers.visible


class ProfileScreenFragment : Fragment(R.layout.fragment_profile_screen) {
    lateinit var binding: FragmentProfileScreenBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentProfileScreenBinding.bind(view)

        binding.myLearningCourseToolbar.toolbarTitle.text = resources.getString(R.string.learn)

        binding.myLearningCourseToolbar.backIcon.visible(false)

        handleOnBackPressed(R.id.action_profileScreenFragment_to_homeScreenFragment)
    }
}