package com.lesson.codewalledu.src.ui.views.core.home.home_nested_screens.daily_dev

import androidx.lifecycle.ViewModel

class BlogsViewModel : ViewModel() {
    init {
//        createBlogs()
    }

    private lateinit var blogsDataList: ArrayList<com.lesson.codewalledu.src.data.models.core.home.BlogsData>

    fun getBlogsList():ArrayList<com.lesson.codewalledu.src.data.models.core.home.BlogsData>{
        return blogsDataList
    }

//    private fun createBlogs(){
//        blogsDataList = ArrayList<com.lesson.codewalledu2024.src.data.models.core.home.BlogsData>()
//        blogsDataList.add(
//            com.lesson.codewalledu2024.src.data.models.core.home.BlogsData(
//                imageUrl = R.drawable.blogs_figma,
//                contentTitle = "7 Figma plugin should know\n as an UI/UX Designer",
//                createdAt = "Jan 1, 2025",
//                readingTime = "15 Mins",
//                contentBody = "nt to creating animations and enhancing accessibility."
//            )
//        )
//        blogsDataList.add(
//            com.lesson.codewalledu2024.src.data.models.core.home.BlogsData(
//                imageUrl = R.drawable.blogs_figma,
//                contentTitle = "7 Figma plugin should know\n as an UI/UX Designer",
//                createdAt = "Jan 1, 2025",
//                readingTime = "15 Mins",
//                contentBody = "nt to creating animations and enhancing accessibility."
//            )
//        )
//    }
}