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

        var position = intent.getIntExtra("POSITION", 0)
        var key= ObjectsCollection.chaptersList[position].KEY
        findViewById<TextView>(R.id.tvTitle).text = ObjectsCollection.chaptersList[position].TITLE
        findViewById<TextView>(R.id.tvChapterText).text = ObjectsCollection.chaptersList[position].TEXT
        findViewById<TextView>(R.id.tvCode).text = ObjectsCollection.chaptersList[position].CODE

        val ivShare = findViewById<ImageView>(R.id.ivShare)
        val ivFavourites = findViewById<ImageView>(R.id.ivFavourite)
        val bToNextChapter = findViewById<Button>(R.id.bToNextChapter)



        ivShare.setOnClickListener {

        }

        if(ObjectsCollection.favouriteChapterKeysList.contains(key)){
            DrawableCompat.setTint(ivFavourites.drawable, ContextCompat.getColor(this, R.color.purple_200))
        }
        else{
            DrawableCompat.setTint(ivFavourites.drawable, ContextCompat.getColor(this, R.color.icon_inactive))
        }
        /* when chapter is added to favourites the chapter key is added twice to the favouriteChapterList.
        * that is why we are removing the key twice
        * reason of chapter being added twice is not known.
        * Don't delete this comment until problem is not solved.*/
        ivFavourites.setOnClickListener {
            if(ObjectsCollection.favouriteChapterKeysList.indexOf(key)!=-1){
                ObjectsCollection.favouriteChapterKeysList.remove(key)
                ObjectsCollection.favouriteChapterKeysList.remove(key)
                val database = Firebase.database
                val myRef = database.getReference("USERS")
                myRef.child(Firebase.auth.currentUser!!.uid).child("FAV_CHAP").child(key!!).setValue(null)
                DrawableCompat.setTint(ivFavourites.drawable, ContextCompat.getColor(this, R.color.icon_inactive))
            }
            else{
                ObjectsCollection.favouriteChapterKeysList.add(key!!)
                val database = Firebase.database
                val myRef = database.getReference("USERS")
                myRef.child(Firebase.auth.currentUser!!.uid).child("FAV_CHAP").child(key!!).setValue(key)
                DrawableCompat.setTint(ivFavourites.drawable, ContextCompat.getColor(this, R.color.purple_200))
            }
        }
        bToNextChapter.setOnClickListener {
            if(position==ObjectsCollection.chaptersList.size-1){
                Toast.makeText(this, "This is the last Chapter.", Toast.LENGTH_SHORT).show()
            }
            else{
                position += 1
                key = ObjectsCollection.chaptersList[position].KEY
                if(ObjectsCollection.favouriteChapterKeysList.contains(key)){
                    DrawableCompat.setTint(ivFavourites.drawable, ContextCompat.getColor(this, R.color.purple_200))
                }
                else{
                    DrawableCompat.setTint(ivFavourites.drawable, ContextCompat.getColor(this, R.color.icon_inactive))
                }
                findViewById<TextView>(R.id.tvTitle).text = ObjectsCollection.chaptersList[position].TITLE
                findViewById<TextView>(R.id.tvChapterText).text = ObjectsCollection.chaptersList[position].TEXT
                findViewById<TextView>(R.id.tvCode).text = ObjectsCollection.chaptersList[position].CODE
            }
        }
    }
}