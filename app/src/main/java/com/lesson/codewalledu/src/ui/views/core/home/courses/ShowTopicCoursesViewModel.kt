package com.lesson.codewalledu.src.ui.views.core.home.courses

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lesson.codewalledu.src.data.models.core.home.CoursesDataResponse
import com.lesson.codewalledu.src.data.repositories.AuthRepository
import com.lesson.codewalledu.src.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ShowTopicCoursesViewModel@Inject constructor(
    private val repository: AuthRepository,
    private val savedStateHandle: SavedStateHandle // Inject SavedStateHandle
): ViewModel() {

    private val _topicCoursesDataResponse: MutableLiveData<Resource<CoursesDataResponse>> = MutableLiveData()
    val topicCoursesDataResponse: LiveData<Resource<CoursesDataResponse>>
        get() = _topicCoursesDataResponse


    init {
        // Retrieve the 'index' (slug) from SavedStateHandle
        // Make sure the key "index" matches the argument name in your Safe Args setup.
        val slug: String = savedStateHandle.get<String>("slug") ?: "programming" // Provide a default or handle null
        if (slug.isNotEmpty()) { // Only fetch if a valid courseId is available
            viewModelScope.launch {
                getTopicCoursesData(slug)
            }
        } else {
            // Handle case where courseId is not found (e.g., set an error state)
            _topicCoursesDataResponse.value = Resource.Failure(false, null, null)
        }
    }

    suspend fun getTopicCoursesData(courseSlug:String)  {
        val response = repository.getTopicCoursesData(courseSlug)
        _topicCoursesDataResponse.value = response
    }




}