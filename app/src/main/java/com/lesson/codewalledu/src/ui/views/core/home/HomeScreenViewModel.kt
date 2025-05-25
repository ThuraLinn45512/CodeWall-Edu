package com.lesson.codewalledu.src.ui.views.core.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lesson.codewalledu.R
import com.lesson.codewalledu.src.data.models.core.home.Coupons
import com.lesson.codewalledu.src.data.models.core.home.CoursesDataResponse
import com.lesson.codewalledu.src.data.models.core.home.TopicDataResponse
import com.lesson.codewalledu.src.data.repositories.AuthRepository
import com.lesson.codewalledu.src.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {

    private lateinit var couponList: ArrayList<Coupons>


    init {
        createCouponsList()


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




}