package com.edpub.cpp

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
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.lang.Exception

class ExampleActivity : AppCompatActivity() {

    private var invoker = "fromExample"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_example)

        val database = Firebase.database
        val myRef = database.getReference("USERS")

        var key:String? = ObjectsCollection.currentExampleKey
        var position = intent.getIntExtra("POSITION", 0)
        var currExample = ObjectsCollection.examplesList[position]
        invoker = intent.getStringExtra("INVOKER")!!
        when (invoker) {
            "fromFav" -> {
                try{
                    key = ObjectsCollection.favouriteExamples[position].KEY

                    currExample = ObjectsCollection.favouriteExamples[position]

                    findViewById<TextView>(R.id.tvTitle).text = ObjectsCollection.favouriteExamples[position].TITLE
                    findViewById<TextView>(R.id.tvExampleText).text = ObjectsCollection.favouriteExamples[position].TEXT
                    findViewById<TextView>(R.id.tvCode).text = ObjectsCollection.favouriteExamples[position].CODE
                }
                catch (exception : Exception){
                    Log.i("FAV", "$exception")
                    //this toast is never shown reason unknown
                    Toast.makeText(this, "Favourite Example List Is Empty Now.", Toast.LENGTH_SHORT).show()
                }

            }
            "fromExample" -> {
                key = ObjectsCollection.examplesList[position].KEY!!

                currExample = ObjectsCollection.examplesList[position]

                ObjectsCollection.currentExamplePosition = position
                ObjectsCollection.currentExampleKey = key

                Firebase.database.getReference("USERS").child(Firebase.auth.currentUser!!.uid).child("CURR_EXAM").setValue(key)

                findViewById<TextView>(R.id.tvTitle).text = ObjectsCollection.examplesList[position].TITLE
                findViewById<TextView>(R.id.tvExampleText).text = ObjectsCollection.examplesList[position].TEXT
                findViewById<TextView>(R.id.tvCode).text = ObjectsCollection.examplesList[position].CODE
            }
            "fromCurrExam" -> {

                currExample = ObjectsCollection.examplesList[ObjectsCollection.currentExamplePosition]

                findViewById<TextView>(R.id.tvTitle).text = ObjectsCollection.examplesList[position].TITLE
                findViewById<TextView>(R.id.tvExampleText).text = ObjectsCollection.examplesList[position].TEXT
                findViewById<TextView>(R.id.tvCode).text = ObjectsCollection.examplesList[position].CODE
            }
        }

        val ivShare = findViewById<ImageView>(R.id.ivShare)
        val ivFavourites = findViewById<ImageView>(R.id.ivFavourite)
        val bToNextExample = findViewById<Button>(R.id.bToNextExample)



        ivShare.setOnClickListener {

        }

        if(ObjectsCollection.favouriteExampleKeysList.indexOf(key)!=-1){
            DrawableCompat.setTint(ivFavourites.drawable, ContextCompat.getColor(this, R.color.primaryColor))
        }
        else{
            DrawableCompat.setTint(ivFavourites.drawable, ContextCompat.getColor(this, R.color.icon_inactive))
        }

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
                DrawableCompat.setTint(ivFavourites.drawable, ContextCompat.getColor(this, R.color.primaryColor))
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

                        if (ObjectsCollection.favouriteExampleKeysList.contains(key)) {
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
                            ObjectsCollection.favouriteExamples[position].TITLE
                        findViewById<TextView>(R.id.tvExampleText).text =
                            ObjectsCollection.favouriteExamples[position].TEXT
                        findViewById<TextView>(R.id.tvCode).text =
                            ObjectsCollection.favouriteExamples[position].CODE
                    }catch (exception:Exception){
                        Toast.makeText(this, "$exception", Toast.LENGTH_SHORT).show()
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

                    ObjectsCollection.currentExamplePosition = position
                    ObjectsCollection.currentExampleKey = key.toString()

                    Firebase.database.getReference("USERS").child(Firebase.auth.currentUser!!.uid).child("CURR_EXAM").setValue(key)

                    if (ObjectsCollection.favouriteExampleKeysList.contains(key)) {
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
                        ObjectsCollection.examplesList[position].TITLE
                    findViewById<TextView>(R.id.tvExampleText).text =
                        ObjectsCollection.examplesList[position].TEXT
                    findViewById<TextView>(R.id.tvCode).text =
                        ObjectsCollection.examplesList[position].CODE
                }
            }
        }

    }
}