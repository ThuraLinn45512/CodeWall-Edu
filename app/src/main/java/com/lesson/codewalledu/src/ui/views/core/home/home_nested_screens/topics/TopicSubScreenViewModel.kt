package com.lesson.codewalledu.src.ui.views.core.home.home_nested_screens.topics

import androidx.lifecycle.ViewModel
import com.lesson.codewalledu.R
import com.lesson.codewalledu.src.data.models.core.home.home_nested.FullCourses
import com.lesson.codewalledu.src.data.models.core.home.home_nested.SubCourses

class TopicSubScreenViewModel : ViewModel() {

    init {
        createCouponsList()
        createSubCoursesList()
    }

    private lateinit var couponList: ArrayList<com.lesson.codewalledu.src.data.models.core.home.Coupons>
    private lateinit var fullCourseList: ArrayList<FullCourses>
    private lateinit var subCoursesList: ArrayList<SubCourses>

    fun getCouponsList(): ArrayList<com.lesson.codewalledu.src.data.models.core.home.Coupons> {
        return couponList
    }



    fun getSubCoursesList(): ArrayList<SubCourses> {
        return subCoursesList
    }


    private fun createCouponsList() {
        couponList = ArrayList<com.lesson.codewalledu.src.data.models.core.home.Coupons>()
        couponList.add(
            com.lesson.codewalledu.src.data.models.core.home.Coupons(
                R.drawable.auto_slide_it,
                R.drawable.baseline_notifications_none_white_24,
                "Started - 09 JUN 2024", "IT Basic Bootcamp Course",
                "Register now to get a discount for the first registered three person"
            )
        )
        couponList.add(
            com.lesson.codewalledu.src.data.models.core.home.Coupons(
                R.drawable.auto_slide_web,
                R.drawable.baseline_notifications_none_white_24,
                "Started - 09 JUN 2024", "IT Basic Bootcamp Course",
                "Register now to get a discount for the first registered three person"
            )
        )
        couponList.add(
            com.lesson.codewalledu.src.data.models.core.home.Coupons(
                R.drawable.auto_slide_android,
                R.drawable.baseline_notifications_none_white_24,
                "Started - 09 JUN 2024", "IT Basic Bootcamp Course",
                "Register now to get a discount for the first registered three person"
            )
        )
        couponList.add(
            com.lesson.codewalledu.src.data.models.core.home.Coupons(
                R.drawable.auto_slide_figma,
                R.drawable.baseline_notifications_none_white_24,
                "Started - 09 JUN 2024", "IT Basic Bootcamp Course",
                "Register now to get a discount for the first registered three person"
            )
        )

    }



    private fun createSubCoursesList(){
        subCoursesList = ArrayList<SubCourses>()
        subCoursesList.add(
            SubCourses(
                image = R.drawable.courses_figma,
                title = "Figma Design Course - Module 1 & 2",
                time = "20 Mins",
                chapters = "Chapters 14",
                fees = "Fees: MMK 250,000/-"
            )
        )
        subCoursesList.add(
            SubCourses(
                image = R.drawable.courses_figma,
                title = "Figma Design Course - Module 1",
                time = "20 Mins",
                chapters = "Chapters 14",
                fees = "Fees: MMK 250,000/-"
            )
        )
        subCoursesList.add(
            SubCourses(
                image = R.drawable.courses_figma,
                title = "Figma Design Course - Module 2",
                time = "20 Mins",
                chapters = "Chapters 14",
                fees = "Fees: MMK 250,000/-"
            )
        )
        subCoursesList.add(
            SubCourses(
                image = R.drawable.courses_figma,
                title = "Figma Design Course - Module 3",
                time = "20 Mins",
                chapters = "Chapters 14",
                fees = "Fees: MMK 250,000/-"
            )
        )

    }
}