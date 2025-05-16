package com.lesson.codewalledu.src.ui.views.auth.forget_password

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.lesson.codewalledu.R
import com.lesson.codewalledu.databinding.FragmentForgetPasswordBinding
import com.lesson.codewalledu.src.data.models.auth.RouteData
import com.lesson.codewalledu.src.utils.APP_COLOR_BLUE
import com.lesson.codewalledu.src.utils.APP_COLOR_CYAN
import com.lesson.codewalledu.src.utils.helpers.applyGradient
import dagger.hilt.android.AndroidEntryPoint
import java.io.IOException

@AndroidEntryPoint
class ForgetPasswordFragment : Fragment(R.layout.fragment_forget_password) {

    lateinit var binding: FragmentForgetPasswordBinding
    val forgetPasswordViewModel: ForgetPasswordViewModel by viewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentForgetPasswordBinding.bind(view)
        binding.titleLoginScreen.applyGradient(APP_COLOR_BLUE, APP_COLOR_CYAN)

        binding.sendCodeBtn.setOnClickListener {
            forgetPasswordViewModel.forgetPassword(email = "codewalltech.hr@gmail.com")
        }

        forgetPasswordViewModel.forgetPasswordResponse.observe(viewLifecycleOwner) { response ->
            if (response.isSuccessful) {
                println("Response: ${response.body()}")
                val routeData = RouteData("forget_password","Reset Password OTP","codewalltech.hr@gmail.com")
                val action = ForgetPasswordFragmentDirections.actionForgetPasswordFragmentToVerificationFragment(routeData)
                findNavController().navigate(action)
            } else {
                response.errorBody()?.let { errorBody ->
                    try {
                        val errorBodyString = errorBody.string()
                        Toast.makeText(activity, errorBodyString, Toast.LENGTH_SHORT).show()
                        // You might want to parse this errorBodyString as JSON if your server sends error details in JSON format
                    } catch (e: IOException) {
                        print("Could not read error body: ${e.message}")
                    }
                }
                print("Response not successful: ${response.code()}, ${response.message()}")
            }
        }

    }
}