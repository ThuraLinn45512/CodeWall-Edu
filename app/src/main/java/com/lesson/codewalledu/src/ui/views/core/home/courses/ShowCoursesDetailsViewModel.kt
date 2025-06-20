package com.lesson.codewalledu.src.ui.views.core.home.courses

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lesson.codewalledu.src.data.models.core.home.EachCourseDataResponse
import com.lesson.codewalledu.src.data.repositories.AuthRepository
import com.lesson.codewalledu.src.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ShowCoursesDetailsViewModel @Inject constructor(
    private val repository: AuthRepository,
    private val savedStateHandle: SavedStateHandle // Inject SavedStateHandle
): ViewModel(){

    private val _eachCourseDataResponse: MutableLiveData<Resource<EachCourseDataResponse>> = MutableLiveData()
    val eachCourseDataResponse: LiveData<Resource<EachCourseDataResponse>>
        get() = _eachCourseDataResponse


    init {
        // Retrieve the 'index' (courseId) from SavedStateHandle
        // Make sure the key "index" matches the argument name in your Safe Args setup.
        val courseId: Int = savedStateHandle.get<Int>("index") ?: 0 // Provide a default or handle null

        // --- ADD THIS LOG STATEMENT ---
        println("ViewModel init - received courseId: $courseId")
        // -----------------------------


        if (courseId != 0) { // Only fetch if a valid courseId is available
            viewModelScope.launch {
                getEachCourseData(courseId)
            }
        } else {
            // Handle case where courseId is not found (e.g., set an error state)
            _eachCourseDataResponse.value = Resource.Failure(false, null, null)
        }
    }


    suspend fun getEachCourseData(courseId:Int)  {
        val response = repository.getEachCourseData(courseId)
        _eachCourseDataResponse.value = response
    }
}