package com.lesson.codewalledu.src.ui.views.core.explore.cheat_sheets

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lesson.codewalledu.src.data.models.core.explore.CheatSheetsDataResponse
import com.lesson.codewalledu.src.data.repositories.AuthRepository
import com.lesson.codewalledu.src.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CheatSheetsViewModel@Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {
    private val _cheatSheetsDataResponse: MutableLiveData<Resource<CheatSheetsDataResponse>> = MutableLiveData()
    val cheatSheetsDataResponse: LiveData<Resource<CheatSheetsDataResponse>>
        get() = _cheatSheetsDataResponse

    init {
        viewModelScope.launch {
            getCheatSheets()
        }
    }

    suspend fun getCheatSheets()  {
        val response = repository.getCheatSheets()
        _cheatSheetsDataResponse.value = response
    }
}