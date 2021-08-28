package com.edpub.cpp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar

class ChapterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chapter)
        val ivShare = findViewById<ImageView>(R.id.ivShare)
        ivShare.setOnClickListener {
            Toast.makeText(this, "Share", Toast.LENGTH_SHORT).show()
        }
        val ivFavourites = findViewById<ImageView>(R.id.ivFavourite)
        ivFavourites.setOnClickListener {
            Toast.makeText(this, "Favourites", Toast.LENGTH_SHORT).show()
        }
        val bToNextChapter = findViewById<Button>(R.id.bToNextChapter)
        bToNextChapter.setOnClickListener {
            Toast.makeText(this, "Next Chapter", Toast.LENGTH_SHORT).show()
        }
    }
}