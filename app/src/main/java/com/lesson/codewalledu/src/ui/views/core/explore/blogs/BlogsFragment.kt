package com.lesson.codewalledu.src.ui.views.core.explore.blogs

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
import com.lesson.codewalledu.databinding.FragmentBlogsBinding
import com.lesson.codewalledu.databinding.LayoutCardBlogsBinding
import com.lesson.codewalledu.src.utils.Resource
import com.lesson.codewalledu.src.utils.adapters.BaseRecyclerViewAdapter
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class BlogsFragment : Fragment(R.layout.fragment_blogs) {

    private val blogsViewModel: BlogsViewModel by viewModels()
    lateinit var blogsBinding: FragmentBlogsBinding
    private var blogsStatus = false

    @SuppressLint("RestrictedApi", "ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        blogsBinding= FragmentBlogsBinding.bind(view)

        // Set up SwipeRefreshLayout listener
        blogsBinding.swipeRefreshLayout.setOnRefreshListener {
            // Trigger data refresh when user pulls down
            blogsBinding.swipeRefreshLayout.isRefreshing = false
            lifecycleScope.launch {
                blogsViewModel.getBlogs()
            }
        }


        blogsViewModel.blogsDataResponse.observe(viewLifecycleOwner){
            when(it){
                is Resource.Success -> {
                    blogsStatus = true
                    blogsBinding.swipeRefreshLayout.isRefreshing = false
                    updateVisibility()
                    val layoutManager = LinearLayoutManager(requireContext())
                    var ourBlogsAdapter = BaseRecyclerViewAdapter(
                        R.layout.layout_card_blogs, false,
                        it.value.data.reversed()
                    )
                    {
                            position, data, view ->
                        val itemBinding = LayoutCardBlogsBinding.bind(view)
                        itemBinding.apply {
                            Picasso.get()
                                .load(data.imageUrl)
                                .error(R.drawable.rectangle_placeholder)
                                .into(ivBlogImage)
                            tvTitleBlogs.text = data.contentTitle
                            tvCreatedDate.text= data.createdAt
                            tvReadingTime.text = "${data.readingTime} min read"
                            tvShortDescription.text = data.contentBody
                            rootCardBlog.setOnClickListener{
                                val navController = findNavController()
                                val currentDest = navController.currentDestination
                                println("Current NavController Destination: ID=${currentDest?.id?.let { resources.getResourceEntryName(it) }}, Label=${currentDest?.label}, Class=${currentDest?.displayName}")

                                try {

                                    val mainNavController = requireActivity().supportFragmentManager
                                        .findFragmentById(R.id.fragment_container_view) // ID of NavHost in MainActivity
                                        ?.childFragmentManager?.primaryNavigationFragment // This should be MainFragmentContainer
                                        ?.childFragmentManager?.findFragmentById(R.id.bottom_nav_fragment_container_view) // ID of NavHost in MainFragmentContainer
                                        ?.findNavController()

                                    if (mainNavController?.currentDestination?.id != R.id.readingBlogFragment) {
                                        val action = ReadingBlogFragmentDirections.actionGlobalReadingBlogFragment(data.blogUrl)
                                        mainNavController?.navigate(action)
                                    }
                                }catch (e: Exception){
                                    // 4. Log any exception
                                    println("NAV EXCEPTION: " + e.toString()) // Also print to System.out as you were doing

                                }

                            }
                        }
                    }
                    blogsBinding.rvBlogs.setHasFixedSize(true)
                    blogsBinding.rvBlogs.layoutManager = layoutManager
                    blogsBinding.rvBlogs.adapter = ourBlogsAdapter


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


        // Inside your Fragment where the RecyclerView is
        blogsBinding.rvBlogs.addOnScrollListener(object : RecyclerView.OnScrollListener() {
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
        blogsBinding.rvBlogs.setOnTouchListener { v, event ->
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
        val allStatus = blogsStatus
        blogsBinding.rvBlogs.visibility = if (allStatus) View.VISIBLE else View.GONE
        blogsBinding.shimmerLayout.visibility = if (allStatus) View.GONE else View.VISIBLE
        if (allStatus) {
            blogsBinding.shimmerLayout.stopShimmer()
        } else {
            blogsBinding.shimmerLayout.startShimmer()
        }
    }
}