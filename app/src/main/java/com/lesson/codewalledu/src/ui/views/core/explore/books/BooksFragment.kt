package com.lesson.codewalledu.src.ui.views.core.explore.books

import android.annotation.SuppressLint
import androidx.fragment.app.viewModels
import android.os.Bundle
import android.view.MotionEvent
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.lesson.codewalledu.R
import com.lesson.codewalledu.databinding.FragmentBooksBinding
import com.lesson.codewalledu.databinding.LayoutCardBookCoverBinding
import com.lesson.codewalledu.src.utils.Resource
import com.lesson.codewalledu.src.utils.adapters.BaseRecyclerViewAdapter
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BooksFragment : Fragment(R.layout.fragment_books) {

    private val booksViewModel: BooksViewModel by viewModels()
    lateinit var booksBinding: FragmentBooksBinding
    private var blogsStatus = true

    @SuppressLint("RestrictedApi", "ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        booksBinding = FragmentBooksBinding.bind(view)

        booksBinding.swipeRefreshLayout.setOnRefreshListener {
            lifecycleScope.launch {
                booksViewModel.getBooks()
            }
        }
        booksViewModel.booksDataResponse.observe(viewLifecycleOwner){
            response ->
            when(response){
                is Resource.Success -> {
                    blogsStatus = true
                    booksBinding.swipeRefreshLayout.isRefreshing = false
                    updateVisibility()
                    val layoutManager = StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)
                    var booksAdapter = BaseRecyclerViewAdapter(
                        R.layout.layout_card_book_cover, false,
                        response.value.data.reversed()
                    )
                    {
                        position, data, view ->

                        val itemBinding = LayoutCardBookCoverBinding.bind(view)
                        itemBinding.apply {
                            Picasso.get()
                                .load(data.imageUrl)
                                .error(R.drawable.rectangle_placeholder)
                                .into(ivBook)
                            rootBook.setOnClickListener{
                                val navController = findNavController()
                                val currentDest = navController.currentDestination
                                val list = response.value.data.filter { it.id != data.id }.reversed()
                                println("Current NavController Destination: ID=${currentDest?.id?.let { resources.getResourceEntryName(it) }}, Label=${currentDest?.label}, Class=${currentDest?.displayName}")

                                try {

                                    val mainNavController = requireActivity().supportFragmentManager
                                        .findFragmentById(R.id.fragment_container_view) // ID of NavHost in MainActivity
                                        ?.childFragmentManager?.primaryNavigationFragment // This should be MainFragmentContainer
                                        ?.childFragmentManager?.findFragmentById(R.id.bottom_nav_fragment_container_view)
                                        // ID of NavHost in MainFragmentContainer
                                        ?.findNavController()

                                    if (mainNavController?.currentDestination?.id != R.id.viewBookFragment) {
                                        val action = ViewBookFragmentDirections.actionGlobalViewBookFragment(data,list.toTypedArray())
                                        mainNavController?.navigate(action)
                                    }
                                }catch (e: Exception){
                                    println(e.toString()) // Also print to System.out as you were doing
                                }
                            }
                        }
                    }
                    booksBinding.rvBooks.setHasFixedSize(true)
                    booksBinding.rvBooks.layoutManager = layoutManager
                    booksBinding.rvBooks.adapter = booksAdapter


                }
                is Resource.Failure -> {
                    Toast.makeText(requireContext(),response.errorBody.toString(),Toast.LENGTH_SHORT).show()
                }
                is Resource.Loading -> {
                    blogsStatus = false
                    updateVisibility()
                }
            }
        }


        // Inside your Fragment where the RecyclerView is
        booksBinding.rvBooks.addOnScrollListener(object : RecyclerView.OnScrollListener() {
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
        booksBinding.rvBooks.setOnTouchListener { v, event ->
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
        booksBinding.rvBooks.visibility = if (allStatus) View.VISIBLE else View.GONE
        booksBinding.shimmerLayout.visibility = if (allStatus) View.GONE else View.VISIBLE
        if (allStatus) {
            booksBinding.shimmerLayout.stopShimmer()
        } else {
            booksBinding.shimmerLayout.startShimmer()
        }
    }
}