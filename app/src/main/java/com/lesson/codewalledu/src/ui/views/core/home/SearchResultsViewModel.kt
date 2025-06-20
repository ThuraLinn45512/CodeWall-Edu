package com.lesson.codewalledu.src.ui.views.core.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lesson.codewalledu.src.data.models.core.UserDataResponse
import com.lesson.codewalledu.src.data.models.core.home.search.SearchDataResponse
import com.lesson.codewalledu.src.data.repositories.AuthRepository
import com.lesson.codewalledu.src.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class SearchResultsViewModel@Inject constructor(
    private val repository: AuthRepository,
    private val savedStateHandle: SavedStateHandle // Inject SavedStateHandle
): ViewModel() {
    private val _searchDataResponse: MutableLiveData<Resource<SearchDataResponse>> = MutableLiveData()
    val searchDataResponse: LiveData<Resource<SearchDataResponse>>
        get() = _searchDataResponse


    init {
        // Retrieve the 'index' (slug) from SavedStateHandle
        // Make sure the key "index" matches the argument name in your Safe Args setup.
        val key: String = savedStateHandle.get<String>("searchQuery") ?: "java" // Provide a default or handle null
        if (key.isNotEmpty()) { // Only fetch if a valid courseId is available
            viewModelScope.launch {
                getSearchData(key)
            }
        } else {
            // Handle case where courseId is not found (e.g., set an error state)
            _searchDataResponse.value = Resource.Failure(false, null, null)
        }
    }

    suspend fun getSearchData(key:String)  {
        val response = repository.getSearchData(key)
        _searchDataResponse.value = response
    }
}