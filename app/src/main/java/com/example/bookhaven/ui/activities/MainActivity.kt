package com.example.bookhaven.ui.activities

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.SearchView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bookhaven.R
import com.example.bookhaven.ui.books.BookAdapter
import com.example.bookhaven.ui.books.BooksViewModel
import com.example.bookhaven.ui.books.BooksUiState
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val booksViewModel: BooksViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fun calculateSpanCount(itemWidth: Int): Int {
            val displayMetrics = resources.displayMetrics
            return (displayMetrics.widthPixels / (itemWidth * displayMetrics.density).toInt()).coerceAtLeast(1)
        }

        // RecyclerView and Adapter
        val recyclerView: RecyclerView = findViewById(R.id.rvBooks)
        recyclerView.layoutManager = GridLayoutManager(this, calculateSpanCount(150))
        val bookAdapter = BookAdapter()
        recyclerView.adapter = bookAdapter

        // Search functionality
        val searchBar: SearchView = findViewById(R.id.search_bar)
        searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query.isNullOrBlank()) {
                    booksViewModel.getBooks("Harry Potter")
                } else {
                    booksViewModel.getBooks(query)
                }
                return true
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrBlank()) {
                    booksViewModel.getBooks("Harry Potter")
                }
                return false
            }
        })

        val reloadButton: ImageView = findViewById(R.id.reloadButton)
        reloadButton.setOnClickListener {
            val query = searchBar.query.toString()
            if (query.isNotBlank()) {
                booksViewModel.getBooks(query)
            } else {
                booksViewModel.getBooks("Harry Potter")
            }
        }

        // Books State
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                booksViewModel.booksUiState.collect { uiState ->
                    val progressBar = findViewById<ProgressBar>(R.id.progress_bar)
                    val errorTextView = findViewById<TextView>(R.id.error_text_view)
                    val errorIcon = findViewById<ImageView>(R.id.error_icon)

                    when (uiState) {
                        is BooksUiState.Loading -> {
                            progressBar.visibility = View.VISIBLE
                            errorTextView.visibility = View.GONE
                        }
                        is BooksUiState.Success -> {
                            progressBar.visibility = View.GONE
                            errorTextView.visibility = View.GONE
                            reloadButton.visibility = View.GONE
                            bookAdapter.submitList(uiState.books)
                        }
                        is BooksUiState.Error -> {
                            progressBar.visibility = View.GONE
                            errorTextView.visibility = View.VISIBLE
                            errorIcon.visibility = View.GONE
                            reloadButton.visibility = View.VISIBLE
                            bookAdapter.submitList(emptyList())
                        }
                        is BooksUiState.NetworkError -> {
                            progressBar.visibility = View.GONE
                            errorTextView.visibility = View.GONE
                            errorIcon.visibility = View.VISIBLE
                            reloadButton.visibility = View.VISIBLE
                        }
                    }
                }
            }
        }

        // Initial fetch
        booksViewModel.getBooks("Harry Potter")
    }
}
