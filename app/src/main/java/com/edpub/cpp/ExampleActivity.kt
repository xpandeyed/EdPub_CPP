package com.edpub.cpp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.lang.Exception
import androidx.core.text.HtmlCompat

import android.text.Spanned
import android.view.View
import android.widget.*


class ExampleActivity : AppCompatActivity() {

    private var invoker = "fromExample"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_example)


        val ivShare = findViewById<ImageView>(R.id.ivShare)
        val ivFavourites = findViewById<ImageView>(R.id.ivFavourite)
        val ivDone = findViewById<ImageView>(R.id.ivDone)
        val bToNextExample = findViewById<Button>(R.id.bToNextExample)


        val database = Firebase.database
        val myRef = database.getReference("USERS")


        var key:String? = ObjectsCollection.currentExampleKey
        var position = intent.getIntExtra("POSITION", 0)
        var currExample = ObjectsCollection.examplesList[position]


        invoker = intent.getStringExtra("INVOKER")!!

        if (invoker == "fromFav") {
            ivDone.visibility = View.GONE
            try{
                key = ObjectsCollection.favouriteExamples[position].KEY

                currExample = ObjectsCollection.favouriteExamples[position]


                findViewById<ScrollView>(R.id.svContainer).scrollTo(0,0)

                val htmlCode = ObjectsCollection.favouriteExamples[position].CODE
                val spannedCode = HtmlCompat.fromHtml(htmlCode!!, HtmlCompat.FROM_HTML_MODE_COMPACT)


                val htmlText = ObjectsCollection.favouriteExamples[position].TEXT
                val spannedText = HtmlCompat.fromHtml(htmlText!!, HtmlCompat.FROM_HTML_MODE_COMPACT)

                val htmlExplanation = ObjectsCollection.favouriteExamples[position].EXPLANATION
                val spannedExplanation= HtmlCompat.fromHtml(htmlExplanation!!, HtmlCompat.FROM_HTML_MODE_COMPACT)

                findViewById<TextView>(R.id.tvTitle).text = ObjectsCollection.favouriteExamples[position].TITLE
                findViewById<TextView>(R.id.tvExampleText).text = spannedText
                findViewById<TextView>(R.id.tvCode).text = spannedCode
                findViewById<TextView>(R.id.tvExplanation).text = spannedExplanation


            } catch (exception : Exception){
                Log.i("FAV", "$exception")
                //this toast is never shown reason unknown
                Toast.makeText(this, "Favourite Example List Is Empty Now.", Toast.LENGTH_SHORT).show()
            }

        }
        else {
            key = ObjectsCollection.examplesList[position].KEY!!

            currExample = ObjectsCollection.examplesList[position]

            ObjectsCollection.currentExamplePosition = position
            ObjectsCollection.currentExampleKey = key

            ObjectsCollection.currentExample.clear()
            ObjectsCollection.adapterCurrentExample.notifyItemRangeRemoved(0, 1)
            ObjectsCollection.currentExample.add(currExample)
            ObjectsCollection.adapterCurrentExample.notifyItemInserted(0)
            Firebase.database.getReference("USERS").child(Firebase.auth.currentUser!!.uid).child("CURR_EXAM").setValue(key)

            findViewById<ScrollView>(R.id.svContainer).scrollTo(0,0)

            val htmlCode = ObjectsCollection.examplesList[position].CODE
            val spannedCode = HtmlCompat.fromHtml(htmlCode!!, HtmlCompat.FROM_HTML_MODE_COMPACT)

            val htmlText = ObjectsCollection.examplesList[position].TEXT
            val spannedText = HtmlCompat.fromHtml(htmlText!!, HtmlCompat.FROM_HTML_MODE_COMPACT)

            val htmlExplanation = ObjectsCollection.examplesList[position].EXPLANATION
            val spannedExplanation= HtmlCompat.fromHtml(htmlExplanation!!, HtmlCompat.FROM_HTML_MODE_COMPACT)

            findViewById<TextView>(R.id.tvTitle).text = ObjectsCollection.examplesList[position].TITLE
            findViewById<TextView>(R.id.tvExampleText).text = spannedText
            findViewById<TextView>(R.id.tvCode).text = spannedCode
            findViewById<TextView>(R.id.tvExplanation).text = spannedExplanation
        }





        ivShare.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            intent.putExtra(Intent.EXTRA_TEXT, "Download EdPub")
            intent.type = "text/plain"
            startActivity(intent)
        }

        if(ObjectsCollection.favouriteExampleKeysList.indexOf(key)!=-1){
            DrawableCompat.setTint(ivFavourites.drawable, ContextCompat.getColor(this, R.color.pure_red))
        }
        else{
            DrawableCompat.setTint(ivFavourites.drawable, ContextCompat.getColor(this, R.color.icon_inactive))
        }

        setDoneIconTint(key.toString())

        ivFavourites.setOnClickListener {
            if(ObjectsCollection.favouriteExampleKeysList.indexOf(key)!=-1){
                val positionCurrExample = ObjectsCollection.favouriteExamples.indexOf(currExample)
                ObjectsCollection.favouriteExamples.remove(currExample)
                ObjectsCollection.adapterFavouriteExamples.notifyItemRemoved(positionCurrExample)
                ObjectsCollection.favouriteExampleKeysList.remove(key)

                myRef.child(Firebase.auth.currentUser!!.uid).child("FAV_EXAM").child(key!!).setValue(null)

                DrawableCompat.setTint(ivFavourites.drawable, ContextCompat.getColor(this, R.color.icon_inactive))
            }
            else{

                ObjectsCollection.favouriteExampleKeysList.add(key!!)
                ObjectsCollection.favouriteExamples.add(currExample)
                ObjectsCollection.adapterFavouriteExamples.notifyItemInserted(ObjectsCollection.favouriteExamples.size-1)

                myRef.child(Firebase.auth.currentUser!!.uid).child("FAV_EXAM").child(key.toString()).setValue(key)
                DrawableCompat.setTint(ivFavourites.drawable, ContextCompat.getColor(this, R.color.pure_red))
            }
        }

        ivDone.setOnClickListener {
            if(ObjectsCollection.completedExamplesKeysList.indexOf(key)!=-1){
                ObjectsCollection.completedExamplesKeysList.remove(key)
                myRef.child(Firebase.auth.currentUser!!.uid).child("COMP_EXAM").child(key!!).setValue(null)
                DrawableCompat.setTint(ivDone.drawable, ContextCompat.getColor(this, R.color.icon_inactive))
            }
            else{
                ObjectsCollection.completedExamplesKeysList.add(key!!)
                myRef.child(Firebase.auth.currentUser!!.uid).child("COMP_EXAM").child(key.toString()).setValue(key)
                DrawableCompat.setTint(ivDone.drawable, ContextCompat.getColor(this, R.color.slight_neon_green))
            }
        }


        bToNextExample.setOnClickListener {
            if(invoker=="fromFav"){
                if(position>=ObjectsCollection.favouriteExamples.size-1){
                    Toast.makeText(this, "No more Favourite Examples Found.", Toast.LENGTH_SHORT).show()
                }
                else {

                    try{
                        position += 1
                        key = ObjectsCollection.favouriteExamples[position].KEY!!
                        currExample = ObjectsCollection.favouriteExamples[position]

                        findViewById<ScrollView>(R.id.svContainer).scrollTo(0,0)

                        if (ObjectsCollection.favouriteExampleKeysList.contains(key)) {
                            DrawableCompat.setTint(
                                ivFavourites.drawable,
                                ContextCompat.getColor(this, R.color.pure_red)
                            )
                        } else {
                            DrawableCompat.setTint(ivFavourites.drawable, ContextCompat.getColor(this, R.color.icon_inactive))
                        }

                        val htmlCode = ObjectsCollection.favouriteExamples[position].CODE
                        val spannedCode = HtmlCompat.fromHtml(htmlCode!!, HtmlCompat.FROM_HTML_MODE_COMPACT)

                        val htmlText = ObjectsCollection.favouriteExamples[position].TEXT
                        val spannedText = HtmlCompat.fromHtml(htmlText!!, HtmlCompat.FROM_HTML_MODE_COMPACT)

                        val htmlExplanation = ObjectsCollection.favouriteExamples[position].EXPLANATION
                        val spannedExplanation= HtmlCompat.fromHtml(htmlExplanation!!, HtmlCompat.FROM_HTML_MODE_COMPACT)

                        findViewById<TextView>(R.id.tvTitle).text = ObjectsCollection.favouriteExamples[position].TITLE
                        findViewById<TextView>(R.id.tvExampleText).text = spannedText
                        findViewById<TextView>(R.id.tvCode).text = spannedCode
                        findViewById<TextView>(R.id.tvExplanation).text = spannedExplanation

                    }catch (exception:Exception){
                        Toast.makeText(this, "${exception.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            else{
                if(position>=ObjectsCollection.examplesList.size-1){
                    Toast.makeText(this, "No more Examples found", Toast.LENGTH_SHORT).show()
                }
                else {
                    position += 1
                    key = ObjectsCollection.examplesList[position].KEY!!
                    currExample = ObjectsCollection.examplesList[position]


                    ObjectsCollection.currentExample.clear()
                    ObjectsCollection.adapterCurrentExample.notifyItemRangeRemoved(0, 1)
                    ObjectsCollection.currentExampleKey = key.toString()
                    ObjectsCollection.currentExamplePosition = position
                    ObjectsCollection.currentExample.add(currExample)
                    ObjectsCollection.adapterCurrentExample.notifyItemInserted(0)
                    Firebase.database.getReference("USERS").child(Firebase.auth.currentUser!!.uid).child("CURR_EXAM").setValue(key)

                    findViewById<ScrollView>(R.id.svContainer).scrollTo(0,0)

                    if (ObjectsCollection.favouriteExampleKeysList.contains(key)) {
                        DrawableCompat.setTint(ivFavourites.drawable,ContextCompat.getColor(this, R.color.pure_red))
                    } else {
                        DrawableCompat.setTint(ivFavourites.drawable,ContextCompat.getColor(this, R.color.icon_inactive))
                    }

                    setDoneIconTint(key.toString())

                    val htmlCode = ObjectsCollection.examplesList[position].CODE
                    val spannedCode = HtmlCompat.fromHtml(htmlCode!!, HtmlCompat.FROM_HTML_MODE_COMPACT)

                    val htmlText = ObjectsCollection.examplesList[position].TEXT
                    val spannedText= HtmlCompat.fromHtml(htmlText!!, HtmlCompat.FROM_HTML_MODE_COMPACT)

                    val htmlExplanation = ObjectsCollection.examplesList[position].EXPLANATION
                    val spannedExplanation= HtmlCompat.fromHtml(htmlExplanation!!, HtmlCompat.FROM_HTML_MODE_COMPACT)

                    findViewById<TextView>(R.id.tvTitle).text = ObjectsCollection.examplesList[position].TITLE
                    findViewById<TextView>(R.id.tvExampleText).text = spannedText
                    findViewById<TextView>(R.id.tvCode).text = spannedCode
                    findViewById<TextView>(R.id.tvExplanation).text = spannedExplanation
                }
            }
        }

    }




    private fun setText(currChapter:Chapter){
        //TODO
    }



    private fun setDoneIconTint(key: String){
        val ivDone = findViewById<ImageView>(R.id.ivDone)
        if(ObjectsCollection.completedExamplesKeysList.indexOf(key)!=-1){
            DrawableCompat.setTint(ivDone.drawable, ContextCompat.getColor(this, R.color.slight_neon_green))
        }
        else{
            DrawableCompat.setTint(ivDone.drawable, ContextCompat.getColor(this, R.color.icon_inactive))
        }
    }
}