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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


//Entry Points
//1. From Current Chapter
//2. From Chapters List
//3. From Favourite Chapters
class ChapterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chapter)

        val database = Firebase.database
        val myRef = database.getReference("USERS")

        lateinit var key : String

        var position = intent.getIntExtra("POSITION", ObjectsCollection.currentChapterPosition)
        when (intent.getStringExtra("INVOKER")) {
            "fromFav" -> {
                key = ObjectsCollection.favouriteChapters[position].KEY!!
                findViewById<TextView>(R.id.tvTitle).text = ObjectsCollection.favouriteChapters[position].TITLE
                findViewById<TextView>(R.id.tvChapterText).text = ObjectsCollection.favouriteChapters[position].TEXT
                findViewById<TextView>(R.id.tvCode).text = ObjectsCollection.favouriteChapters[position].CODE
            }
            "fromChapter" -> {
                key = ObjectsCollection.chaptersList[position].KEY!!

                findViewById<TextView>(R.id.tvTitle).text = ObjectsCollection.chaptersList[position].TITLE
                findViewById<TextView>(R.id.tvChapterText).text = ObjectsCollection.chaptersList[position].TEXT
                findViewById<TextView>(R.id.tvCode).text = ObjectsCollection.chaptersList[position].CODE
            }
            "fromCurrChap" -> {
                key = ObjectsCollection.chaptersList[position].KEY!!

                findViewById<TextView>(R.id.tvTitle).text = ObjectsCollection.chaptersList[position].TITLE
                findViewById<TextView>(R.id.tvChapterText).text = ObjectsCollection.chaptersList[position].TEXT
                findViewById<TextView>(R.id.tvCode).text = ObjectsCollection.chaptersList[position].CODE

            }
        }


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
                ObjectsCollection.areFavouriteChaptersCopied = false
                ObjectsCollection.favouriteChapterKeysList.remove(key)
                ObjectsCollection.favouriteChapterKeysList.remove(key)
                ObjectsCollection.copyFavChaptersFromChapters(this)

                myRef.child(Firebase.auth.currentUser!!.uid).child("FAV_CHAP").child(key).setValue(null)
                DrawableCompat.setTint(ivFavourites.drawable, ContextCompat.getColor(this, R.color.icon_inactive))
            }
            else{
                ObjectsCollection.areFavouriteChaptersCopied = false
                ObjectsCollection.favouriteChapterKeysList.add(key!!)
                ObjectsCollection.copyFavChaptersFromChapters(this)
                val database = Firebase.database
                val myRef = database.getReference("USERS")
                myRef.child(Firebase.auth.currentUser!!.uid).child("FAV_CHAP").child(key).setValue(key)
                DrawableCompat.setTint(ivFavourites.drawable, ContextCompat.getColor(this, R.color.purple_200))
            }
        }
        bToNextChapter.setOnClickListener {
                if(intent.getStringExtra("INVOKER")=="fromFav"){
                    if(position==ObjectsCollection.favouriteChapters.size-1){
                        Toast.makeText(this, "This is the last Chapter.", Toast.LENGTH_SHORT).show()
                    }
                    else {
                        position += 1
                        key = ObjectsCollection.favouriteChapters[position].KEY!!
                        if (ObjectsCollection.favouriteChapterKeysList.contains(key)) {
                            DrawableCompat.setTint(
                                ivFavourites.drawable,
                                ContextCompat.getColor(this, R.color.purple_200)
                            )
                        } else {
                            DrawableCompat.setTint(
                                ivFavourites.drawable,
                                ContextCompat.getColor(this, R.color.icon_inactive)
                            )
                        }
                        findViewById<TextView>(R.id.tvTitle).text =
                            ObjectsCollection.favouriteChapters[position].TITLE
                        findViewById<TextView>(R.id.tvChapterText).text =
                            ObjectsCollection.favouriteChapters[position].TEXT
                        findViewById<TextView>(R.id.tvCode).text =
                            ObjectsCollection.favouriteChapters[position].CODE
                    }
                }
                else{
                    if(position==ObjectsCollection.chaptersList.size-1){
                        Toast.makeText(this, "This is the last Chapter.", Toast.LENGTH_SHORT).show()
                    }
                    else {
                        position += 1
                        key = ObjectsCollection.chaptersList[position].KEY!!
                        if (ObjectsCollection.favouriteChapterKeysList.contains(key)) {
                            DrawableCompat.setTint(
                                ivFavourites.drawable,
                                ContextCompat.getColor(this, R.color.primaryColor)
                            )
                        } else {
                            DrawableCompat.setTint(
                                ivFavourites.drawable,
                                ContextCompat.getColor(this, R.color.icon_inactive)
                            )
                        }
                        findViewById<TextView>(R.id.tvTitle).text =
                            ObjectsCollection.chaptersList[position].TITLE
                        findViewById<TextView>(R.id.tvChapterText).text =
                            ObjectsCollection.chaptersList[position].TEXT
                        findViewById<TextView>(R.id.tvCode).text =
                            ObjectsCollection.chaptersList[position].CODE
                    }
                }
        }
    }
}