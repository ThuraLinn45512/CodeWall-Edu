package com.lesson.codewalledu.src.ui.views.core.explore.news

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.lesson.codewalledu.R
import com.lesson.codewalledu.databinding.FragmentReadingNewsBinding
import com.lesson.codewalledu.src.utils.helpers.handleOnBackPressed
import com.squareup.picasso.Picasso


class ReadingNewsFragment : Fragment((R.layout.fragment_reading_news)) {

    lateinit var binding: FragmentReadingNewsBinding
    val readingNewsFragmentArgs: ReadingNewsFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentReadingNewsBinding.bind(view)

        val news = readingNewsFragmentArgs.news


        binding.apply {
            Picasso.get()
                .load(news.newsCategory.imageUrl)
                .fit()
                .placeholder(R.drawable.baseline_running_with_errors_24)
                .error(R.drawable.rectangle_placeholder)
                .into(ivLogo)
            tvTitleNews.text = news.newsCategory.name
            tvCreatedDate.text= news.publishedAt
            tvTagLine.text = news.newsCategory.description?: "Fake CheatSheetData for description"
            tvDescription.text = news.description
            tvContent.text = news.content
            Picasso.get()
                .load(news.imageUrl)
                .error(R.drawable.rectangle_placeholder)
                .into(ivBlog)
        }

        handleOnBackPressed(R.id.action_readingNewsFragment_pop)




    }
}