package com.lesson.codewalledu.src.ui.views.core.explore.blogs

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.webkit.WebSettings
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.lesson.codewalledu.R
import com.lesson.codewalledu.databinding.FragmentReadingBlogBinding
import com.lesson.codewalledu.src.utils.helpers.handleOnBackPressed

class ReadingBlogFragment : Fragment(R.layout.fragment_reading_blog) {
    lateinit var binding: FragmentReadingBlogBinding
    val args: ReadingBlogFragmentArgs by navArgs()
    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentReadingBlogBinding.bind(view)


        val blogUrl =args.blogUrl


        binding.wvReadingBlog.apply {
            isLongClickable = false
            isClickable = false
            settings.cacheMode = WebSettings.LOAD_DEFAULT // Enable caching
            setLayerType(View.LAYER_TYPE_HARDWARE, null) // Enable hardware acceleration
            webViewClient = WebViewClient()
//            settings.javaScriptEnabled = false
            loadUrl(blogUrl)
        }

        handleOnBackPressed(R.id.action_readingBlogFragment_pop)


    }
}