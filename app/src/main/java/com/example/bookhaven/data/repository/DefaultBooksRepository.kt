package com.example.bookhaven.data.repository

import com.example.bookhaven.BuildConfig
import com.example.bookhaven.data.network.BooksApi
import com.example.bookhaven.data.network.Book
import com.example.bookhaven.data.network.BooksApiService

class DefaultBooksRepository(
    private val booksApi: BooksApi = BooksApiService.create()
) : BooksRepository {

    private val apiKey = BuildConfig.API_KEY

    override suspend fun getBooks(query: String): List<Book>? {
        return try {
            val response = booksApi.searchBooks(query, apiKey)
            if (response.isSuccessful) {
                response.body()?.books
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    override suspend fun getBooksImages(query: String): List<String>? {
        return try {
            val response = booksApi.searchBooks(query, apiKey)
            if (response.isSuccessful) {
                response.body()?.books?.mapNotNull { book -> book.volumeInfo.imageLinks?.thumbnail }
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
