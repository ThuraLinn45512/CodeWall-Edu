package com.lesson.codewalledu.src.ui.views.auth.otp_code

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
class VerificationViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {


    private val _otpVerificationResponse  = MutableLiveData<Response<AuthResponse>>()
    val otpVerificationResponse: LiveData<Response<AuthResponse>>
        get() = _otpVerificationResponse

    fun otpVerification(email: String, otp: String)= viewModelScope.launch {
        val response = repository.otpVerification(email,otp)
        _otpVerificationResponse.value = response
    }


    private val _resendVerificationCodeResponse  = MutableLiveData<Response<AuthResponse>>()
    val resendVerificationCodeResponse: LiveData<Response<AuthResponse>>
        get() = _resendVerificationCodeResponse

    fun resendVerificationCode(email: String)= viewModelScope.launch {
        val response = repository.resendVerificationCode(email)
        _resendVerificationCodeResponse.value = response
    }

    private val _otpForgetPasswordResponse  = MutableLiveData<Response<AuthResponse>>()
    val otpForgetPasswordResponse: LiveData<Response<AuthResponse>>
        get() = _otpForgetPasswordResponse

    fun otpForgetPassword(email: String, otp: String)= viewModelScope.launch {
        val response = repository.otpForgetPassword(email,otp)
        _otpForgetPasswordResponse.value = response
    }


    private val _resendResetCodeResponse  = MutableLiveData<Response<AuthResponse>>()
    val resendResetCodeResponse: LiveData<Response<AuthResponse>>
        get() = _resendVerificationCodeResponse

    fun resendResetCode(email: String)= viewModelScope.launch {
        val response = repository.resendResetCode(email)
        _resendVerificationCodeResponse.value = response
    }


    


}