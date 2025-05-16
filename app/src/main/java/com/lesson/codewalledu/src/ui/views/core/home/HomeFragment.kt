package com.lesson.codewalledu.src.ui.views.core.home

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.lesson.codewalledu.R
import com.lesson.codewalledu.databinding.FragmentHomeScreenBinding
import com.lesson.codewalledu.databinding.LayoutCardBlogsCardBinding
import com.lesson.codewalledu.databinding.LayoutCardFreeCoursesBinding
import com.lesson.codewalledu.databinding.LayoutCardPopularCoursesBinding
import com.lesson.codewalledu.databinding.LayoutCardUpcomingCoursesBinding
import com.lesson.codewalledu.databinding.LayoutCwCuponsBinding
import com.lesson.codewalledu.databinding.LayoutCwTabBinding
import com.lesson.codewalledu.src.ui.adapters.BaseRecyclerViewAdapter
import com.lesson.codewalledu.src.ui.views.core.BottomNavScrollListener
import com.lesson.codewalledu.src.ui.views.core.MainFragment
import com.lesson.codewalledu.src.utils.APP_COLOR_BLUE
import com.lesson.codewalledu.src.utils.APP_COLOR_CYAN
import com.lesson.codewalledu.src.utils.Resource
import com.lesson.codewalledu.src.utils.helpers.applyGradient
import com.lesson.codewalledu.src.utils.helpers.visible
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import java.util.Timer
import java.util.TimerTask

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home_screen) {
    lateinit var binding: FragmentHomeScreenBinding


    private val homeViewModel: HomeViewModel by viewModels()


    private var timer: Timer? = null
    private var timerTask: TimerTask? = null
    private var currentPage = 0
    private var isGoingBack = false


    private var topicStatus = false
    private var popularCoursesStatus = true
    private var freeCoursesStatus = true
    private var upcomingCoursesStatus = true
    private var blogsStatus = true


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
                        binding.viewPagerCouponsExplore.currentItem = --currentPage
                    }
                    else{
                        binding.viewPagerCouponsExplore.currentItem = currentPage++
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
                        binding.viewPagerCouponsExplore.currentItem = --currentPage
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
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentHomeScreenBinding.bind(view)

        binding.toolbar.notificationIcon.visible(true)
        binding.toolbar.profileIcon.visible(true)
        updateVisibility()



        //Sliders
        val couponsAdapter = BaseRecyclerViewAdapter(
            R.layout.layout_cw_cupons,
            false,
            homeViewModel.getCouponsList()
        )
        { position, data, itemView: View ->
            val itemBinding = LayoutCwCuponsBinding.bind(itemView)
            itemBinding.apply {
                imageView.setImageResource(data.image)
                iconView.setImageResource(data.icon)
                cwDateText.text = data.date
                cwTitle.text = data.title
                cwSubTitle.text = data.subTitle
            }
        }
        binding.viewPagerCouponsExplore.adapter = couponsAdapter
        binding.couponsDotsIndicator.attachTo(binding.viewPagerCouponsExplore)


        /**
         * Topic Section
         */
        homeViewModel.topicDataResponse.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    topicStatus = true
                    updateVisibility()
                    val tabAdapter = BaseRecyclerViewAdapter(
                        R.layout.layout_cw_tab, false,
                        it.value.data
                    )
                    { _, data, view ->
                        val itemBinding = LayoutCwTabBinding.bind(view)
                        itemBinding.tvTab.text = data.title
                        itemBinding.tvTab.setTextColor(
                            Color.parseColor(data.textColor)
                        )
                        itemBinding.apply {

                            itemBinding.linearLayoutTabBackground.setBackgroundColor(
                                Color.parseColor(data.backgroundColor)
                            )

                            Picasso.get()
                                .load(data.iconUrl)
                                .placeholder(R.drawable.topic_programming)
                                .error(R.drawable.baseline_search_24)
                                .into(ivTab)

                            itemBinding.root.setOnClickListener {
                                val action = HomeFragmentDirections.actionHomeFragmentToTopicFragment(data.title)
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
        homeViewModel.popularCoursesDataResponse.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    popularCoursesStatus = true
                    updateVisibility()
                    var popularCoursesAdapter = BaseRecyclerViewAdapter(
                        R.layout.layout_card_popular_courses, false,
                        it.value.data
                    ) { _, data, view ->
                        val itemBinding = LayoutCardPopularCoursesBinding.bind(view)
                        itemBinding.apply {
//                    ivPopularCourse.setImageResource(data.image)

                            Picasso.get()
                                .load(data.imageUrl)
                                .fit()
                                .placeholder(R.drawable.topic_programming)
                                .error(R.drawable.baseline_search_24)
                                .into(ivCourse)

                            tvTitle.text = data.title

                            Picasso.get()
                                .load(data.durationIcon)
                                .placeholder(R.drawable.baseline_clock_loader_10_24px)
                                .error(R.drawable.baseline_running_with_errors_24)
                                .into(ivDuration)

                            Log.d("Icon", "onViewCreated: ${data.durationIcon}")

                            tvDuration.text = "${data.duration} hours"
                            tvChapterCount.text = "${data.chapters} Chapters"
                            tvPrice.text = "Fees: MMK ${data.fees}"
                        }
                    }
                    binding.rvPopularCoursesExplore.adapter = popularCoursesAdapter
                    binding.tvSeeAllPopularCourses.setOnClickListener {
                        findNavController().navigate(R.id.action_homeFragment_to_currentCoursesFragment)
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
         * Free Courses
         */
        homeViewModel.freeCoursesDataResponse.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    freeCoursesStatus = true
                    updateVisibility()
                    val freeCoursesAdapter = BaseRecyclerViewAdapter(
                        R.layout.layout_card_free_courses, false,
                        it.value.data
                    )
                    { _, data, view ->
                        val itemBinding = LayoutCardFreeCoursesBinding.bind(view)
                        itemBinding.apply {
                            Picasso.get()
                                .load(data.imageUrl)
                                .fit()
                                .placeholder(R.drawable.topic_programming)
                                .error(R.drawable.baseline_search_24)
                                .into(ivCourse)

                            tvTitle.text = data.title

                            Picasso.get()
                                .load(data.durationIcon)
                                .placeholder(R.drawable.baseline_clock_loader_10_24px)
                                .error(R.drawable.baseline_running_with_errors_24)
                                .into(ivDuration)

                            tvDuration.text = "${data.duration} hours"
                            tvChapterCount.text = "${data.chapters} Chapters"
                        }
                    }

                    binding.rvFreeCoursesExplore.adapter = freeCoursesAdapter
                    binding.tvSeeAllFreeCourses.setOnClickListener {
                        findNavController().navigate(R.id.action_homeFragment_to_currentCoursesFragment)
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
         * Upcoming Courses
         */
        homeViewModel.upComingCoursesDataResponse.observe(viewLifecycleOwner){
            when(it){
                is Resource.Success ->{
                    upcomingCoursesStatus = true
                    updateVisibility()
                    var upcomingCoursesAdapter = BaseRecyclerViewAdapter(R.layout.layout_card_upcoming_courses, false,
                        it.value.data
                    )
                    { _, data, view ->
                        val itemBinding = LayoutCardUpcomingCoursesBinding.bind(view)
                        itemBinding.apply {


                            Picasso.get()
                                .load(data.imageUrl)
                                .fit()
                                .placeholder(R.drawable.topic_programming)
                                .error(R.drawable.baseline_search_24)
                                .into(ivCourse)
                            tvTitleUpcomingCourse.text = data.title
                        }

                    }
                    binding.rvUpcomingCoursesExplore.adapter = upcomingCoursesAdapter

                    binding.tvSeeAllUpcomingCourses.setOnClickListener {
                        findNavController().navigate(R.id.action_homeFragment_to_upComingFragment)
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
         * Blogs
         */
        binding.tvOurBlogsSub.applyGradient(APP_COLOR_BLUE, APP_COLOR_CYAN)
        homeViewModel.blogDataResponse.observe(viewLifecycleOwner){
            when(it){
                is Resource.Success -> {
                    blogsStatus = true
                    updateVisibility()
                    val layoutManager = LinearLayoutManager(requireContext())
                    var ourBlogsAdapter = BaseRecyclerViewAdapter(
                        R.layout.layout_card_blogs_card, false,
                        it.value.data.reversed().take(3)
                    )
                    {
                            position, data, view ->
                        val itemBinding = LayoutCardBlogsCardBinding.bind(view)
                        itemBinding.apply {
                            Picasso.get()
                                .load(data.imageUrl)
                                .fit()
                                .placeholder(R.drawable.topic_programming)
                                .error(R.drawable.baseline_search_24)
                                .into(ivBlogImage)
                            tvTitleBlogs.text = data.contentTitle
                            tvCreatedDate.text= data.createdAt
                            tvReadingTime.text = "${data.readingTime} min read"
                            tvShortDescription.text = data.contentBody
                            blogRoot.setOnClickListener{
                                val action = HomeFragmentDirections.actionHomeFragmentToReadingBlogFragment(data)
                                findNavController().navigate(action)
                            }
                        }
                    }
                    binding.rvOurBlogsExplore.setHasFixedSize(true)
                    binding.rvOurBlogsExplore.layoutManager = layoutManager
                    binding.rvOurBlogsExplore.adapter = ourBlogsAdapter




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



        val scrollListener = BottomNavScrollListener(
            MainFragment.binding.bottomNavHolder,
        )
        binding.scrollView.setOnScrollChangeListener(scrollListener)


    }



    private fun updateVisibility() {
        val allStatus = topicStatus && popularCoursesStatus && freeCoursesStatus && upcomingCoursesStatus && blogsStatus
        binding.scrollView.visibility = if (allStatus) View.VISIBLE else View.GONE
        binding.shimmerLayout.visibility = if (allStatus) View.GONE else View.VISIBLE
        if (allStatus) {
            binding.shimmerLayout.stopShimmer()
        } else {
            binding.shimmerLayout.startShimmer()
        }
    }





}