package com.edpub.cpp

import android.graphics.Color.red
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class ChapterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chapter)

        findViewById<TextView>(R.id.tvTitle).text = intent.getStringExtra("TITLE")
        findViewById<TextView>(R.id.tvChapterText).text = intent.getStringExtra("TEXT")
        findViewById<TextView>(R.id.tvCode).text = intent.getStringExtra("CODE")
        val key = intent.getStringExtra("KEY")

        val ivShare = findViewById<ImageView>(R.id.ivShare)
        val ivFavourites = findViewById<ImageView>(R.id.ivFavourite)
        val bToNextChapter = findViewById<Button>(R.id.bToNextChapter)

        ivShare.setOnClickListener {

        }

        if(ObjectsCollection.favouriteChaptersList.contains(key)){
            DrawableCompat.setTint(ivFavourites.drawable, ContextCompat.getColor(this, R.color.purple_200))
        }
        else{
            DrawableCompat.setTint(ivFavourites.drawable, ContextCompat.getColor(this, R.color.browser_actions_bg_grey))
        }

        ivFavourites.setOnClickListener {
            if(ObjectsCollection.favouriteChaptersList.contains(key)){
                ObjectsCollection.favouriteChaptersList.remove(key)
                val database = Firebase.database
                val myRef = database.getReference("USERS")
                myRef.child(Firebase.auth.currentUser!!.uid).child("FAV_CHAP").child(key!!).setValue(null)
                DrawableCompat.setTint(ivFavourites.drawable, ContextCompat.getColor(this, R.color.browser_actions_bg_grey))
            }
            else{
                ObjectsCollection.favouriteChaptersList.add(key!!)
                val database = Firebase.database
                val myRef = database.getReference("USERS")
                myRef.child(Firebase.auth.currentUser!!.uid).child("FAV_CHAP").child(key).setValue(key)
                DrawableCompat.setTint(ivFavourites.drawable, ContextCompat.getColor(this, R.color.purple_200))
            }
        }

        bToNextChapter.setOnClickListener {
            Toast.makeText(this, "Next Chapter", Toast.LENGTH_SHORT).show()
        }
    }
}