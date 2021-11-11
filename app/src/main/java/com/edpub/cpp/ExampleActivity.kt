package com.edpub.cpp

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.widget.*
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.FirebaseDatabase


class ExampleActivity : AppCompatActivity() {

    private lateinit var ivFavourites : ImageView
    private lateinit var ivDone : ImageView
    private val database = Firebase.database
    private val myRef = database.getReference("USERS")
    private var invoker = "EXAM"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_example)

        val ivClose = findViewById<ImageView>(R.id.ivClose)
        ivClose.setOnClickListener {
            onBackPressed()
        }

        val ivShare = findViewById<ImageView>(R.id.ivShare)
        ivFavourites = findViewById(R.id.ivFavourite)
        ivDone = findViewById<ImageView>(R.id.ivDone)

        var key = intent.getStringExtra("KEY")

        if(intent.getStringExtra("INVOKER")!=null){
            invoker = intent.getStringExtra("INVOKER")!!
        }
        var position = intent.getIntExtra("POSITION", 0)

        setText(key!!)

        if(invoker=="FAV"){
            ivFavourites.visibility = View.GONE
            ivDone.visibility = View.GONE
        }

        ivShare.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share))
            intent.type = "text/plain"
            startActivity(intent)
        }
        ivFavourites.setOnClickListener {
            if(ObjectsCollection.favouriteExampleKeysList.indexOf(key.toString())!=-1){
                myRef.child(Firebase.auth.currentUser!!.uid).child("FAV_EXAM").child(key.toString()).setValue(null)
                ObjectsCollection.favouriteExampleKeysList.remove(key.toString())
                setFavIconTint(key.toString())
            }
            else{
                myRef.child(Firebase.auth.currentUser!!.uid).child("FAV_EXAM").child(key.toString()).setValue(key)
                ObjectsCollection.favouriteExampleKeysList.add(key.toString())
                setFavIconTint(key.toString())
            }
        }

        ivDone.setOnClickListener {
            if(ObjectsCollection.completedExamplesKeysList.indexOf(key)!=-1){
                myRef.child(Firebase.auth.currentUser!!.uid).child("COMP_EXAM").child(key!!).setValue(null)
                ObjectsCollection.completedExamplesKeysList.remove(key)
                setDoneIconTint(key.toString())
            }else{
                myRef.child(Firebase.auth.currentUser!!.uid).child("COMP_EXAM").child(key!!).setValue(key)
                ObjectsCollection.completedExamplesKeysList.add(key.toString())
                setDoneIconTint(key.toString())
            }
        }

        val ivToNextExample = findViewById<ImageView>(R.id.ivToNextExample)
        ivToNextExample.setOnClickListener {
            if(invoker=="FAV"){
                if(position>=ObjectsCollection.favouriteExamplesTitlesList.size-1){
                    Snackbar.make(ivToNextExample, "No more favourite examples found", Snackbar.LENGTH_SHORT).show()
                }else{
                    findViewById<ScrollView>(R.id.svContainer).visibility = View.GONE
                    findViewById<CircularProgressIndicator>(R.id.pbLoading).visibility = View.VISIBLE
                    position++
                    key = ObjectsCollection.exampleTitlesList[position].KEY
                    setText(key!!)
                }
            }
            else{
                if(position>=ObjectsCollection.exampleTitlesList.size-1){
                    Snackbar.make(ivToNextExample, "No more examples found", Snackbar.LENGTH_SHORT).show()
                }else{
                    findViewById<ScrollView>(R.id.svContainer).visibility = View.GONE
                    findViewById<CircularProgressIndicator>(R.id.pbLoading).visibility = View.VISIBLE
                    position++
                    key = ObjectsCollection.exampleTitlesList[position].KEY
                    setText((key!!))
                }
            }
        }


        val ivToPreviousExample = findViewById<ImageView>(R.id.ivToPreviousExample)
        ivToPreviousExample.setOnClickListener {
            if(invoker=="FAV"){
                if(position<=0){
                    Snackbar.make(ivToNextExample, "No more favourite examples found", Snackbar.LENGTH_SHORT).show()
                }else{
                    position--
                    findViewById<ScrollView>(R.id.svContainer).visibility = View.GONE
                    findViewById<CircularProgressIndicator>(R.id.pbLoading).visibility = View.VISIBLE
                    key = ObjectsCollection.exampleTitlesList[position].KEY
                    setText(key!!)
                }
            }
            else{
                if(position<=0){
                    Snackbar.make(ivToNextExample, "No more examples found", Snackbar.LENGTH_SHORT).show()
                }else{
                    findViewById<ScrollView>(R.id.svContainer).visibility = View.GONE
                    findViewById<CircularProgressIndicator>(R.id.pbLoading).visibility = View.VISIBLE
                    position--
                    key = ObjectsCollection.exampleTitlesList[position].KEY
                    setText((key!!))
                }
            }
        }

        val tvContribute = findViewById<TextView>(R.id.tvContribute)
        val contributeMessage = getString(R.string.contribute_message)
        val spannableContributeUs = SpannableString(contributeMessage)
        val span = object  : ClickableSpan(){
            override fun onClick(p0: View) {
                val url = "https://edpubweb.blogspot.com/2021/09/Privacy%20Policy.html"
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(url)
                startActivity(intent)
            }
        }
        spannableContributeUs.setSpan(span, contributeMessage.length-35, contributeMessage.length-1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        tvContribute.movementMethod = LinkMovementMethod.getInstance()
        tvContribute.text = spannableContributeUs


    }

    private fun setText(key: String){
        FirebaseDatabase.getInstance().getReference("E_TITLES").child(key).child("URL").get().addOnSuccessListener {
            Log.i("XPND", it.toString())
            val wbExampleText = findViewById<WebView>(R.id.wbExampleText)
            wbExampleText.settings.javaScriptEnabled = true
            wbExampleText.loadUrl(it.value.toString())
            wbExampleText.webChromeClient = object : WebChromeClient(){
                override fun onProgressChanged(view: WebView?, newProgress: Int) {
                    super.onProgressChanged(view, newProgress)
                    if(newProgress==100){
                        findViewById<ScrollView>(R.id.svContainer).visibility = View.VISIBLE
                        findViewById<CircularProgressIndicator>(R.id.pbLoading).visibility = View.GONE
                    }
                }
            }
            setFavIconTint(key)
            setDoneIconTint(key)
            setCurrExample(key)
        }
    }
    private fun setFavIconTint(key: String){
        if(ObjectsCollection.favouriteExampleKeysList.indexOf(key)!=-1){
            DrawableCompat.setTint(ivFavourites.drawable, ContextCompat.getColor(this, R.color.pure_red))
        }else{
            DrawableCompat.setTint(ivFavourites.drawable, ContextCompat.getColor(this, R.color.icon_inactive))
        }
    }
    private fun setCurrExample(key: String){
        if(invoker=="FAV"){
            return
        }
        ObjectsCollection.currentExampleKey = key
        myRef.child(Firebase.auth.currentUser!!.uid).child("CURR_EXAM").setValue(key)
    }
    private fun setDoneIconTint(key: String){
        if(ObjectsCollection.completedExamplesKeysList.indexOf(key)!=-1){
            DrawableCompat.setTint(ivDone.drawable, ContextCompat.getColor(this, R.color.slight_neon_green))
        }else{
            DrawableCompat.setTint(ivDone.drawable, ContextCompat.getColor(this, R.color.icon_inactive))
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

}