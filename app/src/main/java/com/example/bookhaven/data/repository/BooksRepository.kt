package com.example.bookhaven.data.repository

import com.example.bookhaven.data.network.Book

interface BooksRepository {
    suspend fun getBooks(query: String): List<Book>?
    suspend fun getBooksImages(query: String): List<String>?
}
