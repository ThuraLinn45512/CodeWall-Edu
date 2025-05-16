package com.lesson.codewalledu.src.ui.views.core.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lesson.codewalledu.R
import com.lesson.codewalledu.src.data.models.core.home.DataResponse
import com.lesson.codewalledu.src.data.models.core.home.Coupons
import com.lesson.codewalledu.src.data.models.core.home.CoursesDataResponse
import com.lesson.codewalledu.src.data.models.core.home.TopicDataResponse
import com.lesson.codewalledu.src.data.repositories.AuthRepository
import com.lesson.codewalledu.src.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {

    private lateinit var couponList: ArrayList<Coupons>

    private val _topicDataResponse: MutableLiveData<Resource<TopicDataResponse>> = MutableLiveData()
    val topicDataResponse: LiveData<Resource<TopicDataResponse>>
        get() = _topicDataResponse

    private val _popularCoursesDataResponse: MutableLiveData<Resource<CoursesDataResponse>> = MutableLiveData()
    val popularCoursesDataResponse: LiveData<Resource<CoursesDataResponse>>
        get() = _popularCoursesDataResponse

    private val _freeCoursesDataResponse: MutableLiveData<Resource<CoursesDataResponse>> = MutableLiveData()
    val freeCoursesDataResponse: LiveData<Resource<CoursesDataResponse>>
        get() = _freeCoursesDataResponse

    private val _upcomingCoursesDataResponse: MutableLiveData<Resource<CoursesDataResponse>> = MutableLiveData()
    val upComingCoursesDataResponse: LiveData<Resource<CoursesDataResponse>>
        get() = _upcomingCoursesDataResponse

    private val _blogDataResponse: MutableLiveData<Resource<DataResponse>> = MutableLiveData()
    val blogDataResponse: LiveData<Resource<DataResponse>>
        get() = _blogDataResponse


    init {
        createCouponsList()

        viewModelScope.launch {





            getTopics()
            getPopularCourses()
            getFreeCourses()
            getBlogs()
            getUpcomingCourses()
        }

    }

    fun getCouponsList(): ArrayList<Coupons> {
        return couponList
    }

    private fun createCouponsList() {
        couponList = ArrayList()
        couponList.add(
            Coupons(
                R.drawable.auto_slide_it,
                R.drawable.baseline_notifications_none_white_24,
                "Started - 09 JUN 2024", "IT Basic Bootcamp Course",
                "Register now to get a discount for the first registered three person"
            )
        )
        couponList.add(
            Coupons(
                R.drawable.auto_slide_web,
                R.drawable.baseline_notifications_none_white_24,
                "Started - 09 JUN 2024", "IT Basic Bootcamp Course",
                "Register now to get a discount for the first registered three person"
            )
        )
        couponList.add(
            Coupons(
                R.drawable.auto_slide_android,
                R.drawable.baseline_notifications_none_white_24,
                "Started - 09 JUN 2024", "IT Basic Bootcamp Course",
                "Register now to get a discount for the first registered three person"
            )
        )
        couponList.add(
            Coupons(
                R.drawable.auto_slide_figma,
                R.drawable.baseline_notifications_none_white_24,
                "Started - 09 JUN 2024", "IT Basic Bootcamp Course",
                "Register now to get a discount for the first registered three person"
            )
        )
    }

    private suspend fun getTopics() {
        val response = repository.getTopics()
        _topicDataResponse.value = response
    }

    private suspend fun getPopularCourses()  {
        val response = repository.getPopularCourses()
        _popularCoursesDataResponse.value = response
    }

    private suspend fun getFreeCourses()   {
        val response = repository.getFreeCourses()
        _freeCoursesDataResponse.value = response
    }

    private suspend fun getBlogs()  {
        val response = repository.getBlogs()
        _blogDataResponse.value = response
    }

    private suspend fun getUpcomingCourses()   {
        val response = repository.getUpcomingCourses()
        _upcomingCoursesDataResponse.value = response
    }


}