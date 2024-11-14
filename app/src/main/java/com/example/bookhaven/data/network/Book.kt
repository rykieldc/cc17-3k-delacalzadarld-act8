package com.example.bookhaven.data.network

import com.google.gson.annotations.SerializedName

data class Book(
    @SerializedName("id") val id: String,
    @SerializedName("volumeInfo") val volumeInfo: VolumeInfo
)

data class VolumeInfo(
    @SerializedName("title") val title: String,
    @SerializedName("imageLinks") val imageLinks: ImageLinks?,
    @SerializedName("authors") val authors: List<String>?,
    @SerializedName("description") val description: String?
)

data class ImageLinks(
    @SerializedName("thumbnail") val thumbnail: String
)
