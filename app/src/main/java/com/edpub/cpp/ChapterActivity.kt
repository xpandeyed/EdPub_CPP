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
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.text.HtmlCompat
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import org.w3c.dom.Text
import java.lang.Exception

class ChapterActivity : AppCompatActivity() {

    private lateinit var tvChapterText : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chapter)

        tvChapterText = findViewById(R.id.tvChapterText)


        var key = intent.getStringExtra("KEY")
        val invoker = intent.getStringExtra("INVOKER")
        var position = intent.getIntExtra("POSITION", 0)


        setText(key!!)


        val bToNextChapter = findViewById<Button>(R.id.bToNextChapter)
        bToNextChapter.setOnClickListener {
            if(invoker=="FAV"){
                if(position>=ObjectsCollection.favouriteChaptersTitlesList.size-1){
                    Snackbar.make(bToNextChapter, "No more favourite chapters found", Snackbar.LENGTH_SHORT).show()
                }else{
                    position++
                    key = ObjectsCollection.chaptersTitlesList[position].KEY
                    setText(key!!)
                }
            }
            else{
                if(position>=ObjectsCollection.chaptersTitlesList.size-1){
                    Snackbar.make(bToNextChapter, "No more chapters found", Snackbar.LENGTH_SHORT).show()
                }else{
                    position++
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
        FirebaseDatabase.getInstance().getReference("C_TEXTS").child(key).child("TEXT").get().addOnSuccessListener {
            val htmlText = it.value.toString()
            val spannedText = HtmlCompat.fromHtml(htmlText, HtmlCompat.FROM_HTML_MODE_COMPACT)
            tvChapterText.text = spannedText
            tvChapterText.movementMethod = LinkMovementMethod.getInstance()
        }
    }

}