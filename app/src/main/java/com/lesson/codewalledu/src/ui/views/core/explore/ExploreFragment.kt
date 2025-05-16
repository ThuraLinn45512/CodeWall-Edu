package com.lesson.codewalledu.src.ui.views.core.explore

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import com.lesson.codewalledu.R
import com.lesson.codewalledu.databinding.FragmentExploreBinding
import com.lesson.codewalledu.src.ui.views.core.home.HomeViewModel
import com.lesson.codewalledu.src.utils.APP_COLOR_BLUE
import com.lesson.codewalledu.src.utils.APP_COLOR_CYAN
import com.lesson.codewalledu.src.utils.helpers.applyGradient

class ExploreFragment : Fragment(R.layout.fragment_explore) {

    private val viewModel: HomeViewModel by viewModels()
    lateinit var binding: FragmentExploreBinding


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentExploreBinding.bind(view)
        binding.blogToolbar.toolbarTitle.text = resources.getString(R.string.content)
        binding.blogToolbar.toolbarTitle.applyGradient(APP_COLOR_BLUE, APP_COLOR_CYAN)

    }
}