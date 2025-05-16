package com.lesson.codewalledu.src.ui.views.core.home.home_nested_screens.daily_dev

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.lesson.codewalledu.R
import com.lesson.codewalledu.databinding.FragmentOurBlogsBinding
import com.lesson.codewalledu.src.utils.APP_COLOR_BLUE
import com.lesson.codewalledu.src.utils.APP_COLOR_CYAN
import com.lesson.codewalledu.src.utils.helpers.applyGradient
import com.lesson.codewalledu.src.utils.helpers.handleOnBackPressed
import com.lesson.codewalledu.src.utils.helpers.visible

class BlogsFragment : Fragment(R.layout.fragment_our_blogs) {


    private val viewModel: BlogsViewModel by viewModels()

    lateinit var binding: FragmentOurBlogsBinding


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentOurBlogsBinding.bind(view)

        binding.toolbar.backIcon.visible(true)
        binding.toolbar.toolbarTitle.text = "Our Blogs"
        binding.toolbar.toolbarTitle.applyGradient(APP_COLOR_BLUE, APP_COLOR_CYAN)
        binding.toolbar.backIcon.setOnClickListener {
            findNavController().navigate(R.id.action_ourBlogsFragment_to_exploreFragment)
        }

        handleOnBackPressed(R.id.action_ourBlogsFragment_to_exploreFragment)


//        var ourBlogsAdapter = BaseRecyclerViewAdapter(R.layout.layout_cw_blogs_card_two, false, viewModel.getBlogsList())
//        { position, data, view ->
//            val itemBinding = LayoutCwBlogsCardTwoBinding.bind(view)
//            itemBinding.apply {
//                ivBlogImage.setImageResource(data.imageUrl)
//                tvTitleBlogs.text = data.contentTitle
//                tvReadingTime.text = data.readingTime
//                tvShortDescription.text = data.contentBody
//            }
//
//        }
//        binding.rvOurBlogs.adapter = ourBlogsAdapter
    }

}