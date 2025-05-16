package com.lesson.codewalledu.src.ui.views.auth.register

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.lesson.codewalledu.R
import com.lesson.codewalledu.databinding.FragmentSignupBinding
import com.lesson.codewalledu.src.data.models.auth.RouteData
import com.lesson.codewalledu.src.utils.APP_COLOR_BLUE
import com.lesson.codewalledu.src.utils.APP_COLOR_CYAN
import com.lesson.codewalledu.src.utils.helpers.applyGradient
import com.lesson.codewalledu.src.utils.helpers.visible
import dagger.hilt.android.AndroidEntryPoint
import java.io.IOException

@AndroidEntryPoint
class SignupFragment : Fragment(R.layout.fragment_signup) {


    lateinit var binding: FragmentSignupBinding
    private val signUpViewModel: SignUpViewModel by viewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentSignupBinding.bind(view)
        binding.titleLoginScreen.applyGradient(APP_COLOR_BLUE, APP_COLOR_CYAN)

        // Initially hide the progress bar
        binding.progressbar.visible(false)

        binding.signInHere.setOnClickListener{
            findNavController().navigate(R.id.action_signupFragment_to_loginFragment)
        }


        signUpViewModel.registerResponse.observe(viewLifecycleOwner) { response ->
            // Hide the progress bar after the response is received
            binding.progressbar.visible(false)
            if (response.isSuccessful) {
                    println("Response: ${response.body()}")
                    val routeData = RouteData("signup","OTP Verification","codewalltech.hr@gmail.com")
                    val action = SignupFragmentDirections.actionSignupToVerification(routeData)
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

        binding.signUpBtn.setOnClickListener {
            // Show the progress bar when the button is clicked
            binding.progressbar.visible(true)
            signUpViewModel.register("CodeWall HR","codewalltech.hr@gmail.com","codewall123")



//            authViewModel.register("Thura Linn","thuralinn45512@gmail.com","22345678")

//            var username = binding.usernameEditText.text.toString()
//            var email = binding.emailEditText.text.toString()
//            var password = binding.passwordEditText.text.toString()

//            if(username.isNotEmpty()){
//                binding.usernameContainer.isErrorEnabled = false
//            }else{
//                binding.usernameContainer.error = "Required"
//                binding.usernameContainer.isErrorEnabled = true
//            }

//            if(email.isNotEmpty()){
//                binding.emailContainer.isErrorEnabled = false
//            }else{
//                binding.emailContainer.error = "Required"
//                binding.emailContainer.isErrorEnabled = true
//            }

//            if(password.isNotEmpty()) {
//                binding.passwordContainer.isErrorEnabled = false
//            }else{
//                binding.passwordContainer.error = "Required"
//                binding.passwordContainer.isErrorEnabled = true
//            }

//            if(username.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()){
//                var user = com.lesson.codewalledu2024.src.models.auth.User(username,email,password)
//
//                authViewModel.register(user)
//
//                authViewModel.myResponse.observe(viewLifecycleOwner){  response->
//                    if(response.isSuccessful){
//                        findNavController().navigate(R.id.action_signupFragment_to_loginFragment2)
//                    }
//                }
//            }




        }




    }

}