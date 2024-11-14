package com.example.bookhaven.ui.activities

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils
import androidx.core.graphics.drawable.DrawableCompat
import androidx.palette.graphics.Palette
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.bookhaven.R
import com.example.bookhaven.databinding.InfoBookBinding
import jp.wasabeef.glide.transformations.BlurTransformation

class BookDetailActivity : AppCompatActivity() {

    private lateinit var binding: InfoBookBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = InfoBookBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bookTitle = intent.getStringExtra("BOOK_TITLE")
        val bookAuthor = intent.getStringExtra("BOOK_AUTHOR")
        val bookDescription = intent.getStringExtra("BOOK_DESCRIPTION")
        val bookImageUrl = intent.getStringExtra("BOOK_IMAGE_URL")

        binding.bookTitle.text = bookTitle
        binding.bookAuthor.text = bookAuthor
        binding.bookDescription.text = bookDescription

        Glide.with(this)
            .load(bookImageUrl)
            .into(binding.bookImage)

        val backArrow = findViewById<ImageView>(R.id.backArrow)

        backArrow.setOnClickListener {
            finish()
        }

        Glide.with(this)
            .asBitmap()
            .load(bookImageUrl)
            .transform(CenterCrop(), BlurTransformation(20, 3))
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    binding.bgImg.background = BitmapDrawable(resources, resource)

                    Palette.from(resource).generate { palette ->
                        val dominantColor = palette?.getDominantColor(
                            ContextCompat.getColor(this@BookDetailActivity, R.color.md_theme_onSecondaryFixed)
                        )

                        dominantColor?.let {
                            val contrastColor = if (ColorUtils.calculateLuminance(it) > 0.3) {
                                ContextCompat.getColor(this@BookDetailActivity, R.color.md_theme_onSecondaryFixed)
                            } else {
                                ContextCompat.getColor(this@BookDetailActivity, R.color.md_theme_secondaryFixed)
                            }
                            DrawableCompat.setTint(backArrow.drawable, contrastColor)
                        }
                    }
                }

                override fun onLoadCleared(placeholder: Drawable?) { }
            })
    }
}