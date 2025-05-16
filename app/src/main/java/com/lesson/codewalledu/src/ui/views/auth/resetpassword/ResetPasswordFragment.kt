package com.lesson.codewalledu.src.ui.views.auth.resetpassword

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.lesson.codewalledu.R
import com.lesson.codewalledu.databinding.FragmentResetPasswordBinding
import com.lesson.codewalledu.src.utils.APP_COLOR_BLUE
import com.lesson.codewalledu.src.utils.APP_COLOR_CYAN
import com.lesson.codewalledu.src.utils.helpers.applyGradient
import dagger.hilt.android.AndroidEntryPoint
import java.io.IOException

@AndroidEntryPoint
class ResetPasswordFragment : Fragment(R.layout.fragment_reset_password) {


    lateinit var binding: FragmentResetPasswordBinding
    private val resetPasswordViewModel: ResetPasswordViewModel by viewModels()
    private val args: ResetPasswordFragmentArgs by navArgs()
    private lateinit var email:String
    private lateinit var token:String



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentResetPasswordBinding.bind(view)
        binding.titleLoginScreen.applyGradient(APP_COLOR_BLUE, APP_COLOR_CYAN)

        email = args.routeDataTwo.email.toString()
        token = args.routeDataTwo.token.toString()





        binding.resetPasswordBtn.setOnClickListener {
            var newPassword = binding.newPasswordEditText.text.toString()
            var confirmPassword = binding.confirmPasswordEditText.text.toString()

            if(newPassword.isNotEmpty()){
                binding.newPasswordContainer.isErrorEnabled = false
            }else{
                binding.newPasswordContainer.error = "Required"
                binding.newPasswordContainer.isErrorEnabled = true
                }
            if(confirmPassword.isNotEmpty()) {
                binding.confirmPasswordContainer.isErrorEnabled = false
            }else{
                binding.confirmPasswordContainer.error = "Required"
                binding.confirmPasswordContainer.isErrorEnabled = true
            }

            if(newPassword.isNotEmpty() && confirmPassword.isNotEmpty() && newPassword == confirmPassword){
                resetPasswordViewModel.resetPassword(email,confirmPassword,token)
            }
        }


        resetPasswordViewModel.resetPasswordResponse.observe(viewLifecycleOwner){
            response->
            if (response.isSuccessful) {
                findNavController().navigate(R.id.action_resetPasswordFragment_to_homeFragment)
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