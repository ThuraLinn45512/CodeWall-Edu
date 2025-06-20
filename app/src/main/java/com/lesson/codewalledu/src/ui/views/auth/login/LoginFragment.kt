package com.lesson.codewalledu.src.ui.views.auth.login


import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.lesson.codewalledu.R
import com.lesson.codewalledu.databinding.FragmentLoginBinding
import com.lesson.codewalledu.src.utils.UserPreferences
import com.lesson.codewalledu.src.utils.helpers.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : Fragment(R.layout.fragment_login) {

    lateinit var binding: FragmentLoginBinding

    private val viewModel: LoginViewModel by viewModels()

    @Inject
    lateinit var userPreferences: UserPreferences

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentLoginBinding.bind(view)

        // Initially hide the progress bar
        binding.progressbar.visible(false)


        /**
         * Sign In Button
         */
        binding.signInBtn.setOnClickListener {
            // Show the progress bar when the button is clicked
            binding.progressbar.visible(true)




//            var email = binding.emailEdt.text.toString()
//            var password = binding.passwordEdt.text.toString()
//
//            if(email.isNotEmpty()){
//                binding.emailContainer.isErrorEnabled = false
//            }else{
//                binding.emailContainer.error = "Required"
//                binding.emailContainer.isErrorEnabled = true
//                }
//            if(password.isNotEmpty()) {
//                binding.passwordContainer.isErrorEnabled = false
//            }else{
//                binding.passwordContainer.error = "Required"
//                binding.passwordContainer.isErrorEnabled = true
//            }
//
//            if(email.isNotEmpty() && password.isNotEmpty()){
//                viewModel.login(email,password)
            viewModel.login("codewalltech.hr@gmail.com","codewall123")
//            }




        }

        viewModel.loginResponse.observe(viewLifecycleOwner) {
                response ->

            // Hide the progress bar after the response is received
            binding.progressbar.visible(false)


            if(response.isSuccessful){
                lifecycleScope.launch {
                    try {
                        userPreferences.setLoggedIn(true)
                        Toast.makeText(activity, "Success", Toast.LENGTH_SHORT).show()
                    } catch (e: Exception) {
                        Toast.makeText(activity, e.toString(), Toast.LENGTH_SHORT).show()
                        print("onViewCreated: $e")
                    }
                    findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
                }
            }else{
                Toast.makeText(activity, "${response.body()?.message}", Toast.LENGTH_SHORT).show()
                response.errorBody()?.let {
                    print(it.toString())
                    print(response.code().toString())
                    print(response.message())
                }
            }

        }


        /**
         * Register Button
         */
        binding.signUpHere.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_signupFragment)
        }

        /**
         * Forget Password Button
         */
        binding.forgetPassword.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_forgetPasswordFragment)
        }
    }

}


