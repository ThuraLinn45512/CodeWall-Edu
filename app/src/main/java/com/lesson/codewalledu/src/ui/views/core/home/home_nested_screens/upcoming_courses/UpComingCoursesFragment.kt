package com.lesson.codewalledu.src.ui.views.core.home.home_nested_screens.upcoming_courses

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.navigation.fragment.findNavController
import com.lesson.codewalledu.R
import com.lesson.codewalledu.databinding.FragmentUpComingBinding
import com.lesson.codewalledu.databinding.LayoutCwCourseCardFourBinding
import com.lesson.codewalledu.src.ui.adapters.BaseRecyclerViewAdapter
import com.lesson.codewalledu.src.utils.APP_COLOR_BLUE
import com.lesson.codewalledu.src.utils.APP_COLOR_CYAN
import com.lesson.codewalledu.src.utils.helpers.applyGradient
import com.lesson.codewalledu.src.utils.helpers.handleOnBackPressed
import com.lesson.codewalledu.src.utils.helpers.visible

class UpComingCoursesFragment : Fragment(R.layout.fragment_up_coming) {

    lateinit var binding: FragmentUpComingBinding

    private val viewModel: UpComingCoursesViewModel by viewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentUpComingBinding.bind(view)

        binding.toolbar.backIcon.visible(true)
        binding.toolbar.toolbarTitle.text = "Upcoming Courses"
        binding.toolbar.toolbarTitle.applyGradient(APP_COLOR_BLUE, APP_COLOR_CYAN)
        binding.toolbar.backIcon.setOnClickListener {
            findNavController().navigate(R.id.action_upComingFragment_to_exploreFragment)
        }

//        backPressedOnFragment()
        handleOnBackPressed(R.id.action_upComingFragment_to_exploreFragment)



        var upcomingCoursesAdapter = BaseRecyclerViewAdapter(R.layout.layout_cw_course_card_four, false, viewModel.getUpComingCourses())
        { position, data, view ->
            val itemBinding = LayoutCwCourseCardFourBinding.bind(view)
            itemBinding.apply {
                imageView3.setImageResource(data.image)
                tvTitleUpcomingCourse.text = data.title
                tvDateUpcomingCourses.text = data.date
            }

        }
        binding.rvUpcomingCourses.adapter = upcomingCoursesAdapter
    }

//    private fun backPressedOnFragment() {
//        val onBackPressedDispatcher = requireActivity().onBackPressedDispatcher
//
//        onBackPressedDispatcher.addCallback(
//            viewLifecycleOwner,
//            object : OnBackPressedCallback(true) {
//                override fun handleOnBackPressed() {
//                    findNavController().navigate(R.id.action_upComingFragment_to_exploreFragment)
//                }
//            })
//    }


}