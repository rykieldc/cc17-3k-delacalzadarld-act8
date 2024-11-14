package com.example.bookhaven.data.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object BooksApiService {
    private const val BASE_URL = "https://www.googleapis.com/books/v1/"

    fun create(): BooksApi {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(BooksApi::class.java)
    }
}
