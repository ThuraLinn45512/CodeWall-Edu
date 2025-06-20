package com.lesson.codewalledu.src.ui.views.core.home.daily_dev

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.lesson.codewalledu.R
import com.lesson.codewalledu.databinding.FragmentShowAllBlogsBinding
import com.lesson.codewalledu.databinding.LayoutCardBlogsBinding
import com.lesson.codewalledu.src.ui.views.core.explore.blogs.BlogsViewModel
import com.lesson.codewalledu.src.utils.APP_COLOR_BLUE
import com.lesson.codewalledu.src.utils.APP_COLOR_CYAN
import com.lesson.codewalledu.src.utils.Resource
import com.lesson.codewalledu.src.utils.adapters.BaseRecyclerViewAdapter
import com.lesson.codewalledu.src.utils.helpers.applyGradient
import com.lesson.codewalledu.src.utils.helpers.handleOnBackPressed
import com.lesson.codewalledu.src.utils.helpers.visible
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ShowAllBlogsFragment : Fragment(R.layout.fragment_show_all_blogs) {


    private val blogsViewModel: BlogsViewModel by viewModels()

    lateinit var showAllBlogsBinding: FragmentShowAllBlogsBinding
    private var blogsStatus = true


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showAllBlogsBinding = FragmentShowAllBlogsBinding.bind(view)

        showAllBlogsBinding.toolbar.backIcon.visible(true)
        showAllBlogsBinding.toolbar.toolbarTitle.text = "Our Blogs"
        showAllBlogsBinding.toolbar.toolbarTitle.applyGradient(APP_COLOR_BLUE, APP_COLOR_CYAN)
        showAllBlogsBinding.toolbar.backIcon.setOnClickListener {
            findNavController().navigate(R.id.action_showAllBlogsFragment_pop)
        }

        handleOnBackPressed(R.id.action_showAllBlogsFragment_pop)

        blogsViewModel.blogsDataResponse.observe(viewLifecycleOwner){
            when(it){
                is Resource.Success -> {
                    blogsStatus = true
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
                            root.setOnClickListener{
                               var action = ShowAllBlogsFragmentDirections.actionShowAllBlogsFragmentToReadingBlogFragment(data.blogUrl)
                                findNavController().navigate(action)
                            }
                        }
                    }
                    showAllBlogsBinding.rvBlogs.setHasFixedSize(true)
                    showAllBlogsBinding.rvBlogs.layoutManager = layoutManager
                    showAllBlogsBinding.rvBlogs.adapter = ourBlogsAdapter

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
        showAllBlogsBinding.rvBlogs.visibility = if (allStatus) View.VISIBLE else View.GONE
        showAllBlogsBinding.shimmerLayout.visibility = if (allStatus) View.GONE else View.VISIBLE
        if (allStatus) {
            showAllBlogsBinding.shimmerLayout.stopShimmer()
        } else {
            showAllBlogsBinding.shimmerLayout.startShimmer()
        }
    }

}