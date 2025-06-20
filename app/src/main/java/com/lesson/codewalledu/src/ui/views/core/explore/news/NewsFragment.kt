package com.lesson.codewalledu.src.ui.views.core.explore.news

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
import com.google.android.material.divider.MaterialDividerItemDecoration
import com.lesson.codewalledu.R
import com.lesson.codewalledu.databinding.FragmentNewsBinding
import com.lesson.codewalledu.databinding.LayoutCardNewsBinding
import com.lesson.codewalledu.src.ui.views.core.explore.blogs.ReadingBlogFragmentDirections
import com.lesson.codewalledu.src.utils.Resource
import com.lesson.codewalledu.src.utils.adapters.BaseRecyclerViewAdapter
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NewsFragment : Fragment(R.layout.fragment_news) {


    private val newsViewModel: NewsViewModel by viewModels()
    lateinit var newsBinding: FragmentNewsBinding
    private var blogsStatus = true

    @SuppressLint("RestrictedApi", "ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        newsBinding = FragmentNewsBinding.bind(view)

        newsBinding.swipeRefreshLayout.setOnRefreshListener {
            lifecycleScope.launch {
                newsViewModel.getNews()
            }
        }

        newsViewModel.newsBlogsDataResponse.observe(viewLifecycleOwner){
            when(it){
                is Resource.Success -> {
                    blogsStatus = true
                    newsBinding.swipeRefreshLayout.isRefreshing = false
                    updateVisibility()
                    val layoutManager = LinearLayoutManager(requireContext())
                    var ourNewsAdapter = BaseRecyclerViewAdapter(
                        R.layout.layout_card_news, false,
                        it.value.data
                    )
                    {
                            position, data, view ->
                        val itemBinding = LayoutCardNewsBinding.bind(view)
                        itemBinding.apply {
                            Picasso.get()
                                .load(data.newsCategory.imageUrl)
                                .fit()
                                .placeholder(R.drawable.baseline_running_with_errors_24)
                                .error(R.drawable.rectangle_placeholder)
                                .into(ivLogo)
                            tvTitleNews.text = data.newsCategory.name
                            tvCreatedDate.text= data.publishedAt
                            tvTagLine.text = data.newsCategory.description?: "Fake CheatSheetData for description"
                            tvDescription.text = data.title
                            tvContent.text = data.content
                            Picasso.get()
                                .load(data.imageUrl)
                                .error(R.drawable.rectangle_placeholder)
                                .into(ivBlog)
                            newsRoot.setOnClickListener{
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

                                    if (mainNavController?.currentDestination?.id != R.id.readingNewsFragment) {
                                        val action = ReadingNewsFragmentDirections.actionGlobalReadingNewsFragment(data)
                                        mainNavController?.navigate(action)
                                    }
                                }catch (e: Exception){
                                    println(e.toString()) // Also print to System.out as you were doing
                                }
                            }
                        }
                    }
                    newsBinding.rvNews.setHasFixedSize(true)
                    newsBinding.rvNews.layoutManager = layoutManager
                    newsBinding.rvNews.adapter = ourNewsAdapter



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
        newsBinding.rvNews.addOnScrollListener(object : RecyclerView.OnScrollListener() {
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
        newsBinding.rvNews.setOnTouchListener { v, event ->
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
        val status = blogsStatus
        newsBinding.rvNews.visibility = if (status) View.VISIBLE else View.GONE
        newsBinding.shimmerLayout.visibility = if (status) View.GONE else View.VISIBLE
        if (status) {
            newsBinding.shimmerLayout.stopShimmer()
        } else {
            newsBinding.shimmerLayout.startShimmer()
        }
    }
}