package com.example.bookhaven.ui.books

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.bookhaven.R
import com.example.bookhaven.data.network.Book
import com.example.bookhaven.databinding.ItemBookBinding
import com.example.bookhaven.ui.activities.BookDetailActivity

class BookAdapter : ListAdapter<Book, BookAdapter.BookViewHolder>(BookDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val binding = ItemBookBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BookViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val book = getItem(position)
        holder.bind(book)
    }

    class BookViewHolder(private val binding: ItemBookBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(book: Book) {
            Glide.with(binding.bookImage.context)
                .load(book.volumeInfo.imageLinks?.thumbnail)
                .placeholder(R.drawable.ic_loading_img)
                .error(R.drawable.ic_broken_image)
                .into(binding.bookImage)


            binding.bookTitle.text = book.volumeInfo.title

            binding.root.setOnClickListener {
                val context = binding.root.context
                val intent = Intent(context, BookDetailActivity::class.java)
                intent.putExtra("BOOK_TITLE", book.volumeInfo.title)
                intent.putExtra("BOOK_AUTHOR", book.volumeInfo.authors?.joinToString() ?: "Unknown")
                intent.putExtra("BOOK_DESCRIPTION", book.volumeInfo.description ?: "No description available.")
                intent.putExtra("BOOK_IMAGE_URL", book.volumeInfo.imageLinks?.thumbnail)
                context.startActivity(intent)
            }
        }
    }
}
