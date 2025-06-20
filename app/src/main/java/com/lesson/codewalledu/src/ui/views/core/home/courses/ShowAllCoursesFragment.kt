package com.lesson.codewalledu.src.ui.views.core.home.courses

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.lesson.codewalledu.R
import com.lesson.codewalledu.databinding.FragmentShowAllCoursesBinding
import com.lesson.codewalledu.databinding.LayoutCardCoursesBinding
import com.lesson.codewalledu.src.utils.APP_COLOR_BLUE
import com.lesson.codewalledu.src.utils.APP_COLOR_CYAN
import com.lesson.codewalledu.src.utils.adapters.BaseRecyclerViewAdapter
import com.lesson.codewalledu.src.utils.helpers.applyGradient
import com.lesson.codewalledu.src.utils.helpers.handleOnBackPressed
import com.lesson.codewalledu.src.utils.helpers.stringToDouble
import com.lesson.codewalledu.src.utils.helpers.visible
import com.squareup.picasso.Picasso

class ShowAllCoursesFragment : Fragment(R.layout.fragment_show_all_courses) {

    lateinit var binding: FragmentShowAllCoursesBinding
    val args: ShowAllCoursesFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentShowAllCoursesBinding.bind(view)



        val title = args.titleFrom
        val list = args.courses.toList()

        binding.toolbar.backIcon.visible(true)
        binding.toolbar.toolbarTitle.text = title
        binding.toolbar.toolbarTitle.applyGradient(APP_COLOR_BLUE, APP_COLOR_CYAN)
        binding.toolbar.backIcon.setOnClickListener {
            findNavController().navigate(R.id.action_showAllCoursesFragment_pop)
        }
        handleOnBackPressed(R.id.action_showAllCoursesFragment_pop)




        var coursesAdapter = BaseRecyclerViewAdapter(
            R.layout.layout_card_courses, false,
            list
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
                    var courseFeesInDouble = data.courseFees.stringToDouble()


                    when (courseFeesInDouble) {
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
                    val action = ShowAllCoursesFragmentDirections.actionShowAllCoursesFragmentToShowCourseDetailsFragment(data.id)
                    findNavController().navigate(action)
                }



            }
        }
        binding.rvCourses.adapter = coursesAdapter





    }




}