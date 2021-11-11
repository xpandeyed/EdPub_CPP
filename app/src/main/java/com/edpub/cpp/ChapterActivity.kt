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
import android.webkit.WebView
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class ChapterActivity : AppCompatActivity() {

    private lateinit var ivFavourites : ImageView

    val database = Firebase.database
    val myRef = database.getReference("USERS")
    private var invoker = "CHAP"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chapter)

        val ivClose = findViewById<ImageView>(R.id.ivClose)
        ivClose.setOnClickListener {
            onBackPressed()
        }

        val ivShare = findViewById<ImageView>(R.id.ivShare)
        ivFavourites = findViewById<ImageView>(R.id.ivFavourite)
        val ivDone = findViewById<ImageView>(R.id.ivDone)

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
            intent.putExtra(Intent.EXTRA_TEXT, "I have been learning C++ from this amazing app, EdPub C++.\n\nI recommend you to download it from Play Store.\n\n https://play.google.com/store/apps/details?id=${applicationContext.packageName}")
            intent.type = "text/plain"
            startActivity(intent)
        }

        ivFavourites.setOnClickListener {
            if(ObjectsCollection.favouriteChapterKeysList.indexOf(key)!=-1){
                myRef.child(Firebase.auth.currentUser!!.uid).child("FAV_EXAM").child(key!!).setValue(null)
                ObjectsCollection.favouriteChapterKeysList.remove(key)
                setFavIconTint(key.toString())
            }
            else{
                myRef.child(Firebase.auth.currentUser!!.uid).child("FAV_EXAM").child(key.toString()).setValue(key)
                ObjectsCollection.favouriteChapterKeysList.add(key.toString())
                setFavIconTint(key.toString())
            }
        }


        val ivToNextChapter = findViewById<ImageView>(R.id.ivToNextChapter)
        ivToNextChapter.setOnClickListener {
            if(invoker=="FAV"){
                if(position>=ObjectsCollection.favouriteChaptersTitlesList.size-1){
                    Snackbar.make(ivToNextChapter, "No more favourite chapters found", Snackbar.LENGTH_SHORT).show()
                }else{
                    position++
                    key = ObjectsCollection.chaptersTitlesList[position].KEY
                    setText(key!!)
                }
            }
            else{
                if(position>=ObjectsCollection.chaptersTitlesList.size-1){
                    Snackbar.make(ivToNextChapter, "No more chapters found", Snackbar.LENGTH_SHORT).show()
                }else{
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
                    position--
                    key = ObjectsCollection.chaptersTitlesList[position].KEY
                    setText(key!!)
                }
            }
            else{
                if(position<=0){
                    Snackbar.make(ivToNextChapter, "No more chapters found", Snackbar.LENGTH_SHORT).show()
                }else{
                    position--
                    key = ObjectsCollection.chaptersTitlesList[position].KEY
                    setText((key!!))
                }
            }
        }


        val tvContribute = findViewById<TextView>(R.id.tvContribute)
        val contributeMessage = "Want to share more information about the topic, rewrite the article on same topic or write an article on different topic. See How you can contribute and help other learners."
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
        FirebaseDatabase.getInstance().getReference("C_TITLES").child(key).child("URL").get().addOnSuccessListener {
            Log.i("XPND", it.toString())
            val wbChapterText = findViewById<WebView>(R.id.wbChapterText)
            wbChapterText.settings.javaScriptEnabled = true
            wbChapterText.loadUrl(it.value.toString())
            setFavIconTint(key)
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

    override fun onBackPressed() {
        super.onBackPressed()
    }

}