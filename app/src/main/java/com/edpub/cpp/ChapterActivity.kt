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
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ChapterActivity : AppCompatActivity() {

    private lateinit var ivFavourites : ImageView
    private lateinit var ivDone : ImageView
    private val database = Firebase.database
    private val myRef = database.getReference("USERS")
    private var invoker = "CHAP"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chapter)

        val ivClose = findViewById<ImageView>(R.id.ivClose)
        ivClose.setOnClickListener {
            onBackPressed()
        }

        val ivShare = findViewById<ImageView>(R.id.ivShare)
        ivFavourites = findViewById(R.id.ivFavourite)
        ivDone = findViewById<ImageView>(R.id.ivDone)

        var key = intent.getStringExtra("KEY")
        Log.i("XPND", key.toString())

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
            if(ObjectsCollection.favouriteChapterKeysList.indexOf(key)!=-1){
                myRef.child(Firebase.auth.currentUser!!.uid).child("FAV_CHAP").child(key!!).setValue(null)
                ObjectsCollection.favouriteChapterKeysList.remove(key)
                setFavIconTint(key.toString())
            }
            else{
                myRef.child(Firebase.auth.currentUser!!.uid).child("FAV_CHAP").child(key.toString()).setValue(key)
                ObjectsCollection.favouriteChapterKeysList.add(key.toString())
                setFavIconTint(key.toString())
            }
        }
        ivDone.setOnClickListener {
            if(ObjectsCollection.completedChaptersKeysList.indexOf(key)!=-1){
                myRef.child(Firebase.auth.currentUser!!.uid).child("COMP_CHAP").child(key!!).setValue(null)
                ObjectsCollection.completedChaptersKeysList.remove(key)
                setDoneIconTint(key.toString())
            }else{
                myRef.child(Firebase.auth.currentUser!!.uid).child("COMP_CHAP").child(key!!).setValue(key)
                ObjectsCollection.completedChaptersKeysList.add(key.toString())
                setDoneIconTint(key.toString())
            }
        }

        val ivToNextChapter = findViewById<ImageView>(R.id.ivToNextChapter)
        ivToNextChapter.setOnClickListener {
            if(invoker=="FAV"){
                if(position>=ObjectsCollection.favouriteChaptersTitlesList.size-1){
                    Snackbar.make(ivToNextChapter, "No more favourite chapters found", Snackbar.LENGTH_SHORT).show()
                }else{
                    findViewById<ScrollView>(R.id.svContainer).visibility = View.GONE
                    findViewById<CircularProgressIndicator>(R.id.pbLoading).visibility = View.VISIBLE
                    position++
                    key = ObjectsCollection.chaptersTitlesList[position].KEY
                    setText(key!!)
                }
            }
            else{
                if(position>=ObjectsCollection.chaptersTitlesList.size-1){
                    Snackbar.make(ivToNextChapter, "No more chapters found", Snackbar.LENGTH_SHORT).show()
                }else{
                    findViewById<ScrollView>(R.id.svContainer).visibility = View.GONE
                    findViewById<CircularProgressIndicator>(R.id.pbLoading).visibility = View.VISIBLE
                    position++
                    key = ObjectsCollection.chaptersTitlesList[position].KEY
                    setText((key!!))
                }
            }
        }


        val ivToPreviousChapter = findViewById<ImageView>(R.id.ivToPreviousChapter)
        ivToPreviousChapter.setOnClickListener {
            if(invoker=="FAV"){
                if(position<=0){
                    Snackbar.make(ivToNextChapter, "No more favourite chapters found", Snackbar.LENGTH_SHORT).show()
                }else{
                    findViewById<ScrollView>(R.id.svContainer).visibility = View.GONE
                    findViewById<CircularProgressIndicator>(R.id.pbLoading).visibility = View.VISIBLE
                    position--
                    key = ObjectsCollection.chaptersTitlesList[position].KEY
                    setText(key!!)
                }
            }
            else{
                if(position<=0){
                    Snackbar.make(ivToNextChapter, "No more chapters found", Snackbar.LENGTH_SHORT).show()
                }else{
                    findViewById<ScrollView>(R.id.svContainer).visibility = View.GONE
                    findViewById<CircularProgressIndicator>(R.id.pbLoading).visibility = View.VISIBLE
                    position--
                    key = ObjectsCollection.chaptersTitlesList[position].KEY
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
        Log.i("XPND", "set text : $key" )
        CoroutineScope(Dispatchers.Main).launch {

        }
        FirebaseDatabase.getInstance().getReference("C_TITLES").child(key).child("URL").get().addOnSuccessListener {
            Log.i("XPND", it.toString())
            val wbChapterText = findViewById<WebView>(R.id.wbChapterText)
            wbChapterText.settings.javaScriptEnabled = true
            wbChapterText.loadUrl(it.value.toString())
            wbChapterText.webChromeClient = object : WebChromeClient(){
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
            setCurrChapter(key)
        }
    }
    private fun setFavIconTint(key: String){
        if(ObjectsCollection.favouriteChapterKeysList.indexOf(key)!=-1){
            DrawableCompat.setTint(ivFavourites.drawable, ContextCompat.getColor(this, R.color.pure_red))
        }else{
            DrawableCompat.setTint(ivFavourites.drawable, ContextCompat.getColor(this, R.color.icon_inactive))
        }
    }
    private fun setCurrChapter(key: String){
        if(invoker=="FAV"){
            return
        }
        ObjectsCollection.currentChapterKey = key
        myRef.child(Firebase.auth.currentUser!!.uid).child("CURR_CHAP").setValue(key)
    }
    private fun setDoneIconTint(key: String){
        if(ObjectsCollection.completedChaptersKeysList.indexOf(key)!=-1){
            DrawableCompat.setTint(ivDone.drawable, ContextCompat.getColor(this, R.color.slight_neon_green))
        }else{
            DrawableCompat.setTint(ivDone.drawable, ContextCompat.getColor(this, R.color.icon_inactive))
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

}