package com.lesson.codewalledu.src.ui.views.core.home.courses

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.navigation.fragment.findNavController
import com.lesson.codewalledu.R
import com.lesson.codewalledu.databinding.FragmentCurrentCoursesBinding
import com.lesson.codewalledu.databinding.LayoutCwCourseCardThreeBinding
import com.lesson.codewalledu.src.utils.adapters.BaseRecyclerViewAdapter
import com.lesson.codewalledu.src.utils.APP_COLOR_BLUE
import com.lesson.codewalledu.src.utils.APP_COLOR_CYAN
import com.lesson.codewalledu.src.utils.helpers.applyGradient
import com.lesson.codewalledu.src.utils.helpers.handleOnBackPressed
import com.lesson.codewalledu.src.utils.helpers.visible

class CurrentCoursesFragment : Fragment(R.layout.fragment_current_courses) {



    private val viewModel: CurrentCoursesViewModel by viewModels()
    lateinit var binding: FragmentCurrentCoursesBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentCurrentCoursesBinding.bind(view)


        binding.toolbar.backIcon.visible(true)
        binding.toolbar.toolbarTitle.text = "Popular Courses"
        binding.toolbar.toolbarTitle.applyGradient(APP_COLOR_BLUE, APP_COLOR_CYAN)


        binding.toolbar.backIcon.setOnClickListener {
            findNavController().navigate(R.id.action_currentCoursesFragment_to_exploreFragment)
        }





        handleOnBackPressed(R.id.action_currentCoursesFragment_to_exploreFragment)

        var currentCoursesAdapter = BaseRecyclerViewAdapter(R.layout.layout_cw_course_card_three, false, viewModel.getCurrentCoursesList())
        { position, data, view ->
            val itemBinding = LayoutCwCourseCardThreeBinding.bind(view)
            itemBinding.apply {
                ivSubCourse.setImageResource(data.image)
                tvTitleSubCourse.text = data.title
                tvDurationSubCourse.text = data.time
                tvChapterCountSubCourse.text = data.chapters
                tvPriceSubCourse.text = data.fees
            }
        }
        binding.rvCurrentCourses.adapter = currentCoursesAdapter



    }


}