package com.lesson.codewalledu.src.ui.views.core.home.courses

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.lesson.codewalledu.R
import com.lesson.codewalledu.databinding.FragmentShowCourseDetailsBinding
import com.lesson.codewalledu.databinding.LayoutCardChapterBinding
import com.lesson.codewalledu.databinding.LayoutCardCurriculumBinding
import com.lesson.codewalledu.databinding.LayoutCardSectionBinding
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

@Suppress("DEPRECATION")
@AndroidEntryPoint
class ShowCourseDetailsFragment : Fragment(R.layout.fragment_show_course_details) {
    lateinit var binding: FragmentShowCourseDetailsBinding
    val args: ShowCourseDetailsFragmentArgs by navArgs()
    val viewModel: ShowCoursesDetailsViewModel by viewModels()
    private var status = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentShowCourseDetailsBinding.bind(view)

        binding.toolbar.backIcon.visible(true)
        binding.toolbar.toolbarTitle.applyGradient(APP_COLOR_BLUE, APP_COLOR_CYAN)
        binding.toolbar.toolbarTitle.text = "EnrollCourse Details"
        binding.toolbar.backIcon.setOnClickListener {
            findNavController().navigate(R.id.action_showCourseDetailsFragment_pop)
        }


        val index = args.index

        handleOnBackPressed(R.id.action_showCourseDetailsFragment_pop)


        viewModel.eachCourseDataResponse.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    val data = it.value.data
                    status = true
                    updateVisibility()


                    binding.apply {

                        info.tvTitle.visible(true)
                        info.tvTitle.text = data.course.courseTitle

                        tvCurriculum.visible(true)

                        Picasso
                            .get()
                            .load(data.course.imageUrl)
                            .error(R.drawable.ic_launcher_background)
                            .into(ivCourse)


                        if(data.course.isPublished==1){
                            var courseFees = data.course.courseFees.stringToDouble()
                            when (courseFees) {
                                in 100000.00..1000000.00 -> {
                                    info.detailLayout.visible(true)
                                    info.cvTab.visible(true)
                                    info.tvDuration.text = "${data.course.length} hours"
                                    info.tvChapterCount.text = "${data.course.chapterCount} Chapters"
                                    info.tvPrice.text = "Fees: MMK ${data.course.courseFees}"
                                }
                                0.0 -> {
                                    info.detailLayout.visible(true)
                                    info.tvDuration.text = "${data.course.length} hours"
                                    info.tvChapterCount.text = "${data.course.chapterCount} Lessons"
                                }

                            }

                            binding.btnEnroll.visible(true)
                            binding.btnEnroll.setOnClickListener {
                                val action = ShowCourseDetailsFragmentDirections.actionShowCourseDetailsFragmentToEnrollInCourseFragment(data.course.id)
                              findNavController().navigate(action)
                            }

                        }



                    }


                    /**
                     * Header Sections
                     */


                    var headerSectionAdapter = BaseRecyclerViewAdapter(
                        R.layout.layout_card_section, false,
                        data.course.headerSections
                    ) { _, data, view ->
                        val itemBinding = LayoutCardSectionBinding.bind(view)
                        itemBinding.apply {
                            tvSectionName.text = data.sectionName
                            tvSectionContent.text = data.content
                        }
                    }

                    binding.rvHeaderSections.adapter = headerSectionAdapter
                    binding.rvHeaderSections.setHasFixedSize(true)


                    /**
                     * Curriculum
                     */

                    if(data.course.curriculums.isEmpty()){
                        binding.tvCurriculum.visible(false)
                    }

                    var curriculumAdapter = BaseRecyclerViewAdapter(
                        R.layout.layout_card_curriculum, false,
                        data.course.curriculums
                    ) { _, curriculumData, view ->
                        val itemBinding = LayoutCardCurriculumBinding.bind(view)
                        itemBinding.apply {

                            tvModuleName.text = curriculumData.title
                            if(data.course.curriculums.size < 2){
                                tvModuleName.visible(false)
                            }


                            var chapterAdapter = BaseRecyclerViewAdapter(
                                R.layout.layout_card_chapter, false,
                                curriculumData.chapters
                            ){
                                _, chapterData, view ->
                                val itemBinding = LayoutCardChapterBinding.bind(view)
                                itemBinding.apply {
                                    tvChapterName.text = chapterData.title
                                    tvChapterName.textSize = 12.0f
                                    tvChapterName.setTextColor(resources.getColor(R.color.text_color_2))
                                }
                            }

                            rvChapters.adapter = chapterAdapter
                            rvChapters.setHasFixedSize(true)

                        }
                    }

                    binding.rvCurriculums.adapter = curriculumAdapter
                    binding.rvCurriculums.setHasFixedSize(true)


                    /**
                     * Other Sections
                     */

                    var otherSectionAdapter = BaseRecyclerViewAdapter(
                        R.layout.layout_card_section, false,
                        data.course.otherSections
                    ) { _, data, view ->
                        val itemBinding = LayoutCardSectionBinding.bind(view)
                        itemBinding.apply {
                            tvSectionName.text = data.sectionName
                            tvSectionContent.text = data.content
                        }
                    }

                    binding.rvOtherSections.adapter = otherSectionAdapter
                    binding.rvOtherSections.setHasFixedSize(true)





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
        binding.nestedScrollView.visibility = if (allStatus) View.VISIBLE else View.GONE
        binding.shimmerLayout.visibility = if (allStatus) View.GONE else View.VISIBLE
        if (allStatus) {
            binding.shimmerLayout.stopShimmer()
        } else {
            binding.shimmerLayout.startShimmer()
        }
    }
}