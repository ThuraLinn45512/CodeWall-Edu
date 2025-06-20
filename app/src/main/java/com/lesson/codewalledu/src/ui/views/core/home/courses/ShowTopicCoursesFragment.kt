package com.lesson.codewalledu.src.ui.views.core.home.courses

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.lesson.codewalledu.R
import com.lesson.codewalledu.databinding.FragmentShowTopicCoursesBinding
import com.lesson.codewalledu.databinding.LayoutCardCoursesBinding
import com.lesson.codewalledu.src.ui.views.core.home.HomeScreenFragmentDirections
import com.lesson.codewalledu.src.utils.APP_COLOR_BLUE
import com.lesson.codewalledu.src.utils.APP_COLOR_CYAN
import com.lesson.codewalledu.src.utils.Resource
import com.lesson.codewalledu.src.utils.adapters.BaseRecyclerViewAdapter
import com.lesson.codewalledu.src.utils.helpers.applyGradient
import com.lesson.codewalledu.src.utils.helpers.handleOnBackPressed
import com.lesson.codewalledu.src.utils.helpers.stringToDouble
import com.lesson.codewalledu.src.utils.helpers.visible
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ShowTopicCoursesFragment: Fragment(R.layout.fragment_show_topic_courses) {
    lateinit var binding: FragmentShowTopicCoursesBinding
    val args : ShowTopicCoursesFragmentArgs by navArgs()
    val viewModel: ShowTopicCoursesViewModel by viewModels()
    private var status = false


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentShowTopicCoursesBinding.bind(view)


        val title = "${args.name} Courses"
        var slug = args.slug

        binding.toolbar.backIcon.visible(true)
        binding.toolbar.toolbarTitle.text = title
        binding.toolbar.toolbarTitle.applyGradient(APP_COLOR_BLUE, APP_COLOR_CYAN)
        binding.toolbar.backIcon.setOnClickListener {
            findNavController().navigate(R.id.action_showTopicCoursesFragment_pop)
        }
        handleOnBackPressed(R.id.action_showTopicCoursesFragment_pop)


        // Set up SwipeRefreshLayout listener
        binding.swipeRefreshLayout.setOnRefreshListener {
            // Trigger data refresh when user pulls down
            binding.swipeRefreshLayout.isRefreshing = false
            lifecycleScope.launch {
                viewModel.getTopicCoursesData(slug)
            }

        }


        viewModel.topicCoursesDataResponse.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    val data = it.value.data
                    status = true
                    updateVisibility()
                    var coursesAdapter = BaseRecyclerViewAdapter(
                        R.layout.layout_card_courses, false,
                        data
                    ) { _, data, view ->
                        val itemBinding = LayoutCardCoursesBinding.bind(view)
                        itemBinding.apply {
                            Picasso.get()
                                .load(data.imageUrl)
                                .placeholder(R.drawable.rectangle_placeholder)
                                .error(R.drawable.rectangle_placeholder)
                                .into(ivCourse)

                            tvTitle.text = data.courseTitle

                            if(data.isPublished==1){
                                var courseFees  = data.courseFees.stringToDouble()
                                when (courseFees) {
                                    in 100000.00..1000000.00 -> {
                                        detailLayout.visible(true)
                                        cvTab.visible(true)
                                        tvDuration.text = "${data.length} hours"
                                        tvChapterCount.text = "${data.chapterCount} Chapters"
                                        tvPrice.text = "Fees: ${data.courseFees}/MMK"
                                    }
                                    0.0 -> {
                                        detailLayout.visible(true)
                                        tvDuration.text = "${data.length} hours"
                                        tvChapterCount.text = "${data.chapterCount} Lessons"
                                    }
                                }
                            }

                            cardRoot.setOnClickListener {
                                val action = ShowTopicCoursesFragmentDirections.actionShowTopicCoursesFragmentToShowCourseDetailsFragment(data.id)
                                findNavController().navigate(action)
                            }


                        }
                    }
                    binding.rvCourses.adapter = coursesAdapter

                }

                is Resource.Failure -> {
                    Toast.makeText(requireContext(), it.errorBody.toString(), Toast.LENGTH_SHORT)
                        .show()
                }

                is Resource.Loading -> {
                    status = false
                    updateVisibility()
                }
            }
        }


    }

    private fun updateVisibility() {
        val allStatus = status
        binding.rvCourses.visibility = if (allStatus) View.VISIBLE else View.GONE
        binding.shimmerLayout.visibility = if (allStatus) View.GONE else View.VISIBLE
        if (allStatus) {
            binding.shimmerLayout.stopShimmer()
        } else {
            binding.shimmerLayout.startShimmer()
        }
    }
}