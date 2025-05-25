package com.lesson.codewalledu.src.ui.views.core.explore.cheat_sheets

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.lesson.codewalledu.R
import com.lesson.codewalledu.databinding.FragmentCheatSheetsBinding
import com.lesson.codewalledu.databinding.ItemCheatSheetImageBinding
import com.lesson.codewalledu.databinding.LayoutCardCheatSheetsBinding
import com.lesson.codewalledu.src.utils.Resource
import com.lesson.codewalledu.src.utils.adapters.BaseRecyclerViewAdapter
import com.lesson.codewalledu.src.utils.helpers.visible
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CheatSheetsFragment : Fragment(R.layout.fragment_cheat_sheets) {

    private val viewModel: CheatSheetsViewModel by viewModels()
    lateinit var cheatSheetsBinding: FragmentCheatSheetsBinding
    private var discoverStatus = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cheatSheetsBinding = FragmentCheatSheetsBinding.bind(view)

        // Solution 1: Control SwipeRefreshLayout based on NestedScrollView's scroll position
        cheatSheetsBinding.nestedScrollView.setOnScrollChangeListener {
                v, scrollX, scrollY, oldScrollX, oldScrollY ->
            // Enable SwipeRefreshLayout only when the NestedScrollView is at the very top
            cheatSheetsBinding.swipeRefreshLayout.isEnabled = scrollY == 0
        }

        // Set up SwipeRefreshLayout listener for refresh action
        cheatSheetsBinding.swipeRefreshLayout.setOnRefreshListener {
            // Data တွေ reload လုပ်ဖို့ ViewModel ကို ခေါ်ပါ။
            // ဥပမာ: viewModel.getCheatSheets()
            // reload ပြီးရင် .isRefreshing = false လုပ်ပေးပါ။
            lifecycleScope.launch {
                viewModel.getCheatSheets()
            }
        }


        viewModel.cheatSheetsDataResponse.observe(viewLifecycleOwner){
            when(it){
                is Resource.Success -> {
                    discoverStatus = true
                    updateVisibility()
                    cheatSheetsBinding.swipeRefreshLayout.isRefreshing = false // Stop refreshing
                    val layoutManager = LinearLayoutManager(requireContext())
                    var cheatSheetsAdapter = BaseRecyclerViewAdapter(
                        R.layout.layout_card_cheat_sheets, false,
                        it.value.data.reversed()
                    )
                    {
                            position, data, view ->
                        val itemBinding = LayoutCardCheatSheetsBinding.bind(view)
                        itemBinding.apply {
                            Picasso.get()
                                .load(data.logoImageUrl)
                                .placeholder(R.drawable.blogs_figma)
                                .error(R.drawable.baseline_search_24)
                                .into(ivLogo)
                            tvTechCategory.text = data.techCategoryName?: "nothing"
                            tvCreatedDate.visible(false)
                            tvShortDescription.text = data.description?: "Fake Data for description"

                            var imagesAdapter = BaseRecyclerViewAdapter(
                                R.layout.item_cheat_sheet_image, false,
                                data.cheatSheetContents
                            )
                            {
                                    position, data, view ->
                                val itemBinding = ItemCheatSheetImageBinding.bind(view)

                                // Image loading မစခင် shimmer ကို စတင်ပါ။


                                itemBinding.apply {
                                    ivDiscoverImage.setImageDrawable(null)
                                    shimmerImageContainer.startShimmer()//Clear previous image if any

//

                                    Picasso.get()
                                        .load(data.imageUrl)
                                        .error(R.drawable.baseline_running_with_errors_24)
                                        .into(ivDiscoverImage, object : Callback {
                                            override fun onSuccess() {
                                                // Image load ပြီးသွားရင် shimmer ကို ရပ်တန့်ပါ။
                                                shimmerImageContainer.stopShimmer()
                                                shimmerImageContainer.setShimmer(null) // Shimmer animation ပြီးရင် background effect ကို ဖယ်ရှားပါ။
                                            }

                                            override fun onError(e: Exception?) {
                                                // Image load မအောင်မြင်ရင်လည်း shimmer ကို ရပ်တန့်ပါ။
                                                shimmerImageContainer.stopShimmer()
                                                shimmerImageContainer.setShimmer(null)
                                                // Error icon/placeholder ပြပါ။
                                                ivDiscoverImage.setImageResource(R.drawable.baseline_running_with_errors_24)
                                            }
                                        })
                                }
                            }
                             rvHorizontalImages.setHasFixedSize(true)
                             rvHorizontalImages.layoutManager = LinearLayoutManager(requireContext(),
                                 LinearLayoutManager.HORIZONTAL,false)
                             rvHorizontalImages.adapter = imagesAdapter



                        }
                    }
                    cheatSheetsBinding.rvNews.setHasFixedSize(true)
                    cheatSheetsBinding.rvNews.layoutManager = layoutManager
                    cheatSheetsBinding.rvNews.adapter = cheatSheetsAdapter


                }
                is Resource.Failure -> {
                    discoverStatus = true
                    updateVisibility()
                    cheatSheetsBinding.swipeRefreshLayout.isRefreshing = false // Stop refreshing
                    Toast.makeText(requireContext(), it.errorBody.toString(), Toast.LENGTH_SHORT).show()
                }
                is Resource.Loading -> {
                    discoverStatus = false
                    updateVisibility()
                }
            }
        }
    }

    private fun updateVisibility() {
        val status = discoverStatus
        cheatSheetsBinding.rvNews.visibility = if (status) View.VISIBLE else View.GONE
        cheatSheetsBinding.shimmerLayout.visibility = if (status) View.GONE else View.VISIBLE
        if (status) {
            cheatSheetsBinding.shimmerLayout.stopShimmer()
        } else {
            cheatSheetsBinding.shimmerLayout.startShimmer()
        }
    }
}