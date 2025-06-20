package com.lesson.codewalledu.src.ui.views.core.explore.books

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.lesson.codewalledu.R
import com.lesson.codewalledu.databinding.FragmentViewBookBinding
import com.lesson.codewalledu.databinding.LayoutCardBookCoverBinding
import com.lesson.codewalledu.src.utils.adapters.BaseRecyclerViewAdapter
import com.lesson.codewalledu.src.utils.helpers.handleOnBackPressed
import com.squareup.picasso.Picasso
import kotlin.getValue


class ViewBookFragment : Fragment(R.layout.fragment_view_book) {
    lateinit var binding: FragmentViewBookBinding
    val args: ViewBookFragmentArgs by navArgs()
    @SuppressLint("RestrictedApi")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding= FragmentViewBookBinding.bind(view)

        val book = args.book
        val books = args.bookList.toList().shuffled()


        binding.apply {
            Picasso.get()
                .load(book.imageUrl)
                .placeholder(R.drawable.rectangle_placeholder)
                .error(R.drawable.rectangle_placeholder)
                .into(ivCover)
        }



        val layoutManager = StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL)
        var booksAdapter = BaseRecyclerViewAdapter(
            R.layout.layout_card_book_cover, false,
            books
        )
        {
                position, data, view ->
            val itemBinding = LayoutCardBookCoverBinding.bind(view)
            itemBinding.apply {
                Picasso.get()
                    .load(data.imageUrl)
                    .error(R.drawable.rectangle_placeholder)
                    .into(ivBook)
            }
        }
        binding.rvBooks.setHasFixedSize(true)
        binding.rvBooks.layoutManager = layoutManager
        binding.rvBooks.adapter = booksAdapter


        handleOnBackPressed(R.id.action_viewBookFragment_pop)
        println("this is "+R.id.action_readingBlogFragment_pop)

    }
}