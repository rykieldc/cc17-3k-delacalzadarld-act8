package com.example.bookhaven.ui.books

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookhaven.data.network.Book
import com.example.bookhaven.data.repository.DefaultBooksRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.IOException

sealed interface BooksUiState {
    data object Loading : BooksUiState
    data class Success(val books: List<Book>) : BooksUiState
    data object Error : BooksUiState
    data object NetworkError : BooksUiState
}

class BooksViewModel(application: Application) : AndroidViewModel(application) {

    private val booksRepository = DefaultBooksRepository()
    private val _booksUiState: MutableStateFlow<BooksUiState> = MutableStateFlow(BooksUiState.Loading)
    val booksUiState: StateFlow<BooksUiState> = _booksUiState

    fun getBooks(query: String) {
        viewModelScope.launch {
            _booksUiState.value = BooksUiState.Loading
            try {
                val books = booksRepository.getBooks(query)
                if (books.isNullOrEmpty()) {
                    _booksUiState.value = BooksUiState.Error
                } else {
                    _booksUiState.value = BooksUiState.Success(books)
                }
            } catch (e: IOException) {
                _booksUiState.value = BooksUiState.NetworkError
            }
        }
    }
}
