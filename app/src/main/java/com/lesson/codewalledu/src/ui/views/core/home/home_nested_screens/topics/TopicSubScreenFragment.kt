package com.lesson.codewalledu.src.ui.views.core.home.home_nested_screens.topics

import android.annotation.SuppressLint
import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.lesson.codewalledu.R
import com.lesson.codewalledu.databinding.FragmentTopicSubScreenBinding
import com.lesson.codewalledu.databinding.LayoutCwCourseCardThreeBinding

import com.lesson.codewalledu.src.ui.adapters.BaseRecyclerViewAdapter
import com.lesson.codewalledu.src.utils.helpers.handleOnBackPressed
import com.lesson.codewalledu.src.utils.helpers.visible

class TopicSubScreenFragment : Fragment(R.layout.fragment_topic_sub_screen) {

    lateinit var binding: FragmentTopicSubScreenBinding

    private val viewModel: TopicSubScreenViewModel by viewModels()
    val args: TopicSubScreenFragmentArgs by navArgs()


    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentTopicSubScreenBinding.bind(view)
        binding.toolbar.backIcon.visible(true)


        binding.toolbar.backIcon.setOnClickListener {
            findNavController().navigate(R.id.action_topicFragment_to_exploreFragment)
        }

        handleOnBackPressed(R.id.action_topicFragment_to_exploreFragment)

//        binding.toolbar.toolbarTitle.applyGradient(APP_COLOR_BLUE, APP_COLOR_CYAN)
        val category = args.category
        binding.toolbar.toolbarTitle.text = " $category Courses"
        binding.toolbar.toolbarTitle.setTextAppearance(R.style.body_large)
        binding.toolbar.toolbarTitle.setTextColor(resources.getColor(R.color.blue, null))


        val subCoursesAdapter = BaseRecyclerViewAdapter(
            R.layout.layout_cw_course_card_three,
            false,
            viewModel.getSubCoursesList()
        ) { position, data, view ->
            val itemBinding = LayoutCwCourseCardThreeBinding.bind(view)
            itemBinding.apply {
                ivSubCourse.setImageResource(data.image)
                tvTitleSubCourse.text = data.title
                tvDurationSubCourse.text = data.time
                tvChapterCountSubCourse.text = data.chapters
                tvPriceSubCourse.text = data.fees
            }
        }

        binding.rvSubCourses.adapter = subCoursesAdapter
        binding.rvSubCourses.setHasFixedSize(true)


    }


}