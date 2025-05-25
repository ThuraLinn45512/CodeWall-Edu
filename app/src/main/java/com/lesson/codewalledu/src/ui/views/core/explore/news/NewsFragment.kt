package com.lesson.codewalledu.src.ui.views.core.explore.news

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.lesson.codewalledu.R
import com.lesson.codewalledu.databinding.FragmentNewsBinding
import com.lesson.codewalledu.databinding.LayoutCardNewsBinding
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        newsBinding = FragmentNewsBinding.bind(view)

//        newsBinding.swipeRefreshLayout.setOnRefreshListener {
//            lifecycleScope.launch {
//                newsViewModel.getNews()
//            }
//        }

        newsViewModel.newsBlogsDataResponse.observe(viewLifecycleOwner){
            when(it){
                is Resource.Success -> {
                    blogsStatus = true
//                    newsBinding.swipeRefreshLayout.isRefreshing = false
                    updateVisibility()
                    val layoutManager = LinearLayoutManager(requireContext())
                    var ourNewsAdapter = BaseRecyclerViewAdapter(
                        R.layout.layout_card_news, false,
                        it.value.data.reversed()
                    )
                    {
                            position, data, view ->
                        val itemBinding = LayoutCardNewsBinding.bind(view)
                        itemBinding.apply {
                            Picasso.get()
                                .load(data.newsCategory.imageUrl)
                                .fit()
                                .placeholder(R.drawable.baseline_running_with_errors_24)
                                .error(R.drawable.baseline_search_24)
                                .into(ivLogo)
                            tvTitleNews.text = data.newsCategory.name
                            tvCreatedDate.text= data.publishedAt
                            tvTagLine.text = data.newsCategory.description?: "Fake Data for description"
                            tvShortDescription.text = data.description
                            Picasso.get()
                                .load(data.imageUrl)
                                .fit()
                                .error(R.drawable.baseline_search_24)
                                .into(ivBlog)
                            newsRoot.setOnClickListener{
//                                val action = HomeFragmentDirections.actionHomeFragmentToReadingBlogFragment(data)
//                                findNavController().navigate(action)
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