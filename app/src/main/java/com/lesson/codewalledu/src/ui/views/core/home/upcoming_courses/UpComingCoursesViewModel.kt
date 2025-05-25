package com.lesson.codewalledu.src.ui.views.core.home.upcoming_courses

import androidx.lifecycle.ViewModel
import com.lesson.codewalledu.R
import com.lesson.codewalledu.src.data.models.core.home.home_nested.UpcomingCourses

class UpComingCoursesViewModel : ViewModel() {
    init {
        createUpComingCoursesList()
    }

    private lateinit var upComingCoursesList: ArrayList<UpcomingCourses>

    fun getUpComingCourses(): ArrayList<UpcomingCourses> {
        return upComingCoursesList
    }

    private fun createUpComingCoursesList() {
        upComingCoursesList = ArrayList<UpcomingCourses>()
        upComingCoursesList.add(
            UpcomingCourses(
                image = R.drawable.blogs_figma,
                date = "Started - 09 JUN 2024",
                title = "Drat Programming & Flutter\n Framework Course",
                fees = "Fees: MMK 250,000/-"
            )
        )
        upComingCoursesList.add(
            UpcomingCourses(
                image = R.drawable.blogs_figma,
                date = "Started - 09 JUN 2024",
                title = "Kotlin Programming & Android Application Development Course",
                fees = "Fees: MMK 490,000/-"
            )
        )
    }
}