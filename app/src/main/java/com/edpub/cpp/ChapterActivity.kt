package com.edpub.cpp


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast

import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat

import com.google.firebase.auth.ktx.auth

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
        /* when chapter is added to favourites the chapter key is added twice to the favouriteChapterList.
        * that is why we are removing the key twice
        * reason of chapter being added twice is not known.
        * Don't delete this comment until problem is not solved.*/
        ivFavourites.setOnClickListener {
            if(ObjectsCollection.favouriteChaptersList.indexOf(key)!=-1){
                Toast.makeText(this, "${ObjectsCollection.favouriteChaptersList}", Toast.LENGTH_SHORT).show()
                ObjectsCollection.favouriteChaptersList.remove(key)
                ObjectsCollection.favouriteChaptersList.remove(key)
                val database = Firebase.database
                val myRef = database.getReference("USERS")
                myRef.child(Firebase.auth.currentUser!!.uid).child("FAV_CHAP").child(key!!).setValue(null)
                DrawableCompat.setTint(ivFavourites.drawable, ContextCompat.getColor(this, R.color.browser_actions_bg_grey))
            }
            else{
                Toast.makeText(this, "${ObjectsCollection.favouriteChaptersList}", Toast.LENGTH_SHORT).show()
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