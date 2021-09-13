package com.edpub.cpp

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import org.w3c.dom.Text

class ProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        findViewById<TextView>(R.id.tvUserName).text = FirebaseAuth.getInstance().currentUser?.displayName
        findViewById<TextView>(R.id.tvEmail).text = FirebaseAuth.getInstance().currentUser?.email
        Glide.with(this).load(FirebaseAuth.getInstance().currentUser?.photoUrl).into(findViewById(R.id.ivProfilePicture))

        setSupportActionBar(findViewById(R.id.tbProfileToolBar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        findViewById<CardView>(R.id.cvUserDetails).setOnClickListener {
            val intent = Intent(this, UpdateProfileActivity::class.java)
            startActivity(intent)
        }

        findViewById<TextView>(R.id.tvTermsConditions).setOnClickListener {
            val url = "https://edpubweb.blogspot.com/2021/09/Terms%20And%20Conditions.html"
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            startActivity(intent)
        }
        findViewById<TextView>(R.id.tvPrivacyPolicy).setOnClickListener {
            val url = "https://edpubweb.blogspot.com/2021/09/Privacy%20Policy.html"
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            startActivity(intent)
        }
        findViewById<TextView>(R.id.tvReport).setOnClickListener {
            try{
                val intent = Intent(Intent.ACTION_SENDTO).apply {
                    data = Uri.parse("mailto:")
                    putExtra(Intent.EXTRA_EMAIL, arrayOf("lalbiharipandeyg@gmail.com")) // recipients
                    putExtra(Intent.EXTRA_SUBJECT, "Report regarding EdPub C++")
                }
                startActivity(intent)
            }catch(exception : ActivityNotFoundException) {
                Toast.makeText(this, "No Email Client Found.\nSend an mail on lalbiharipandeyg@gmail.com", Toast.LENGTH_LONG).show()
            }
        }
        findViewById<TextView>(R.id.tvFeedback).setOnClickListener {
            try{
                val intent = Intent(Intent.ACTION_SENDTO).apply {
                    data = Uri.parse("mailto:")
                    putExtra(Intent.EXTRA_EMAIL, arrayOf("lalbiharipandeyg@gmail.com"))
                    putExtra(Intent.EXTRA_SUBJECT, "Feedback for EdPub C++")
                }
                startActivity(intent)
            }catch(exception : ActivityNotFoundException) {
                Toast.makeText(this, "No Email Client Found.\nSend an mail on lalbiharipandeyg@gmail.com", Toast.LENGTH_LONG).show()
            }

        }
        findViewById<TextView>(R.id.tvShare).setOnClickListener {
            Toast.makeText(this, "Share", Toast.LENGTH_SHORT).show()
        }
        findViewById<TextView>(R.id.tvRateUs).setOnClickListener {
            Toast.makeText(this, "Rate Us", Toast.LENGTH_SHORT).show()
        }
        findViewById<TextView>(R.id.tvVersion).setOnClickListener {
            Toast.makeText(this, "Version", Toast.LENGTH_SHORT).show()
        }
    }
}