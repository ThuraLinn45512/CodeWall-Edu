package com.lesson.codewalledu.src.ui.views.auth.resetpassword

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lesson.codewalledu.src.data.models.auth.AuthResponse
import com.lesson.codewalledu.src.data.repositories.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class ResetPasswordViewModel @Inject constructor(
    private val repository: AuthRepository
): ViewModel() {
    private val _resetPasswordResponse  = MutableLiveData<Response<AuthResponse>>()
    val resetPasswordResponse: LiveData<Response<AuthResponse>>
        get() = _resetPasswordResponse

    fun resetPassword(email: String, password: String,token:String)= viewModelScope.launch {
        val response = repository.resetPassword(email,password,token)
        _resetPasswordResponse.value = response
    }
}