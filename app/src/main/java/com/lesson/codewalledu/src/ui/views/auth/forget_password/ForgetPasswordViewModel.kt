package com.lesson.codewalledu.src.ui.views.auth.forget_password

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
class ForgetPasswordViewModel @Inject constructor(
    private val repository: AuthRepository
): ViewModel() {
    private val _forgetPasswordResponse  = MutableLiveData<Response<AuthResponse>>()
    val forgetPasswordResponse: LiveData<Response<AuthResponse>>
        get() = _forgetPasswordResponse

    fun forgetPassword(email: String)= viewModelScope.launch {
        val response = repository.forgetPassword(email)
        _forgetPasswordResponse.value = response
    }
}