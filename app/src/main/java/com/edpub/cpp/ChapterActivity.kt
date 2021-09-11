package com.edpub.cpp


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast

import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat

import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.lang.Exception


//Entry Points
//1. From Current Chapter
//2. From Chapters List
//3. From Favourite Chapters
class ChapterActivity : AppCompatActivity() {

    private var invoker = "fromChapter"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chapter)

        val database = Firebase.database
        val myRef = database.getReference("USERS")

        var key:String? = ObjectsCollection.currentChapterKey
        var position = intent.getIntExtra("POSITION", 0)
        invoker = intent.getStringExtra("INVOKER")!!
        when (invoker) {
            "fromFav" -> {
                try{
                    key = ObjectsCollection.favouriteChapters[position].KEY

                    findViewById<TextView>(R.id.tvTitle).text = ObjectsCollection.favouriteChapters[position].TITLE
                    findViewById<TextView>(R.id.tvChapterText).text = ObjectsCollection.favouriteChapters[position].TEXT
                    findViewById<TextView>(R.id.tvCode).text = ObjectsCollection.favouriteChapters[position].CODE
                }
                catch (exception : Exception){
                    Log.i("FAV", "$exception")
                    //this toast is never shown reason unknown
                    Toast.makeText(this, "Favourite Chapter List Is Empty Now.", Toast.LENGTH_SHORT).show()
                }

            }
            "fromChapter" -> {
                key = ObjectsCollection.chaptersList[position].KEY!!

                ObjectsCollection.currentChapterPosition = position
                ObjectsCollection.currentChapterKey = key



                Firebase.database.getReference("USERS").child(Firebase.auth.currentUser!!.uid).child("CURR_CHAP").setValue(key)

                findViewById<TextView>(R.id.tvTitle).text = ObjectsCollection.chaptersList[position].TITLE
                findViewById<TextView>(R.id.tvChapterText).text = ObjectsCollection.chaptersList[position].TEXT
                findViewById<TextView>(R.id.tvCode).text = ObjectsCollection.chaptersList[position].CODE
            }
            "fromCurrChap" -> {


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

        if(ObjectsCollection.favouriteChapterKeysList.indexOf(key)!=-1){
            DrawableCompat.setTint(ivFavourites.drawable, ContextCompat.getColor(this, R.color.primaryColor))
        }
        else{
            DrawableCompat.setTint(ivFavourites.drawable, ContextCompat.getColor(this, R.color.icon_inactive))
        }


        ivFavourites.setOnClickListener {
            if(ObjectsCollection.favouriteChapterKeysList.indexOf(key)!=-1){

                ObjectsCollection.favouriteChapters.removeAt(position)
                ObjectsCollection.favouriteChapterKeysList.remove(key)
                ObjectsCollection.adapterFavouriteChapters.notifyItemRemoved(position)

                myRef.child(Firebase.auth.currentUser!!.uid).child("FAV_CHAP").child(key!!).setValue(null)

                DrawableCompat.setTint(ivFavourites.drawable, ContextCompat.getColor(this, R.color.icon_inactive))
            }
            else{

                ObjectsCollection.favouriteChapterKeysList.add(key!!)
                ObjectsCollection.favouriteChapters.add(ObjectsCollection.chaptersList[position])
                ObjectsCollection.adapterFavouriteChapters.notifyItemInserted(ObjectsCollection.favouriteChapters.size-1)


                myRef.child(Firebase.auth.currentUser!!.uid).child("FAV_CHAP").child(key.toString()).setValue(key)

                DrawableCompat.setTint(ivFavourites.drawable, ContextCompat.getColor(this, R.color.primaryColor))
            }
        }
        bToNextChapter.setOnClickListener {
                if(invoker=="fromFav"){
                    if(position>=ObjectsCollection.favouriteChapters.size-1){
                        Toast.makeText(this, "No more Favourite Chapters found.", Toast.LENGTH_SHORT).show()
                    }
                    else {
                        position += 1
                        key = ObjectsCollection.favouriteChapters[position].KEY!!

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
                            ObjectsCollection.favouriteChapters[position].TITLE
                        findViewById<TextView>(R.id.tvChapterText).text =
                            ObjectsCollection.favouriteChapters[position].TEXT
                        findViewById<TextView>(R.id.tvCode).text =
                            ObjectsCollection.favouriteChapters[position].CODE
                    }
                }
                else{
                    if(position>=ObjectsCollection.chaptersList.size-1){
                        Toast.makeText(this, "No more Chapters found.", Toast.LENGTH_SHORT).show()
                    }
                    else {
                        position += 1
                        key = ObjectsCollection.chaptersList[position].KEY!!

                        ObjectsCollection.currentChapterPosition = position
                        ObjectsCollection.currentChapterKey = key.toString()

                        Firebase.database.getReference("USERS").child(Firebase.auth.currentUser!!.uid).child("CURR_CHAP").setValue(key)

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