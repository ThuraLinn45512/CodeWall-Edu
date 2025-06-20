package com.lesson.codewalledu.src.ui.views.core.home.courses

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lesson.codewalledu.src.data.models.core.home.enroll.EnrollDataResponse
import com.lesson.codewalledu.src.data.repositories.AuthRepository
import com.lesson.codewalledu.src.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class EnrollInCourseViewModel@Inject constructor(
    private val repository: AuthRepository,
    private val savedStateHandle: SavedStateHandle // Inject SavedStateHandle
) : ViewModel() {
    private val _dataResponse: MutableLiveData<Resource<EnrollDataResponse>> = MutableLiveData()
    val enrolledDataResponse: LiveData<Resource<EnrollDataResponse>>
        get() = _dataResponse

    init {
        // Retrieve the 'index' (courseId) from SavedStateHandle
        // Make sure the key "courseId" matches the argument name in your Safe Args setup.
        val courseId: Int = savedStateHandle.get<Int>("courseId") ?: 0 // Provide a default or handle null

        // --- ADD THIS LOG STATEMENT ---
        println("ViewModel init - received courseId: $courseId")
        // -----------------------------


        if (courseId != 0) { // Only fetch if a valid courseId is available
            viewModelScope.launch {
                getEnrollData(courseId)
            }
        } else {
            // Handle case where courseId is not found (e.g., set an error state)
            _dataResponse.value = Resource.Failure(false, null, null)
        }
    }

    suspend fun getEnrollData(courseId: Int) {
        val response = repository.getEnrollData(courseId)
        _dataResponse.value = response
    }
}
