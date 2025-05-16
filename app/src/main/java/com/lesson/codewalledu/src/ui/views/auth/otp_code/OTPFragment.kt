package com.lesson.codewalledu.src.ui.views.auth.otp_code

import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.lesson.codewalledu.R
import com.lesson.codewalledu.databinding.FragmentOtpCodeBinding
import com.lesson.codewalledu.src.data.models.auth.RouteData2
import com.lesson.codewalledu.src.utils.APP_COLOR_BLUE
import com.lesson.codewalledu.src.utils.APP_COLOR_CYAN
import com.lesson.codewalledu.src.utils.helpers.applyGradient
import com.lesson.codewalledu.src.utils.helpers.visible
import dagger.hilt.android.AndroidEntryPoint
import java.io.IOException
import kotlin.getValue

@AndroidEntryPoint
class OTPFragment : Fragment(R.layout.fragment_otp_code) {

    lateinit var binding: FragmentOtpCodeBinding
    private val verificationViewModel: VerificationViewModel by viewModels()
    private val args: OTPFragmentArgs by navArgs()

    private var timer: CountDownTimer? = null
    private val totalTime = 300000L // 5 minutes in milliseconds


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentOtpCodeBinding.bind(view)



        // Initially hide the progress bar
        binding.progressbar.visible(false)

        val source = args.routeData.routeName
        val title = args.routeData.title
        val email = args.routeData.email  // Retrieve the email

        binding.titleOtpCodeScreen.applyGradient(APP_COLOR_BLUE, APP_COLOR_CYAN)
        binding.titleOtpCodeScreen.text = title


        startCountdown()


        // Set up listeners for OTP input fields
        setupOtpEditTextListeners()

        binding.verifyBtn.setOnClickListener {
            // Show the progress bar when the button is clicked
            binding.progressbar.visible(true)


            val otp = getOtpCode()
            if (otp.length == 6) {
                if(source=="signup"){
                    verificationViewModel.otpVerification(email = email,otp)
                }else if(source=="forget_password"){
                    verificationViewModel.otpForgetPassword(email= email,otp)
                }

            } else {
                Toast.makeText(activity, "Please enter the complete OTP", Toast.LENGTH_SHORT).show()
            }
        }

        verificationViewModel.otpVerificationResponse.observe(viewLifecycleOwner) { response ->
            // Hide the progress bar after the response is received
            binding.progressbar.visible(false)
            if (response.isSuccessful) {
                    findNavController().navigate(R.id.action_verificationFragment_to_homeFragment)
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

        verificationViewModel.otpForgetPasswordResponse.observe(viewLifecycleOwner) { response->
            // Hide the progress bar after the response is received
            binding.progressbar.visible(false)
            if (response.isSuccessful) {
                    val routeData2 = RouteData2(email,response.body()?.data?.token.toString())
                    val action = OTPFragmentDirections.actionVerificationFragmentToResetPasswordFragment(routeData2)
                    findNavController().navigate(action)
                Toast.makeText(activity, "success", Toast.LENGTH_SHORT).show()
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


        binding.resendOtpBtn.setOnClickListener {
            if(source=="signup"){
                verificationViewModel.resendVerificationCode(email = "codewalltech.hr@gmail.com")
            }else if(source=="forget_password"){
                verificationViewModel.resendResetCode(email= "codewalltech.hr@gmail.com")
            }

            restartCountdown() // Restart the countdown
        }

        verificationViewModel.resendVerificationCodeResponse.observe(viewLifecycleOwner,::handleResendOtpResponse)

//        { response ->
//            if (response.isSuccessful) {
//                println("Response: ${response.body()}")
//                Toast.makeText(activity, "Check your email again:", Toast.LENGTH_SHORT).show()
//            } else {
//                response.errorBody()?.let { errorBody ->
//                    try {
//                        val errorBodyString = errorBody.string()
//                        Toast.makeText(activity, errorBodyString, Toast.LENGTH_SHORT).show()
//                        // You might want to parse this errorBodyString as JSON if your server sends error details in JSON format
//                    } catch (e: IOException) {
//                        print("Could not read error body: ${e.message}")
//                    }
//                }
//                print("Response not successful: ${response.code()}, ${response.message()}")
//            }
//        }

        verificationViewModel.resendResetCodeResponse.observe(viewLifecycleOwner,::handleResendOtpResponse)



    }

    private fun handleResendOtpResponse(response: retrofit2.Response<*>) {
        if (response.isSuccessful) {
            println("Response: ${response.body()}")
            Toast.makeText(activity, "Check your email again:", Toast.LENGTH_SHORT).show()
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

    private fun startCountdown() {
        timer = object : CountDownTimer(totalTime, 1000) { // Millis in future, countdown interval
            override fun onTick(millisUntilFinished: Long) {
                val minutes = (millisUntilFinished / 1000) / 60
                val seconds = (millisUntilFinished / 1000) % 60
                val timeFormatted = String.format("%02d:%02d s", minutes, seconds)
                binding.countDownTv.text = timeFormatted
            }

            override fun onFinish() {
                binding.countDownTv.text = "00:00 s"
                // Handle timer finish (e.g., resend code button enable)
            }
        }.start()
    }

    private fun restartCountdown() {
        timer?.cancel() // Cancel the existing timer
        startCountdown()  // Start a new timer
    }

    override fun onDestroyView() {
        super.onDestroyView()
        timer?.cancel() // Cancel the timer to prevent leaks
    }

    private fun setupOtpEditTextListeners() {
        binding.editTextOtp1.addTextChangedListener(OtpTextWatcher(binding.editTextOtp1, binding.editTextOtp2))
        binding.editTextOtp2.addTextChangedListener(OtpTextWatcher(binding.editTextOtp2, binding.editTextOtp3))
        binding.editTextOtp3.addTextChangedListener(OtpTextWatcher(binding.editTextOtp3, binding.editTextOtp4))
        binding.editTextOtp4.addTextChangedListener(OtpTextWatcher(binding.editTextOtp4, binding.editTextOtp5))
        binding.editTextOtp5.addTextChangedListener(OtpTextWatcher(binding.editTextOtp5, binding.editTextOtp6))
        binding.editTextOtp6.addTextChangedListener(OtpTextWatcher(binding.editTextOtp6, null)) // Last field
    }

    private fun getOtpCode(): String {
        return binding.editTextOtp1.text.toString() +
                binding.editTextOtp2.text.toString() +
                binding.editTextOtp3.text.toString() +
                binding.editTextOtp4.text.toString() +
                binding.editTextOtp5.text.toString() +  // Add new EditText values
                binding.editTextOtp6.text.toString()   // Add new EditText values
    }

}