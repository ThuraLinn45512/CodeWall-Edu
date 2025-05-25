package com.lesson.codewalledu.src.ui.views.core.home.courses

import androidx.lifecycle.ViewModel
import com.lesson.codewalledu.R
import com.lesson.codewalledu.src.data.models.core.home.home_nested.PopularCourses

class CurrentCoursesViewModel : ViewModel() {

    init {
        createCurrentCoursesList()
    }

    private lateinit var currentCoursesList: ArrayList<PopularCourses>

    fun getCurrentCoursesList(): ArrayList<PopularCourses> {
        return currentCoursesList
    }

    private fun createCurrentCoursesList() {
        currentCoursesList = ArrayList<PopularCourses>()
        currentCoursesList.add(
            PopularCourses(
                image = R.drawable.blogs_figma,
                title = "UI & Figma Master Basic to Advanced Course",
                time = "20 Mins",
                chapters = "Chapters 14",
                fees = "Fees: MMK 250,000/-"
            )
        )
        currentCoursesList.add(
            PopularCourses(
                image = R.drawable.blogs_figma,
                title = "Java Programming Basic to Advanced Course",
                time = "20 Mins",
                chapters = "Chapters 14",
                fees = "Fees: MMK 250,000/-"
            )
        )
        currentCoursesList.add(
            PopularCourses(
                image = R.drawable.blogs_figma,
                title = "Complete Responsive Web Development Course",
                time = "20 Mins",
                chapters = "Chapters 14",
                fees = "Fees: MMK 250,000/-"
            )
        )
    }
}