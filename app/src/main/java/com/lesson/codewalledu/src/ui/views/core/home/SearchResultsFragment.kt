package com.lesson.codewalledu.src.ui.views.core.home

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.internal.ViewUtils.hideKeyboard
import com.google.android.material.tabs.TabLayout
import com.lesson.codewalledu.R
import com.lesson.codewalledu.databinding.FragmentSearchResultsBinding
import com.lesson.codewalledu.databinding.ItemCheatSheetImageBinding
import com.lesson.codewalledu.databinding.LayoutCardBlogsBinding
import com.lesson.codewalledu.databinding.LayoutCardBookCoverBinding
import com.lesson.codewalledu.databinding.LayoutCardCheatSheetsBinding
import com.lesson.codewalledu.databinding.LayoutCardCoursesBinding
import com.lesson.codewalledu.databinding.LayoutCardNewsBinding
import com.lesson.codewalledu.src.data.models.core.home.search.BlogDataSearch
import com.lesson.codewalledu.src.data.models.core.home.search.BookDataSearch
import com.lesson.codewalledu.src.data.models.core.home.search.CheatSheetDataSearch
import com.lesson.codewalledu.src.data.models.core.home.search.CourseDataSearch
import com.lesson.codewalledu.src.data.models.core.home.search.NewsDataSearch
import com.lesson.codewalledu.src.data.models.core.home.search.SearchData
import com.lesson.codewalledu.src.ui.views.core.explore.blogs.ReadingBlogFragmentDirections
import com.lesson.codewalledu.src.ui.views.core.explore.books.ViewBookFragmentDirections
import com.lesson.codewalledu.src.ui.views.core.explore.cheat_sheets.ViewCheatSheetFragmentDirections
import com.lesson.codewalledu.src.ui.views.core.explore.news.ReadingNewsFragmentDirections
import com.lesson.codewalledu.src.utils.Resource
import com.lesson.codewalledu.src.utils.adapters.BaseRecyclerViewAdapter
import com.lesson.codewalledu.src.utils.helpers.handleOnBackPressed
import com.lesson.codewalledu.src.utils.helpers.stringToDouble
import com.lesson.codewalledu.src.utils.helpers.visible
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlin.collections.listOf


@AndroidEntryPoint
class SearchResultsFragment : Fragment(R.layout.fragment_search_results) {

    private lateinit var binding: FragmentSearchResultsBinding
    private val args: SearchResultsFragmentArgs by navArgs()
    private val searchViewModel: SearchResultsViewModel by viewModels()

    // Private variable to hold the full search data response
    private var currentSearchData: SearchData? = null

    // Adapters initialized once in onViewCreated with empty lists
    private lateinit var recentSearchesAdapter: BaseRecyclerViewAdapter<String> // New adapter for recent searches
    private lateinit var coursesAdapter: BaseRecyclerViewAdapter<CourseDataSearch>
    private lateinit var blogsAdapter: BaseRecyclerViewAdapter<BlogDataSearch>
    private lateinit var newsAdapter: BaseRecyclerViewAdapter<NewsDataSearch>
    private lateinit var booksAdapter: BaseRecyclerViewAdapter<BookDataSearch>
    private lateinit var cheatSheetsAdapter: BaseRecyclerViewAdapter<CheatSheetDataSearch>


    private fun resetToolbarSearchVisibility() {
        binding.searchResultToolbar.mainSearchView.setQuery("", false)
        binding.searchResultToolbar.mainSearchView.clearFocus()
        binding.searchResultToolbar.mainSearchView.isIconified = true

        val layoutParams = binding.searchResultToolbar.mainSearchView.layoutParams
        layoutParams.width = LinearLayout.LayoutParams.WRAP_CONTENT
        binding.searchResultToolbar.mainSearchView.layoutParams = layoutParams
        binding.searchResultToolbar.mainSearchView.requestLayout()

        println("resetToolbarSearchVisibility")
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSearchResultsBinding.bind(view)

        val searchQuery = args.searchQuery

        binding.searchResultToolbar.toolbarBackIcon.setOnClickListener {

                findNavController().navigate(R.id.action_searchResultsFragment_pop)

        }

        handleOnBackPressed(R.id.action_searchResultsFragment_pop)


        val resultsSearchView: SearchView = binding.searchResultToolbar.mainSearchView
        resultsSearchView.setQuery(searchQuery, false)
        resultsSearchView.clearFocus()


        resultsSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            @SuppressLint("RestrictedApi")
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    if (it.isNotBlank()) {
                        println("Query submitted: $it")
                        hideKeyboard(binding.root,false)
//                        resultsSearchView.clearFocus()
                        // Save the recent search query here if you have a UserPreferences or similar
                        // lifecycleScope.launch { userPreferences.addRecentSearch(it) }

                        lifecycleScope.launch {
                            searchViewModel.getSearchData(it)
                        }
                    }
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                println("Query text changed: $newText")
                if (newText.isNullOrBlank()) {
                    // Show Recent Searches when query is empty
                    binding.rvSearchResults.adapter = recentSearchesAdapter // Set recent searches adapter
                    // Load recent searches (dummy for now)
                    val dummyRecentSearches = listOf("Flutter ", "Kotlin ", "UI Design", "Android ", "Ai ")
                    recentSearchesAdapter.setItems(dummyRecentSearches)

                    binding.tvNoResults.visible(dummyRecentSearches.isEmpty()) // Show no results if recent is empty
                    binding.progressBar.visible(false)
                    // Select "Recent" tab (index 0)
                    binding.tabLayoutSearchCategories.selectTab(binding.tabLayoutSearchCategories.getTabAt(0))
                } else {
                    // Trigger a new search for live results
                    lifecycleScope.launch {
                        searchViewModel.getSearchData(newText.toString())
                    }
                }
                return false
            }
        })


        resultsSearchView.setOnQueryTextFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                (v.layoutParams as LinearLayout.LayoutParams).width = LinearLayout.LayoutParams.MATCH_PARENT
                v.requestLayout()
            } else {
                resetToolbarSearchVisibility()
            }
        }

        // --- Initialize Adapters Once in onViewCreated ---

        // New Recent Searches Adapter
        recentSearchesAdapter = BaseRecyclerViewAdapter(
            R.layout.layout_search_recent_item, // You'll need to create this layout
            false, mutableListOf()
        ) { _, data: String, view -> // Data is String for recent query
            val itemBinding = com.lesson.codewalledu.databinding.LayoutSearchRecentItemBinding.bind(view) // Create this binding
            itemBinding.apply {
                tvRecentQuery.text = data
                root.setOnClickListener {
                    // When recent search item is clicked, perform a search with that query
                    resultsSearchView.setQuery(data, true) // Set text and submit
                    resultsSearchView.clearFocus()
                }
            }
        }


        /**
         * Course Adapter
         */
        coursesAdapter = BaseRecyclerViewAdapter(
            R.layout.layout_card_courses, false, mutableListOf()
        ) { _, data, view ->
            val itemBinding = LayoutCardCoursesBinding.bind(view)
            itemBinding.apply {
                Picasso.get()
                    .load(data.imageUrl)
                    .placeholder(R.drawable.rectangle_placeholder)
                    .error(R.drawable.rectangle_placeholder)
                    .into(ivCourse)

                tvTitle.text = data.courseTitle

                if (data.isPublished == 1) {
                    val courseFeesInDouble = data.courseFees.stringToDouble()
                    when (courseFeesInDouble) {
                        in 100000.00..10000000.00 -> {
                            detailLayout.visible(true)
                            cvTab.visible(true)
                            tvDuration.text = "${data.length} hours"
                            tvChapterCount.text = "${data.chapterCount} Chapters"
                            tvPrice.text = "Fees: ${data.courseFees}/MMK"
                        }
                        0.0 -> {
                            detailLayout.visible(true)
                            cvTab.visible(false)
                            tvDuration.text = "${data.length} hours"
                            tvChapterCount.text = "${data.chapterCount} Lessons"
                            tvPrice.text = "Free"
                        }
                        else -> {
                            detailLayout.visible(false)
                            cvTab.visible(false)
                        }
                    }
                } else {
                    detailLayout.visible(false)
                    cvTab.visible(false)
                }
                cardRoot.setOnClickListener {
                    Toast.makeText(view.context, "Course: ${data.courseTitle} clicked!", Toast.LENGTH_SHORT).show()
                }
            }
        }


        /**
         * Blogs Adapter
         */
        blogsAdapter = BaseRecyclerViewAdapter(
            R.layout.layout_card_blogs, false, mutableListOf()
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
                rootCardBlog.setOnClickListener {
                    val action = ReadingBlogFragmentDirections.actionGlobalReadingBlogFragment(data.blogUrl)
                    findNavController().navigate(action)
                }
            }
        }

        /**
         * News Adapter
         */
        newsAdapter = BaseRecyclerViewAdapter(
            R.layout.layout_card_news, false,mutableListOf()
        )
        {
                position, data, view ->
            val itemBinding = LayoutCardNewsBinding.bind(view)
            itemBinding.apply {
                tvCreatedDate.text = data.publishedAt
                tvDescription.text = data.title
                tvContent.text = data.content
                Picasso.get()
                    .load(data.imageUrl)
                    .error(R.drawable.rectangle_placeholder)
                    .into(ivBlog)

            }
        }


        /**
         * Books adapter
         */
        booksAdapter = BaseRecyclerViewAdapter(
            R.layout.layout_card_book_cover, false, mutableListOf()
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


        /**
         * CheatSheetsAdapter
         */
        cheatSheetsAdapter = BaseRecyclerViewAdapter(
            R.layout.layout_card_cheat_sheets, false, mutableListOf()
        )
        {
                position, data, view ->
            val itemBinding = LayoutCardCheatSheetsBinding.bind(view)
            itemBinding.apply {
                Picasso.get()
                    .load(data.logoImageUrl)
                    .placeholder(R.drawable.blogs_figma)
                    .error(R.drawable.rectangle_placeholder)
                    .into(ivLogo)
                tvTechCategory.text = data.techCategoryName ?: "nothing"
                tvCreatedDate.text = data.createdAt
                tvShortDescription.text = data.description ?: "Fake CheatSheetData for description"


                val imageList = data.cheatSheetContents
                var imagesAdapter = BaseRecyclerViewAdapter(
                    R.layout.item_cheat_sheet_image, false,
                    imageList
                )
                {
                        imagePosition, imageData, imageView ->
                    val itemBinding = ItemCheatSheetImageBinding.bind(imageView)
                    itemBinding.apply {
                        ivDiscoverImage.setImageDrawable(null)
                        shimmerImageContainer.startShimmer()
                        Picasso.get()
                            .load(imageData.imageUrl)
                            .error(R.drawable.baseline_running_with_errors_24)
                            .into(ivDiscoverImage, object : Callback {
                                override fun onSuccess() {
                                    shimmerImageContainer.stopShimmer()
                                    shimmerImageContainer.setShimmer(null)
                                }
                                override fun onError(e: Exception?) {
                                    shimmerImageContainer.stopShimmer()
                                    shimmerImageContainer.setShimmer(null)
                                    ivDiscoverImage.setImageResource(R.drawable.baseline_running_with_errors_24)
                                }
                            })
                    }
                }
                rvHorizontalImages.setHasFixedSize(true)
                rvHorizontalImages.layoutManager = LinearLayoutManager(requireContext(),
                    LinearLayoutManager.HORIZONTAL,false)
                rvHorizontalImages.adapter = imagesAdapter
            }
        }


        // --- Category Tabs setup ---
        // Changed to include "Recent" as the first tab
        val categoryTitles = arrayListOf("Recent", "Courses", "Blogs", "News", "Books", "Cheatsheets")

        // Add tabs to TabLayout
        for (title in categoryTitles) {
            binding.tabLayoutSearchCategories.addTab(binding.tabLayoutSearchCategories.newTab().setText(title))
        }

        // Set up tab selected listener
        binding.tabLayoutSearchCategories.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.position?.let { position ->
                    // Update main RecyclerView based on selected tab
                    updateSearchResultsDisplay(position)
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                // Not needed for simple background/text color change with TabLayout's built-in indicators
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                // Handle re-selection if needed (e.g., scroll to top of list)
            }
        })


        // --- Observe Search Data Response ---
        searchViewModel.searchDataResponse.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Success -> {
                    currentSearchData = resource.value.data // Store the full data

                    binding.progressBar.visible(false)

                    // Determine which tab to initially select after a search
                    var tabToSelect = -1

                    if (!resultsSearchView.query.isNullOrBlank()) {
                        // If there's a current query, try to select the first category with results
                        if (currentSearchData!!.courses.isNotEmpty()) {
                            tabToSelect = 1 // Courses tab (index 1)
                        } else if (currentSearchData!!.blogs.isNotEmpty()) {
                            tabToSelect = 2 // Blogs tab (index 2)
                        } else if (currentSearchData!!.news.isNotEmpty()) {
                            tabToSelect = 3 // News tab (index 3)
                        } else if (currentSearchData!!.books.isNotEmpty()) {
                            tabToSelect = 4 // Books tab (index 4)
                        } else if (currentSearchData!!.cheatSheets.isNotEmpty()) {
                            tabToSelect = 5 // CheatSheets tab (index 5)
                        } else {
                            // If query has no results in any category, default to Recent tab if no results
                            tabToSelect = 0 // Recent tab
                            binding.tvNoResults.text = "No results found for '${resultsSearchView.query}'."
                            binding.tvNoResults.visible(true)
                            binding.rvSearchResults.visible(false)
                        }
                    } else {
                        // If query is empty initially (e.g., first time opening SearchResultsFragment without query)
                        tabToSelect = 0 // Default to Recent tab
                        // This branch will be hit if resultsSearchView.query is initially blank
                        // The onQueryTextChange listener will handle setting Recent data
                    }

                    if (tabToSelect != -1) {
                        binding.tabLayoutSearchCategories.selectTab(binding.tabLayoutSearchCategories.getTabAt(tabToSelect))
                    }

                    println("Search data received: ${currentSearchData}")
                }
                is Resource.Failure -> {
                    binding.progressBar.visible(false)
                    binding.rvSearchResults.visible(false)
                    binding.tvNoResults.text = "Failed to load results: ${resource.errorBody?.string() ?: "Unknown error"}. Please try again."
                    binding.tvNoResults.visible(true)
                    Toast.makeText(requireContext(), "Search failed!", Toast.LENGTH_SHORT).show()
                    println("Search error: ${resource.errorBody?.string() ?: "Unknown error"}")
                }
                is Resource.Loading -> {
                    binding.progressBar.visible(true)
                    binding.tvNoResults.visible(false)
                    binding.rvSearchResults.visible(false)
                    println("Search data loading...")
                }
            }
        }

        // Trigger initial search when fragment is created
        // If initial query is blank, onQueryTextChange will show recent
        // If initial query has value, it will trigger a search
        lifecycleScope.launch {
            searchViewModel.getSearchData(searchQuery)
        }
    }


    // Function to update the RecyclerView's content based on the selected tab
    private fun updateSearchResultsDisplay(tabPosition: Int) {
        // Hide "No results" by default when a tab is selected and we're about to show content
        binding.tvNoResults.visible(false)
        binding.rvSearchResults.visible(true)

        when (tabPosition) {
            0 -> { // Recent tab
                binding.rvSearchResults.adapter = recentSearchesAdapter
                // Load recent searches (dummy for now)
                val dummyRecentSearches = listOf("Flutter ", "Kotlin ", "UI Design", "Android", "Ai")
                recentSearchesAdapter.setItems(dummyRecentSearches)
                binding.tvNoResults.visible(dummyRecentSearches.isEmpty()) // Show if recent list is empty
            }
            1 -> { // Courses
                binding.rvSearchResults.adapter = coursesAdapter
                currentSearchData?.let {
                    coursesAdapter.setItems(it.courses)
                    binding.tvNoResults.visible(it.courses.isEmpty())
                } ?: binding.tvNoResults.visible(true) // Show no results if currentSearchData is null

            }
            2 -> { // Blogs
                binding.rvSearchResults.adapter = blogsAdapter
                currentSearchData?.let {
                    blogsAdapter.setItems(it.blogs)
                    binding.tvNoResults.visible(it.blogs.isEmpty())
                } ?: binding.tvNoResults.visible(true)
            }
            3 -> { // News
                binding.rvSearchResults.adapter = newsAdapter
                currentSearchData?.let {
                    newsAdapter.setItems(it.news)
                    binding.tvNoResults.visible(it.news.isEmpty())
                } ?: binding.tvNoResults.visible(true)
            }
            4 -> { // Books
                binding.rvSearchResults.adapter = booksAdapter
                currentSearchData?.let {
                    booksAdapter.setItems(it.books)
                    binding.tvNoResults.visible(it.books.isEmpty())
                } ?: binding.tvNoResults.visible(true)
            }
            5 -> { // Cheatsheets
                binding.rvSearchResults.adapter = cheatSheetsAdapter
                currentSearchData?.let {
                    cheatSheetsAdapter.setItems(it.cheatSheets)
                    binding.tvNoResults.visible(it.cheatSheets.isEmpty())
                } ?: binding.tvNoResults.visible(true)
            }
        }
    }
}