package com.edpub.cpp


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*

import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.text.HtmlCompat

import com.google.firebase.auth.ktx.auth

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.lang.Exception



class ChapterActivity : AppCompatActivity() {

    private var invoker = "fromChapter"

    private var key:String? = ObjectsCollection.currentChapterKey
    private var position = ObjectsCollection.currentChapterPosition
    private var currChapter = ObjectsCollection.chaptersList[position]


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chapter)


        val ivShare = findViewById<ImageView>(R.id.ivShare)
        val ivFavourites = findViewById<ImageView>(R.id.ivFavourite)
        val ivDone = findViewById<ImageView>(R.id.ivDone)
        val bToNextChapter = findViewById<Button>(R.id.bToNextChapter)


        val database = Firebase.database
        val myRef = database.getReference("USERS")


        key = ObjectsCollection.currentChapterKey
        position = intent.getIntExtra("POSITION", ObjectsCollection.currentChapterPosition)
        currChapter = ObjectsCollection.chaptersList[position]

        invoker = intent.getStringExtra("INVOKER")!!

        if (invoker == "fromFav") {
            ivDone.visibility = View.GONE

            try{
                key = ObjectsCollection.favouriteChapters[position].KEY

                currChapter = ObjectsCollection.favouriteChapters[position]

                val htmlCode = ObjectsCollection.favouriteChapters[position].CODE
                val spannedCode = HtmlCompat.fromHtml(htmlCode!!, HtmlCompat.FROM_HTML_MODE_COMPACT)

                val htmlText = ObjectsCollection.favouriteChapters[position].TEXT
                val spannedText = HtmlCompat.fromHtml(htmlText!!, HtmlCompat.FROM_HTML_MODE_COMPACT)

                val htmlExplanation = ObjectsCollection.favouriteChapters[position].EXPLANATION
                val spannedExplanation = HtmlCompat.fromHtml(htmlExplanation!!, HtmlCompat.FROM_HTML_MODE_COMPACT)

                findViewById<TextView>(R.id.tvTitle).text = ObjectsCollection.favouriteChapters[position].TITLE
                findViewById<TextView>(R.id.tvChapterText).text = spannedText
                findViewById<TextView>(R.id.tvCode).text = spannedCode
                findViewById<TextView>(R.id.tvExplanation).text = spannedExplanation
            } catch (exception : Exception){
                Log.i("FAV", "$exception")
                //this toast is never shown reason unknown
                Toast.makeText(this, "Favourite Chapter List Is Empty Now.", Toast.LENGTH_SHORT).show()
            }

        }
        else {
            key = ObjectsCollection.chaptersList[position].KEY!!

            currChapter = ObjectsCollection.chaptersList[position]

            ObjectsCollection.currentChapterPosition = position
            ObjectsCollection.currentChapterKey = key.toString()

            ObjectsCollection.currentChapter.clear()
            ObjectsCollection.adapterCurrentChapter.notifyItemRangeRemoved(0, 1)
            ObjectsCollection.currentChapter.add(currChapter)
            ObjectsCollection.adapterCurrentChapter.notifyItemInserted(0)
            Firebase.database.getReference("USERS").child(Firebase.auth.currentUser!!.uid).child("CURR_CHAP").setValue(key)

            val htmlCode = ObjectsCollection.chaptersList[position].CODE
            val spannedCode = HtmlCompat.fromHtml(htmlCode!!, HtmlCompat.FROM_HTML_MODE_COMPACT)

            val htmlText = ObjectsCollection.chaptersList[position].TEXT
            val spannedText = HtmlCompat.fromHtml(htmlText!!, HtmlCompat.FROM_HTML_MODE_COMPACT)

            val htmlExplanation = ObjectsCollection.chaptersList[position].EXPLANATION
            val spannedExplanation = HtmlCompat.fromHtml(htmlExplanation!!, HtmlCompat.FROM_HTML_MODE_COMPACT)

            findViewById<TextView>(R.id.tvTitle).text = ObjectsCollection.chaptersList[position].TITLE
            findViewById<TextView>(R.id.tvChapterText).text = spannedText
            findViewById<TextView>(R.id.tvCode).text = spannedCode
            findViewById<TextView>(R.id.tvExplanation).text = spannedExplanation
        }



        ivShare.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            intent.putExtra(Intent.EXTRA_TEXT, "Download EdPub")
            intent.type = "text/plain"
            startActivity(intent)
        }

        if(ObjectsCollection.favouriteChapterKeysList.indexOf(key)!=-1){
            DrawableCompat.setTint(ivFavourites.drawable, ContextCompat.getColor(this, R.color.pure_red))
        }
        else{
            DrawableCompat.setTint(ivFavourites.drawable, ContextCompat.getColor(this, R.color.icon_inactive))
        }

        setDoneIconTint(key.toString())


        ivFavourites.setOnClickListener {
            if(ObjectsCollection.favouriteChapterKeysList.indexOf(key)!=-1){
                val positionCurrChapter = ObjectsCollection.favouriteChapters.indexOf(currChapter)
                ObjectsCollection.favouriteChapters.remove(currChapter)
                ObjectsCollection.adapterFavouriteChapters.notifyItemRemoved(positionCurrChapter)
                ObjectsCollection.favouriteChapterKeysList.remove(key)

                myRef.child(Firebase.auth.currentUser!!.uid).child("FAV_CHAP").child(key!!).setValue(null)

                DrawableCompat.setTint(ivFavourites.drawable, ContextCompat.getColor(this, R.color.icon_inactive))
            }
            else{
                ObjectsCollection.favouriteChapterKeysList.add(key!!)
                ObjectsCollection.favouriteChapters.add(currChapter)
                ObjectsCollection.adapterFavouriteChapters.notifyItemInserted(ObjectsCollection.favouriteChapters.size-1)

                myRef.child(Firebase.auth.currentUser!!.uid).child("FAV_CHAP").child(key.toString()).setValue(key)

                DrawableCompat.setTint(ivFavourites.drawable, ContextCompat.getColor(this, R.color.pure_red))
            }
        }

        ivDone.setOnClickListener {
            if(ObjectsCollection.completedChaptersKeysList.indexOf(key)!=-1){
                ObjectsCollection.completedChaptersKeysList.remove(key)
                myRef.child(Firebase.auth.currentUser!!.uid).child("COMP_CHAP").child(key!!).setValue(null)
                DrawableCompat.setTint(ivDone.drawable, ContextCompat.getColor(this, R.color.icon_inactive))
            }
            else{
                ObjectsCollection.completedChaptersKeysList.add(key!!)
                myRef.child(Firebase.auth.currentUser!!.uid).child("COMP_CHAP").child(key.toString()).setValue(key)
                DrawableCompat.setTint(ivDone.drawable, ContextCompat.getColor(this, R.color.slight_neon_green))
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
                        currChapter = ObjectsCollection.favouriteChapters[position]

                        findViewById<ScrollView>(R.id.svContainer).scrollTo(0,0)

                        if (ObjectsCollection.favouriteChapterKeysList.contains(key)) {
                            DrawableCompat.setTint(
                                ivFavourites.drawable,
                                ContextCompat.getColor(this, R.color.pure_red)
                            )
                        } else {
                            DrawableCompat.setTint(
                                ivFavourites.drawable,
                                ContextCompat.getColor(this, R.color.icon_inactive)
                            )
                        }

                        val htmlCode = ObjectsCollection.favouriteChapters[position].CODE
                        val spannedCode = HtmlCompat.fromHtml(htmlCode!!, HtmlCompat.FROM_HTML_MODE_COMPACT)

                        val htmlText = ObjectsCollection.favouriteChapters[position].TEXT
                        val spannedText = HtmlCompat.fromHtml(htmlText!!, HtmlCompat.FROM_HTML_MODE_COMPACT)

                        val htmlExplanation = ObjectsCollection.favouriteChapters[position].EXPLANATION
                        val spannedExplanation = HtmlCompat.fromHtml(htmlExplanation!!, HtmlCompat.FROM_HTML_MODE_COMPACT)

                        findViewById<TextView>(R.id.tvTitle).text = ObjectsCollection.favouriteChapters[position].TITLE
                        findViewById<TextView>(R.id.tvChapterText).text = spannedText
                        findViewById<TextView>(R.id.tvCode).text = spannedCode
                        findViewById<TextView>(R.id.tvExplanation).text = spannedExplanation
                    }
                }
                else{
                    if(position>=ObjectsCollection.chaptersList.size-1){
                        Toast.makeText(this, "No more Chapters found.", Toast.LENGTH_SHORT).show()
                    }
                    else {
                        position += 1
                        key = ObjectsCollection.chaptersList[position].KEY!!
                        currChapter = ObjectsCollection.chaptersList[position]

                        //shift this to somewhere in a function
                        currChapterHandler()


                        findViewById<ScrollView>(R.id.svContainer).scrollTo(0,0)


                        if (ObjectsCollection.favouriteChapterKeysList.contains(key)) {
                            DrawableCompat.setTint(ivFavourites.drawable,ContextCompat.getColor(this, R.color.pure_red))
                        } else {
                            DrawableCompat.setTint(ivFavourites.drawable,ContextCompat.getColor(this, R.color.icon_inactive))
                        }

                        setDoneIconTint(key.toString())

                        val htmlCode = ObjectsCollection.chaptersList[position].CODE
                        val spannedCode = HtmlCompat.fromHtml(htmlCode!!, HtmlCompat.FROM_HTML_MODE_COMPACT)

                        val htmlText = ObjectsCollection.chaptersList[position].TEXT
                        val spannedText = HtmlCompat.fromHtml(htmlText!!, HtmlCompat.FROM_HTML_MODE_COMPACT)

                        val htmlExplanation = ObjectsCollection.chaptersList[position].EXPLANATION
                        val spannedExplanation = HtmlCompat.fromHtml(htmlExplanation!!, HtmlCompat.FROM_HTML_MODE_COMPACT)

                        findViewById<TextView>(R.id.tvTitle).text = ObjectsCollection.chaptersList[position].TITLE
                        findViewById<TextView>(R.id.tvChapterText).text = spannedText
                        findViewById<TextView>(R.id.tvCode).text = spannedCode
                        findViewById<TextView>(R.id.tvExplanation).text = spannedExplanation
                    }
                }
        }
    }



    private fun setText(currChapter:Chapter){
        findViewById<TextView>(R.id.tvTitle)
        findViewById<TextView>(R.id.tvChapterText)
        findViewById<TextView>(R.id.tvCode)
        findViewById<TextView>(R.id.tvExplanation)
    }


    private fun currChapterHandler(){
        ObjectsCollection.currentChapter.clear()
        ObjectsCollection.adapterCurrentChapter.notifyItemRangeRemoved(0, 1)
        ObjectsCollection.currentChapterKey = key.toString()
        ObjectsCollection.currentChapterPosition = position
        ObjectsCollection.currentChapter.add(currChapter)
        ObjectsCollection.adapterCurrentChapter.notifyItemInserted(0)
        Firebase.database.getReference("USERS").child(Firebase.auth.currentUser!!.uid).child("CURR_CHAP").setValue(key)

    }

    private fun setDoneIconTint(key: String){
        val ivDone = findViewById<ImageView>(R.id.ivDone)
        if(ObjectsCollection.completedChaptersKeysList.indexOf(key)!=-1){
            DrawableCompat.setTint(ivDone.drawable, ContextCompat.getColor(this, R.color.slight_neon_green))
        }
        else{
            DrawableCompat.setTint(ivDone.drawable, ContextCompat.getColor(this, R.color.icon_inactive))
        }
    }
}