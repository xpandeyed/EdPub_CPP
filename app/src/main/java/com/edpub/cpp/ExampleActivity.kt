package com.edpub.cpp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast

class ExampleActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_example)
        val ivShare = findViewById<ImageView>(R.id.ivShare)
        ivShare.setOnClickListener {
            Toast.makeText(this, "Share", Toast.LENGTH_SHORT).show()
        }
        val ivFavourites = findViewById<ImageView>(R.id.ivFavourite)
        ivFavourites.setOnClickListener {
            Toast.makeText(this, "Favourites", Toast.LENGTH_SHORT).show()
        }
        val bToNextExample = findViewById<Button>(R.id.bToNextExample)
        bToNextExample.setOnClickListener {
            Toast.makeText(this, "Next Example", Toast.LENGTH_SHORT).show()
        }
    }
}