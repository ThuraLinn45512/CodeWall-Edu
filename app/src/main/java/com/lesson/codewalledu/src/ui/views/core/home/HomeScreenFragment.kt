package com.lesson.codewalledu.src.ui.views.core.home

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.lesson.codewalledu.R
import com.lesson.codewalledu.databinding.FragmentHomeScreenBinding
import com.lesson.codewalledu.databinding.LayoutCardBannersBinding
import com.lesson.codewalledu.databinding.LayoutCardBlogsBinding
import com.lesson.codewalledu.databinding.LayoutCardFreeCoursesBinding
import com.lesson.codewalledu.databinding.LayoutCardPopularCoursesBinding
import com.lesson.codewalledu.databinding.LayoutCardUpcomingCoursesBinding
import com.lesson.codewalledu.databinding.LayoutCwTabBinding
import com.lesson.codewalledu.src.ui.views.core.BottomNavScrollListener
import com.lesson.codewalledu.src.ui.views.core.MainFragmentContainer
import com.lesson.codewalledu.src.ui.views.core.explore.blogs.BlogsViewModel
import com.lesson.codewalledu.src.utils.APP_COLOR_BLUE
import com.lesson.codewalledu.src.utils.APP_COLOR_CYAN
import com.lesson.codewalledu.src.utils.Resource
import com.lesson.codewalledu.src.utils.UserPreferences
import com.lesson.codewalledu.src.utils.adapters.BaseRecyclerViewAdapter
import com.lesson.codewalledu.src.utils.helpers.applyGradient
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import jakarta.inject.Inject
import kotlinx.coroutines.launch
import java.util.Timer
import java.util.TimerTask



@AndroidEntryPoint
class HomeScreenFragment : Fragment(R.layout.fragment_home_screen) {
    lateinit var binding: FragmentHomeScreenBinding


    private val homeScreenViewModel: HomeScreenViewModel by viewModels()
    private val blogsViewModel: BlogsViewModel by viewModels()

    private var timer: Timer? = null
    private var timerTask: TimerTask? = null
    private var currentPage = 0
    private var isGoingBack = false


    private var bannerStatus = false
    private var topicStatus = false
    private var blogsStatus = false
    private var popularCoursesStatus = false
    private var freeCoursesStatus = false
    private var upcomingCoursesStatus = false



    @Inject
    lateinit var userPreferences:UserPreferences


    private fun startAutoSwipe() {
        timer = Timer()
        timerTask= object : TimerTask() {
            override fun run() {
                requireActivity().runOnUiThread {
                    if (currentPage == 4  && !isGoingBack) {
                        // Pause on the final page
                        isGoingBack = true
                        stopAutoSwipeBack()
                        startAutoSwipeBack()
                    } else if(currentPage == 0 && isGoingBack){
                        isGoingBack = false
                        stopAutoSwipeBack()
                        startAutoSwipe()
                    } else if(isGoingBack){
                        binding.viewPagerBanners.currentItem = --currentPage
                    }
                    else{
                        binding.viewPagerBanners.currentItem = currentPage++
                    }
                }
            }
        }
        timer?.schedule(timerTask, 3000, 5000) // Delay: 1s, Period: 1s
    }

    private fun startAutoSwipeBack() {
        timer = Timer()
        timerTask = object : TimerTask() {
            override fun run() {
                requireActivity().runOnUiThread {
                    if(currentPage == 0 && isGoingBack){
                        isGoingBack = false
                        stopAutoSwipeBack()
                        startAutoSwipe()
                    } else if(isGoingBack){
                        binding.viewPagerBanners.currentItem = --currentPage
                    }
                }
            }
        }
        timer?.schedule(timerTask, 3000, 5000) // Delay: 1s, Period: 1s
    }

    private fun stopAutoSwipeBack() {
        timer?.cancel()
        timer = null
        timerTask?.cancel()
        timerTask = null
    }

    override fun onResume() {
        super.onResume()
        startAutoSwipe()
        resetToolbarSearchVisibility()
    }



    private fun resetToolbarSearchVisibility() {
        // Ensure elements that hide for search are visible by default
        binding.toolbar.toolbarBrandIcon.visibility = View.VISIBLE
        binding.toolbar.toolbarAppName.visibility = View.VISIBLE // Keep app name visible
        binding.toolbar.toolbarSpacerView.visibility = View.VISIBLE
        binding.toolbar.notificationIcon.visibility = View.VISIBLE
        binding.toolbar.profileIcon.visibility = View.VISIBLE

        binding.toolbar.mainSearchView.setQuery("", false) // Clear any text
        binding.toolbar.mainSearchView.clearFocus() // Remove focus
        binding.toolbar.mainSearchView.isIconified = true // Force it to iconify (collapse)

        // Ensure its layout parameters are reset to WRAP_CONTENT

        binding.toolbar.mainSearchView.layoutParams.width = LinearLayout.LayoutParams.WRAP_CONTENT // Restore original width
        binding.toolbar.mainSearchView.requestLayout()

    }


    @SuppressLint("SetTextI18n", "UseKtx")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentHomeScreenBinding.bind(view)

        /**
         * Premium Member Status ကို observe လုပ်ခြင်း
         */
        homeScreenViewModel.userDataResponse.observe(viewLifecycleOwner) { // Fragment ရဲ့ View Lifecycle နဲ့ တွဲပြီး observe လုပ်ပါတယ်။
                response ->
                when(response){
                    is Resource.Success->{
                        var isPremium = response.value.data.isPremiumMember
                        if (isPremium) {
                            binding.toolbar.toolbarAppName.text = "Premium"
                            viewLifecycleOwner.lifecycleScope.launch {
                                userPreferences.setUserPremium(true)
                            }
                        } else {
                            binding.toolbar.toolbarAppName.text = "CodeWall Edu"
                            viewLifecycleOwner.lifecycleScope.launch {
                                userPreferences.setUserPremium(false)
                            }
                        }
                    }
                    is Resource.Failure->{

                    }
                    is Resource.Loading->{

                    }
                }

        }


        // Find your SearchView
        val searchView= binding.toolbar.mainSearchView


        // --- Handle On Back Pressed ---
        val callback = object : OnBackPressedCallback(true /* isEnabled */) {
            override fun handleOnBackPressed() {
                        requireActivity().finish() // Exit the app
                        return // Stop further execution

                    // --- END MODIFIED CODE ---

            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
        // --- End Handle On Back Pressed ---



        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // User submitted the query, navigate to search results
                query?.let {
                    if (it.isNotBlank()) {

                        val action = HomeScreenFragmentDirections.actionHomeFragmentToSearchResultsFragment(it)
                        findNavController().navigate(action)
                        searchView.setQuery("", false) // Clear search view after submission
                        searchView.clearFocus() // Hide keyboard
                    }
                }
                return true // Indicate that the query has been handled
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Optional: Handle live search suggestions here if needed
                return false // We're not handling suggestions, so return false
            }
        })

        // Set up the OnActionExpandListener for the SearchView
        searchView.setOnQueryTextFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                // SearchView is activated (gained focus)
                binding.toolbar.toolbarBrandIcon.visibility = View.GONE
                binding.toolbar.toolbarAppName.visibility = View.GONE
                binding.toolbar.toolbarSpacerView.visibility = View.GONE // Hide spacer
                binding.toolbar.notificationIcon.visibility = View.GONE // Hide other icons as search expands
                binding.toolbar.profileIcon.visibility = View.GONE // Hide other icons as search expands
                // Optionally adjust SearchView's layout_width here if not handled by iconifiedByDefault
                // (e.g., if you want it to truly fill_parent)
                searchView.layoutParams.width = LinearLayout.LayoutParams.MATCH_PARENT
                searchView.requestLayout()

            } else {
                // SearchView lost focus (collapsed)
                resetToolbarSearchVisibility()
            }
        }


        /**
         * Banner Section
         */
        homeScreenViewModel.bannersDataResponse.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    bannerStatus = true
                    updateVisibility()


                    val bannerAdapter = BaseRecyclerViewAdapter(
                        R.layout.layout_card_banners, false,
                        it.value.data
                    )
                    { _, data, view ->
                        val itemBinding = LayoutCardBannersBinding.bind(view)
                        itemBinding.apply {
                            Picasso.get()
                                .load(data.imageUrl)
                                .placeholder(R.drawable.rectangle_placeholder)
                                .error(R.drawable.search_24)
                                .into(ivBannner)
                        }
                    }

                    binding.viewPagerBanners.adapter = bannerAdapter
                    binding.couponsDotsIndicator.attachTo(binding.viewPagerBanners)

                }

                is Resource.Failure -> {
                    Toast.makeText(requireContext(), it.errorBody.toString(), Toast.LENGTH_SHORT)
                        .show()
                }

                is Resource.Loading -> {
                    topicStatus = false
                    updateVisibility()
                }
            }
        }


        /**
         * Topic Section
         */

        homeScreenViewModel.shuffledTopicDataResponse.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    topicStatus = true
                    updateVisibility()


                    val topicData = it.value.data

                    val tabAdapter = BaseRecyclerViewAdapter(
                        R.layout.layout_cw_tab, false,
                        topicData
                    )
                    { _, data, view ->
                        val itemBinding = LayoutCwTabBinding.bind(view)
                        itemBinding.apply {
                            tvTab.text = data.name
                            tvTab.setTextColor(Color.parseColor(data.textColor))
                            linearLayoutTabBackground.setBackgroundColor(Color.parseColor(data.backgroundColor))
                            Picasso.get()
                                .load(data.iconUrl)
                                .placeholder(R.drawable.circle_placeholder)
                                .error(R.drawable.rectangle_placeholder)
                                .into(ivTab)

                            linearLayoutTabBackground.setOnClickListener {
                                val action = HomeScreenFragmentDirections.actionHomeScreenFragmentToShowTopicCoursesFragment(data.name,data.slug)
                                findNavController().navigate(action)
                            }

                        }
                    }
                    binding.rvTabsExplore.setHasFixedSize(true)
                    binding.rvTabsExplore.adapter = tabAdapter

                }

                is Resource.Failure -> {
                    Toast.makeText(requireContext(), it.errorBody.toString(), Toast.LENGTH_SHORT)
                        .show()
                }

                is Resource.Loading -> {
                    topicStatus = false
                    updateVisibility()
                }
            }
        }

        /**
         * Popular Courses Section
         */

        homeScreenViewModel.popularCoursesDataResponse.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    popularCoursesStatus = true
                    updateVisibility()
                    val data = it.value.data
                    var popularCoursesAdapter = BaseRecyclerViewAdapter(
                        R.layout.layout_card_popular_courses, false,
                        it.value.data
                    ) { _, data, view ->
                        val itemBinding = LayoutCardPopularCoursesBinding.bind(view)
                        itemBinding.apply {
                            Picasso.get()
                                .load(data.imageUrl)
                                .placeholder(R.drawable.rectangle_placeholder)
                                .error(R.drawable.rectangle_placeholder)
                                .into(ivCourse)

                            tvTitle.text = data.courseTitle

                            tvDuration.text = "${data.length} hours"
                            tvChapterCount.text = "${data.chapterCount} Chapters"
                            tvPrice.text = "Fees: MMK ${data.courseFees}"
                            cardRoot.setOnClickListener {
                                val action = HomeScreenFragmentDirections.actionHomeScreenFragmentToShowCourseDetailsFragment(data.id)
                                findNavController().navigate(action)
                            }
                        }
                    }
                    binding.rvPopularCoursesExplore.adapter = popularCoursesAdapter
                    binding.tvSeeAllPopularCourses.setOnClickListener {
                        val action = HomeScreenFragmentDirections.actionHomeScreenFragmentToShowAllCoursesFragment(data.toTypedArray(),"Popular Courses")
                        findNavController().navigate(action)
                    }


                }

                is Resource.Failure -> {
                    Toast.makeText(requireContext(), it.errorBody.toString(), Toast.LENGTH_SHORT)
                        .show()
                }

                is Resource.Loading -> {
                    popularCoursesStatus = false
                    updateVisibility()
                }
            }
        }


        /**
         * Free Courses Section
         */

        homeScreenViewModel.freeCoursesDataResponse.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    freeCoursesStatus = true
                    updateVisibility()
                    val data = it.value.data
                    val freeCoursesAdapter = BaseRecyclerViewAdapter(
                        R.layout.layout_card_free_courses, false,
                        it.value.data
                    )
                    { _, data, view ->
                        val itemBinding = LayoutCardFreeCoursesBinding.bind(view)
                        itemBinding.apply {

                            Picasso.get()
                                .load(data.imageUrl)
                                .placeholder(R.drawable.rectangle_placeholder)
                                .error(R.drawable.rectangle_placeholder)
                                .into(ivCourse)

                            tvTitle.text = data.courseTitle

                            tvDuration.text = "${data.length} hours"
                            tvChapterCount.text = "${data.chapterCount} Lessons"
                            cardRoot.setOnClickListener {
                                val action = HomeScreenFragmentDirections.actionHomeScreenFragmentToShowCourseDetailsFragment(data.id)
                                findNavController().navigate(action)
                            }
                        }
                    }

                    binding.rvFreeCoursesExplore.adapter = freeCoursesAdapter
                    binding.tvSeeAllFreeCourses.setOnClickListener {
                        val action = HomeScreenFragmentDirections.actionHomeScreenFragmentToShowAllCoursesFragment(data.toTypedArray(),"Free Courses")
                        findNavController().navigate(action)
                    }
                }

                is Resource.Failure -> {
                    Toast.makeText(requireContext(), it.errorBody.toString(), Toast.LENGTH_SHORT)
                        .show()
                }

                is Resource.Loading -> {
                    freeCoursesStatus = false
                    updateVisibility()
                }
            }
        }


         /**
         * Upcoming Courses Section
         */


        homeScreenViewModel.upcomingCoursesDataResponse.observe(viewLifecycleOwner){
            when(it){
                is Resource.Success ->{
                    upcomingCoursesStatus = true
                    updateVisibility()
                    val data = it.value.data
                    var upcomingCoursesAdapter = BaseRecyclerViewAdapter(R.layout.layout_card_upcoming_courses, false,
                        it.value.data
                    )
                    { _, data, view ->
                        val itemBinding = LayoutCardUpcomingCoursesBinding.bind(view)
                        itemBinding.apply {


                            Picasso.get()
                                .load(data.imageUrl)
                                .placeholder(R.drawable.rectangle_placeholder)
                                .error(R.drawable.rectangle_placeholder)
                                .into(ivCourse)
                            tvTitleUpcomingCourse.text = data.courseTitle
                            cardRoot.setOnClickListener {
                                val action = HomeScreenFragmentDirections.actionHomeScreenFragmentToShowCourseDetailsFragment(data.id)
                                findNavController().navigate(action)
                            }
                        }

                    }
                    binding.rvUpcomingCoursesExplore.adapter = upcomingCoursesAdapter

                    binding.tvSeeAllUpcomingCourses.setOnClickListener {
                        val action = HomeScreenFragmentDirections.actionHomeScreenFragmentToShowAllCoursesFragment(data.toTypedArray(),"Upcoming Courses")
                        findNavController().navigate(action)
                    }
                }

                is Resource.Failure ->{
                    Toast.makeText(requireContext(),it.errorBody.toString(),Toast.LENGTH_SHORT).show()
                }
                is Resource.Loading ->{
                    upcomingCoursesStatus = false
                    updateVisibility()
                }
            }
        }


        /**
         * Blogs Section
         */
        binding.tvOurBlogsSub.applyGradient(APP_COLOR_BLUE, APP_COLOR_CYAN)


        blogsViewModel.blogsDataResponse.observe(viewLifecycleOwner){
            when(it){
                is Resource.Success -> {
                    blogsStatus = true
                    updateVisibility()
                    val layoutManager = LinearLayoutManager(requireContext())
                    var ourBlogsAdapter = BaseRecyclerViewAdapter(
                        R.layout.layout_card_blogs, false,
                        it.value.data.reversed().take(3)
                    )
                    {
                            position, data, view ->
                        val itemBinding = LayoutCardBlogsBinding.bind(view)
                        itemBinding.apply {
                            Picasso.get()
                                .load(data.imageUrl)
                                .fit()
                                .placeholder(R.drawable.baseline_running_with_errors_24)
                                .error(R.drawable.rectangle_placeholder)
                                .into(ivBlogImage)
                            tvTitleBlogs.text = data.contentTitle
                            tvCreatedDate.text= data.createdAt
                            tvReadingTime.text = "${data.readingTime} min read"
                            tvShortDescription.text = data.contentBody
                            rootCardBlog.setOnClickListener{
                                val action = HomeScreenFragmentDirections.actionHomeFragmentToReadingBlogFragment(data.blogUrl)
                                findNavController().navigate(action)
                            }
                        }
                    }
                    binding.rvOurBlogsExplore.setHasFixedSize(true)
                    binding.rvOurBlogsExplore.layoutManager = layoutManager
                    binding.rvOurBlogsExplore.adapter = ourBlogsAdapter


                    binding.tvReadMore.setOnClickListener {
                        findNavController().navigate(R.id.action_homeFragment_to_showAllBlogsFragment)
                    }
                }
                is Resource.Failure -> {
                    Toast.makeText(requireContext(),it.errorBody.toString(),Toast.LENGTH_SHORT).show()
                }
                is Resource.Loading -> {
                    blogsStatus = false
                    updateVisibility()
                }
            }
        }


        /**
         * Bottom Nav
         */

        val scrollListener = BottomNavScrollListener(
            MainFragmentContainer.binding.bottomNavHolder,
        )
        binding.scrollView.setOnScrollChangeListener(scrollListener)


    }



    private fun updateVisibility() {
        val allStatus = bannerStatus && topicStatus &&  blogsStatus  && popularCoursesStatus && freeCoursesStatus && upcomingCoursesStatus

        binding.scrollView.visibility = if (allStatus) View.VISIBLE else View.GONE
        binding.shimmerLayout.visibility = if (allStatus) View.GONE else View.VISIBLE
        if (allStatus) {
            binding.shimmerLayout.stopShimmer()
        } else {
            binding.shimmerLayout.startShimmer()
        }
    }










}