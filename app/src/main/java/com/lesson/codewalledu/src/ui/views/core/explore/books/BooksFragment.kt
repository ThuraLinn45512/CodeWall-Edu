package com.lesson.codewalledu.src.ui.views.core.explore.books

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.lesson.codewalledu.R
import com.lesson.codewalledu.databinding.FragmentBooksBinding
import com.lesson.codewalledu.databinding.LayoutImageCardBinding
import com.lesson.codewalledu.src.utils.Resource
import com.lesson.codewalledu.src.utils.adapters.BaseRecyclerViewAdapter
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BooksFragment : Fragment(R.layout.fragment_books) {

    private val booksViewModel: BooksViewModel by viewModels()
    lateinit var booksBinding: FragmentBooksBinding
    private var blogsStatus = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        booksBinding = FragmentBooksBinding.bind(view)


        booksBinding.swipeRefreshLayout.setOnRefreshListener {
            lifecycleScope.launch {
                booksViewModel.getBooks()
            }
        }

        booksViewModel.booksDataResponse.observe(viewLifecycleOwner){
            when(it){
                is Resource.Success -> {
                    blogsStatus = true
                    booksBinding.swipeRefreshLayout.isRefreshing = false
                    updateVisibility()
                    val layoutManager = StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)
                    var booksAdapter = BaseRecyclerViewAdapter(
                        R.layout.layout_image_card, false,
                        it.value.data
                    )
                    {
                            position, data, view ->
                        val itemBinding = LayoutImageCardBinding.bind(view)
                        itemBinding.apply {
                            Picasso.get()
                                .load(data.imageUrl)
                                .error(R.drawable.baseline_search_24)
                                .into(ivBook)
                            rootBook.setOnClickListener{
//                                val action = HomeFragmentDirections.actionHomeFragmentToReadingBlogFragment(data)
//                                findNavController().navigate(action)
                            }
                        }
                    }
                    booksBinding.rvBooks.setHasFixedSize(true)
                    booksBinding.rvBooks.layoutManager = layoutManager
                    booksBinding.rvBooks.adapter = booksAdapter


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
        booksBinding.rvBooks.visibility = if (allStatus) View.VISIBLE else View.GONE
        booksBinding.shimmerLayout.visibility = if (allStatus) View.GONE else View.VISIBLE
        if (allStatus) {
            booksBinding.shimmerLayout.stopShimmer()
        } else {
            booksBinding.shimmerLayout.startShimmer()
        }
    }
}