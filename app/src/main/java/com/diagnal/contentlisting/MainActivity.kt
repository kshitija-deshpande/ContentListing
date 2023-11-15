package com.diagnal.contentlisting

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.NestedScrollView
import androidx.core.widget.addTextChangedListener
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableField
import androidx.recyclerview.widget.GridLayoutManager
import com.diagnal.contentlisting.adapter.MoviesAdapter
import com.diagnal.contentlisting.data.Content
import com.diagnal.contentlisting.databinding.ActivityMainBinding
import com.diagnal.contentlisting.utils.GridSpacingItemDecoration
import com.google.android.material.snackbar.Snackbar

/**
 * Main activity class to show the list of movies
 */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    var backButtonClicked = false
    private var movieDataArrayList = ObservableArrayList<Content>()
    private var searchedText = ObservableField<String>("")
    private var list = ArrayList<Content>()
    private lateinit var moviesAdapter: MoviesAdapter
    private var currentPage = 1
    private var maxPages = 3
    private var isLoading = false
    private val portraitSpanCount = 3
    private val landscapeSpanCount = 7
    private val searchSnackbar: Snackbar by lazy {
        Snackbar.make(binding.root, getString(R.string.search_text_length_error), Snackbar.LENGTH_SHORT)
    }
    private val contentListingViewModel by lazy {
        ContentListingViewModel(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.i(TAG, "onCreate: View binding initiated.")


        // Handle UI click, scroll listeners & search events
        handleSearchOpenButton()
        handleBackButton()
        handleSearchCloseButton()
        handleSearchTextChange()
        fetchMoreOnScroll()
        Log.i(TAG, "onCreate: UI click, scroll listener for pagination & search events handled.")

        moviesAdapter = MoviesAdapter(movieDataArrayList, searchedText, this)
        binding.moviesRecyclerView.adapter = moviesAdapter
        Log.i(TAG, "onCreate: Created moviesAdapter with observable moviesDataArrayList and observable field as search text.")

        fetchMovies(currentPage)

        showEmptyScreenIfNeeded()

        // Add decorator for grid items
        val spanCount = 3 // 3 columns
        val spacing = 30 // 30px
        val includeEdge = true
        binding.moviesRecyclerView.addItemDecoration(GridSpacingItemDecoration(spanCount, spacing, includeEdge))
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            binding.moviesRecyclerView.layoutManager = GridLayoutManager(this, landscapeSpanCount)
        } else {
            binding.moviesRecyclerView.layoutManager = GridLayoutManager(this, portraitSpanCount)
        }
    }

    override fun onBackPressed() {
        backButtonPressed()
    }

    /**
     * Fetch movies from view model when the scroll is reached the end
     */
    private fun fetchMoreOnScroll() {
        // Listen for scroll event to fetch more data
        binding.nestedScrollView.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->

            if (v.getChildAt(v.childCount - 1) != null) {
                if (!isLoading && (scrollY >= (v.getChildAt(v.childCount - 1).measuredHeight - v.measuredHeight)) && scrollY > oldScrollY) {
                    // Scrolled to bottom
                    if (currentPage < maxPages) {
                        currentPage += 1
                        isLoading = true
                        Log.i(TAG, "fetchMoreOnScroll: End of the scroll detected, fetch next page movies items.")
                        fetchMovies(currentPage)
                        isLoading = false
                    }
                }
            }
        })
    }

    /**
     * Handle search click, request focus on search field and show keyboard
     */
    private fun handleSearchOpenButton() {
        binding.toolbar.search.setOnClickListener {
            // Request focus and show keyboard
            binding.toolbar.searchText.requestFocus()
            this.currentFocus?.let { view ->
                val imm = getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager
                imm?.showSoftInput(view, 0)
            }
            binding.toolbar.searchBar.visibility = View.VISIBLE
            binding.toolbar.titleBar.visibility = View.GONE
            Log.i(TAG, "handleSearchOpenButton: search icon clicked, requested focus and soft keyboard is shown for entering search text.")
        }
    }

    /**
     * Handle title bar back button click
     */
    private fun handleBackButton() {
        binding.toolbar.back.setOnClickListener {
            backButtonPressed()
            Log.i(TAG, "handleBackButton: Title bar back button is clicked")
        }
    }

    /**
     * Handles back button press
     */
    private fun backButtonPressed() {
        if (backButtonClicked) {
            finish()
        } else {
            Toast.makeText(this, getString(R.string.back_button_click_text), Toast.LENGTH_SHORT).show()
            backButtonClicked = true
        }
    }

    /**
     * Handles search close button click, hides keyboard and clears search text
     */
    private fun handleSearchCloseButton() {
        binding.toolbar.closeSearch.setOnClickListener {
            // Hide keyboard
            this.currentFocus?.let { view ->
                val imm = getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager
                imm?.hideSoftInputFromWindow(view.windowToken, 0)
            }
            // Clear Search text
            binding.toolbar.searchText.text.clear()
            binding.toolbar.searchBar.visibility = View.GONE
            binding.toolbar.titleBar.visibility = View.VISIBLE
            Log.i(TAG, "handleSearchCloseButton: Search bar close button is clicked, keyboard is hidden and search text is cleared.")
        }
    }

    /**
     * Handles search text change, filters the list based on search text
     */
    @SuppressLint("NotifyDataSetChanged")
    private fun handleSearchTextChange() {
        binding.toolbar.searchText.addTextChangedListener { text ->
            // If the focus is not on search text, do not filter
            if(!binding.toolbar.searchText.hasFocus()) {
                Log.i(TAG, "handleSearchTextChange: Search edit text is not in focus, do not filter the list.")
                return@addTextChangedListener
            }

            if (text.isNullOrEmpty()) {
                // If the search text is empty, show all the movies
                movieDataArrayList.clear()
                movieDataArrayList.addAll(list)
                searchedText.set("")
                searchSnackbar.dismiss()
                Log.i(TAG, "handleSearchTextChange: search text is empty or null, show all the movies in the list.")
            } else if (text.toString().length < 3) {
                // If the search text is less than 3 characters, show snackbar to have at least 3 chars
                movieDataArrayList.clear()
                movieDataArrayList.addAll(list)
                searchedText.set("")
                searchSnackbar.show()
                Log.i(TAG, "handleSearchTextChange: search text is less than 3 char, show a snackbar to have at least 3 chars.")
            } else {
                // If the search text is more than 3 characters, filter the list and notify the adapter
                movieDataArrayList.clear()
                val tempList = ArrayList<Content>()
                for (movie in list) {
                    if (movie.name?.startsWith(text.toString(), true) == true) {
                        tempList.add(movie)
                    }
                }
                movieDataArrayList.addAll(tempList)
                searchedText.set(text.toString())
                searchSnackbar.dismiss()
                Log.i(TAG, "handleSearchTextChange: current movies list name is filtered with matching search text.")
            }
            moviesAdapter.notifyDataSetChanged()
            showEmptyScreenIfNeeded()
        }
    }

    /**
     * Fetch movies from view model and notify adapter
     */
    @SuppressLint("NotifyDataSetChanged")
    private fun fetchMovies(pageNumber: Int) {
        val response = contentListingViewModel.fetchContentList(pageNumber)

        Log.i(TAG, "fetchMovies: movies are fetched from view model for pageNumber $pageNumber.")

        response?.let { list.addAll(it) }
        movieDataArrayList.clear()
        movieDataArrayList.addAll(list)

        moviesAdapter.notifyDataSetChanged()
        Log.i(TAG, "fetchMovies: updated moves grid view.")
    }

    /**
     * Show empty screen if no movies are available
     */
    private fun showEmptyScreenIfNeeded() {
        Log.i(TAG, "showEmptyScreenIfNeeded: movies list is empty: ${movieDataArrayList.isEmpty()}.")
        if (movieDataArrayList.isEmpty()) {
            binding.noItemsFound.visibility = View.VISIBLE
            binding.moviesRecyclerView.visibility = View.GONE
        } else {
            binding.noItemsFound.visibility = View.GONE
            binding.moviesRecyclerView.visibility = View.VISIBLE
        }
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}