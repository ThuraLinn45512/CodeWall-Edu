package com.lesson.codewalledu.src.ui.views.core.explore.blogs

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.lesson.codewalledu.R
import com.lesson.codewalledu.databinding.FragmentBlogsBinding
import com.lesson.codewalledu.databinding.FragmentErrorTestBinding
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
    private var blogsStatus = true

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
                                .fit()
                                .error(R.drawable.baseline_search_24)
                                .into(ivBlogImage)
                            tvTitleBlogs.text = data.contentTitle
                            tvCreatedDate.text= data.createdAt
                            tvReadingTime.text = "${data.readingTime} min read"
                            tvShortDescription.text = data.contentBody
                            rootCardBlog.setOnClickListener{

                                try {
//                                    findNavController().navigate(R.id.action_blogsFragment_to_fragmentErrorTestFragment2)
                                }catch (e: Exception){
                                    println(e.toString())
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