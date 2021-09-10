package com.edpub.cpp

import android.content.Intent
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

        }
        findViewById<TextView>(R.id.tvPrivacyPolicy).setOnClickListener {

        }
        findViewById<TextView>(R.id.tvFeedback).setOnClickListener {
            val intent = Intent(this, FeedbackActivity::class.java)
            startActivity(intent)
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