package com.lesson.codewalledu.src.ui.views.core.home.courses

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.lesson.codewalledu.R
import com.lesson.codewalledu.databinding.FragmentEnrollInCourseBinding
import com.lesson.codewalledu.databinding.LayoutCardNormalPromotionBinding
import com.lesson.codewalledu.databinding.LayoutCardNormalPromotionInSummaryBinding
import com.lesson.codewalledu.src.data.models.core.home.enroll.EnrollData
import com.lesson.codewalledu.src.utils.APP_COLOR_BLUE
import com.lesson.codewalledu.src.utils.APP_COLOR_CYAN
import com.lesson.codewalledu.src.utils.Resource
import com.lesson.codewalledu.src.utils.UserPreferences
import com.lesson.codewalledu.src.utils.adapters.BaseRecyclerViewAdapter
import com.lesson.codewalledu.src.utils.helpers.applyGradient
import com.lesson.codewalledu.src.utils.helpers.handleOnBackPressed
import com.lesson.codewalledu.src.utils.helpers.visible
import dagger.hilt.android.AndroidEntryPoint
import jakarta.inject.Inject
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@AndroidEntryPoint
class EnrollInCourseFragment : Fragment(R.layout.fragment_enroll_in_course) {

    private val viewModel: EnrollInCourseViewModel by viewModels()
    lateinit var binding: FragmentEnrollInCourseBinding
    val args: EnrollInCourseFragmentArgs by navArgs()

    @Inject
    lateinit var userPreferences: UserPreferences

    var isPremiumMember = false


    private var currentPaymentMethod: String = "Balance" // Default selection
    private var enrollData: EnrollData? = null // Store the loaded data


    var normalPromotionsInPriceSummaryAdapter: BaseRecyclerViewAdapter<*>? = null

    @SuppressLint("ResourceAsColor", "SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentEnrollInCourseBinding.bind(view)

        binding.toolbar.backIcon.visible(true)
        binding.toolbar.toolbarTitle.applyGradient(APP_COLOR_BLUE, APP_COLOR_CYAN)
        binding.toolbar.toolbarTitle.text = "Enroll in EnrollCourse"
        binding.toolbar.backIcon.setOnClickListener {
            findNavController().navigate(R.id.action_enrollInCourseFragment_pop)
        }
        handleOnBackPressed(R.id.action_enrollInCourseFragment_pop)

        val courseId = args.courseId



        // --- Observe Premium Member Status from UserPreferences (DataStore) ---
        viewLifecycleOwner.lifecycleScope.launch {
            userPreferences.isUserPremiumFlow.collectLatest { isPremium ->
                    isPremiumMember = isPremium
            }
        }
        // --- End Observe Premium Member Status ---


        // Set click listeners for payment methods
        binding.layoutPayWithBalance.setOnClickListener {
            updatePaymentMethodSelection("Balance")
        }

        binding.layoutPayWithPoint.setOnClickListener {
            updatePaymentMethodSelection("Credit")
        }

        // Observe the API response
        viewModel.enrolledDataResponse.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Success -> {
                    val data = resource.value.data
                    enrollData = data // Store the received data here


                    /**
                     * Course Data
                     */
                    binding.tvEnrollCourseLength.text = "${data.course.length} Hours"
                    binding.tvCountChapter.text = "${data.course.chapterCount} Chapters"


                    /**
                     * User Data
                     */
                    binding.tvUserCurrentPoints.text = "${data.enrollmentInfo.enrollmentMethod.point.available} Pts"
                    binding.tvUserCurrentBalance.text = "${data.enrollmentInfo.enrollmentMethod.balance.available} MMK"


                    /**
                     * Available Discounts
                     */

                    var normalPromotionCount = data.enrollmentInfo.promotions.normalPromotions.size
                    if(isPremiumMember && normalPromotionCount!=0){
                        binding.tvAvailableDiscounts.visible(true)
                    }

                    if(normalPromotionCount!=0){
                        binding.rvNormalPromotions.visible(true)
                        val normalPromotionsAdapter = BaseRecyclerViewAdapter(
                            R.layout.layout_card_normal_promotion, false,
                            data.enrollmentInfo.promotions.normalPromotions
                        ) { _, data, view ->
                            val binding = LayoutCardNormalPromotionBinding.bind(view)

                            binding.tvPromotionName.text = data.title
                            binding.tvPromotionRate.text = "${data.rate}% OFF"
                        }
                        binding.rvNormalPromotions.adapter = normalPromotionsAdapter
                        binding.rvNormalPromotions.setHasFixedSize(true)

                    }





                    if(isPremiumMember){
                        binding.mcPremiumMemberDiscount.visible(true)
                        binding.tvPremiumMemberSaveRate.text = "${data.enrollmentInfo.promotions.premiumPromotion.rate}% OFF"
                    }





                    /**
                    * Payment Methods
                     */
                    binding.tvUserCurrentPointsInPaymentMethods.text = "${data.enrollmentInfo.enrollmentMethod.point.available} Pts"
                    binding.tvUserCurrentBalanceInPaymentMethod.text = "${data.enrollmentInfo.enrollmentMethod.balance.available} MMK"

                    binding.tvTotalCostPointsInPaymentMethod.text = "${data.enrollmentInfo.enrollmentMethod.point.total} Pts"
                    binding.tvTotalCostPriceInPaymentMethod.text = "${data.enrollmentInfo.enrollmentMethod.balance.total} MMK"

                    val isNeeded = data.enrollmentInfo.enrollmentMethod.balance.isNeeded

                    if(isNeeded){
                        binding.mcInsufficientPayWithBalance.visible(true)
                        binding.tvInsufficientUserCurrentBalanceInPaymentMethod.text = "You need ${data.enrollmentInfo.enrollmentMethod.balance.needed} MMK more.Please top up to proceed."
                    }


                    /**
                     * Price Summary
                     */
                    binding.tvOriginalPrice.text = "${data.enrollmentInfo.enrollmentMethod.balance.summary.original} MMK"


                    normalPromotionsInPriceSummaryAdapter = BaseRecyclerViewAdapter(
                        R.layout.layout_card_normal_promotion_in_summary, false,
                        enrollData?.enrollmentInfo?.enrollmentMethod?.balance?.summary?.discounts!!
                    ) { id, data, view ->
                        val binding = LayoutCardNormalPromotionInSummaryBinding.bind(view)
                        binding.tvPromotionName.text = data.title
                        binding.tvSavedPrice.text = "-${data.saved} MMK"
                    }

                    if(normalPromotionCount!=0){
                        binding.rvNormalPromotionsInPriceSummary.visible(true)
                        binding.rvNormalPromotionsInPriceSummary.adapter = normalPromotionsInPriceSummaryAdapter
                        binding.rvNormalPromotionsInPriceSummary.setHasFixedSize(true)
                    }


                    binding.tvTotalCostAmount.text = "${data.enrollmentInfo.enrollmentMethod.balance.total} MMK"

                }
                is Resource.Failure -> {
                    Toast.makeText(requireContext(), resource.errorBody.toString(), Toast.LENGTH_SHORT).show()

                }
                is Resource.Loading -> {
                }
            }
        }
    }

    // Function to update the UI based on selected payment method
    @SuppressLint("SetTextI18n")
    private fun updatePaymentMethodSelection(method: String) {
        currentPaymentMethod = method

        // Get color values
        val selectedColor = ContextCompat.getColor(requireContext(), R.color.selected_stroke_color)
        val unselectedColor = ContextCompat.getColor(requireContext(), R.color.unselected_stroke_color)
        val text_color_1 = ContextCompat.getColor(requireContext(), R.color.text_color_1)


        binding.mcPayWithPoint.strokeColor = if (currentPaymentMethod == "Credit") selectedColor else unselectedColor
        binding.mcPayWithBalance.strokeColor = if (currentPaymentMethod == "Balance") selectedColor else unselectedColor

        binding.tvTotalCostPointsInPaymentMethod.setTextColor(if (currentPaymentMethod == "Credit") selectedColor else text_color_1)
        binding.tvTotalCostPriceInPaymentMethod.setTextColor(if (currentPaymentMethod == "Balance") selectedColor else text_color_1)

        binding.iconPayWithPoints.setColorFilter(if (currentPaymentMethod == "Credit") selectedColor else unselectedColor)
        binding.iconPayWithBalance.setColorFilter(if (currentPaymentMethod == "Balance") selectedColor else unselectedColor)


        var arrowIcon = ContextCompat.getDrawable(requireContext(), R.drawable.chevron_right_24px)
        var checkIcon = ContextCompat.getDrawable(requireContext(), R.drawable.check_circle_24px)

        binding.iconPayWithPoints.setImageDrawable(if (currentPaymentMethod == "Credit") checkIcon  else arrowIcon)
        binding.iconPayWithBalance.setImageDrawable(if (currentPaymentMethod == "Balance") checkIcon else arrowIcon)





        binding.rvNormalPromotionsInPriceSummary.visible(true)
        if (currentPaymentMethod == "Balance"){
            normalPromotionsInPriceSummaryAdapter = BaseRecyclerViewAdapter(
                R.layout.layout_card_normal_promotion_in_summary, false,
                enrollData?.enrollmentInfo?.enrollmentMethod?.balance?.summary?.discounts!!
            ) { id, data, view ->
                val binding = LayoutCardNormalPromotionInSummaryBinding.bind(view)
                binding.tvPromotionName.text = data.title
                binding.tvSavedPrice.text = "-${data.saved} MMK"
            }
            binding.rvNormalPromotionsInPriceSummary.adapter = normalPromotionsInPriceSummaryAdapter
        } else{
            normalPromotionsInPriceSummaryAdapter = BaseRecyclerViewAdapter(
                R.layout.layout_card_normal_promotion_in_summary, false,
                enrollData?.enrollmentInfo?.enrollmentMethod?.point?.summary?.discounts!!
            ) { id, data, view ->
                val binding = LayoutCardNormalPromotionInSummaryBinding.bind(view)
                binding.tvPromotionName.text = data.title
                binding.tvSavedPrice.text = "-${data.saved} Pts"
            }
            binding.rvNormalPromotionsInPriceSummary.adapter = normalPromotionsInPriceSummaryAdapter
        }





        /**
         * Insufficient Card
         */

        val balance = enrollData?.enrollmentInfo?.enrollmentMethod?.balance
        val isNeededBalance = balance?.isNeeded
        val neededBalance = balance?.needed

        if(currentPaymentMethod == "Balance" && isNeededBalance == true){
            binding.mcInsufficientPayWithBalance.visible(true)
            binding.tvInsufficientUserCurrentBalanceInPaymentMethod.text = "You need $neededBalance MMK more.Please top up to proceed."
        }else{
            binding.mcInsufficientPayWithBalance.visible(false)
        }

        val point = enrollData?.enrollmentInfo?.enrollmentMethod?.point
        val isNeededPoint = point?.isNeeded
        val neededPoint = point?.needed
        if(currentPaymentMethod == "Credit" && isNeededPoint == true){
            binding.mcInsufficientPayWithPoint.visible(true)
            binding.tvInsufficientUserCurrentPointInPaymentMethod.text = "You need ${neededPoint} more points. Earn points or use another payment method."
        }else{
            binding.mcInsufficientPayWithPoint.visible(false)
        }








        /**
         *  Update Price Summary and conditional visibility based on selection and premium status
         */
        enrollData?.enrollmentInfo?.enrollmentMethod?.let { data ->
            // Update original price and total amount based on selected method
            if (currentPaymentMethod == "Balance") {
                binding.tvOriginalPrice.text = "${data.balance.summary.original} MMK"
                binding.tvTotalCostAmount.text = "${data.balance.total} MMK"
            } else { // currentPaymentMethod == "Credit"
                binding.tvOriginalPrice.text = "${data.point.summary.original} Pts"
                binding.tvTotalCostAmount.text = "${data.point.total} Pts"
            }
        }
    }




}