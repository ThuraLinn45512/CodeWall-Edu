package com.lesson.codewalledu.src.ui.views.core.explore.cheat_sheets

import android.annotation.SuppressLint
import androidx.fragment.app.viewModels
import android.os.Bundle
import android.view.MotionEvent
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lesson.codewalledu.R
import com.lesson.codewalledu.databinding.FragmentCheatSheetsBinding
import com.lesson.codewalledu.databinding.ItemCheatSheetImageBinding
import com.lesson.codewalledu.databinding.LayoutCardCheatSheetsBinding
import com.lesson.codewalledu.src.ui.views.core.explore.books.ViewBookFragmentDirections
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

    @SuppressLint("ClickableViewAccessibility", "RestrictedApi")
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
            // CheatSheetData တွေ reload လုပ်ဖို့ ViewModel ကို ခေါ်ပါ။
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
                                .error(R.drawable.rectangle_placeholder)
                                .into(ivLogo)
                            tvTechCategory.text = data.techCategoryName?: "nothing"
                            tvCreatedDate.text = data.createdAt
                            tvShortDescription.text = data.description?: "Fake CheatSheetData for description"


                            val imageList = data.cheatSheetContents
                            var imagesAdapter = BaseRecyclerViewAdapter(
                                R.layout.item_cheat_sheet_image, false,
                                imageList
                            )
                            {
                                    position, data, view ->
                                val itemBinding = ItemCheatSheetImageBinding.bind(view)

                                // Image loading မစခင် shimmer ကို စတင်ပါ။


                                itemBinding.apply {
                                    ivDiscoverImage.setImageDrawable(null)
                                    shimmerImageContainer.startShimmer()//Clear previous image if any
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
                                    root.setOnClickListener {
                                        val navController = findNavController()
                                        val currentDest = navController.currentDestination
                                        println("Current NavController Destination: ID=${currentDest?.id?.let { resources.getResourceEntryName(it) }}, Label=${currentDest?.label}, Class=${currentDest?.displayName}")

                                        try {

                                            val mainNavController = requireActivity().supportFragmentManager
                                                .findFragmentById(R.id.fragment_container_view) // ID of NavHost in MainActivity
                                                ?.childFragmentManager?.primaryNavigationFragment // This should be MainFragmentContainer
                                                ?.childFragmentManager?.findFragmentById(R.id.bottom_nav_fragment_container_view)
                                                // ID of NavHost in MainFragmentContainer
                                                ?.findNavController()

                                            if (mainNavController?.currentDestination?.id != R.id.viewCheatSheetFragment) {
                                                val action = ViewCheatSheetFragmentDirections.actionGlobalViewCheatSheetFragment(imageList.toTypedArray(),position)
                                                mainNavController?.navigate(action)
                                            }
                                        }catch (e: Exception){
                                            println(e.toString()) // Also print to System.out as you were doing
                                        }
                                    }

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

        // Inside your Fragment where the RecyclerView is
        cheatSheetsBinding.rvNews.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                // RecyclerView စပြီး scroll ဖြစ်တာနဲ့ parent ကို touch event တွေ မပို့တော့ဖို့
                // ViewPager ကို touch event တွေ ကြားမဖြတ်ဖို့ ပြောလိုက်တာ
                if (dy != 0) { // Only disallow if actually scrolling vertically
                    recyclerView.parent.requestDisallowInterceptTouchEvent(true)
                }
            }
        })

        // Optional: If you also need to handle touch events for the RecyclerView directly
        cheatSheetsBinding.rvNews.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    v.parent.requestDisallowInterceptTouchEvent(true)
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    v.parent.requestDisallowInterceptTouchEvent(false)
                }
            }
            false // Let the RecyclerView handle its own touch events
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