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

        findViewById<android.view.View>(R.id.clUserDetails).setOnClickListener {
            val intent = Intent(this, UpdateProfileActivity::class.java)
            startActivity(intent)
        }

        findViewById<TextView>(R.id.tvTermsConditions).setOnClickListener {
            val url = "https://xpandeyed.github.io/EdPubCPPWeb/TermsAndConditions.html"
            val intent = Intent(this, WebActivity::class.java).apply {
                putExtra("URL", url)
            }
            startActivity(intent)
        }
        findViewById<TextView>(R.id.tvPrivacyPolicy).setOnClickListener {
            val url = "https://xpandeyed.github.io/EdPubCPPWeb/PrivacyPolicy.html"
            val intent = Intent(this, WebActivity::class.java).apply {
                putExtra("URL", url)
            }
            startActivity(intent)
        }
        findViewById<TextView>(R.id.tvReport).setOnClickListener {
            val url = "https://docs.google.com/forms/d/e/1FAIpQLScqWJta8g0dWkZKpe4TKnFf-Ud6PINh2nQoqJZDPfrvX9c7xw/viewform?usp=sf_link"
            val intent = Intent(this, WebActivity::class.java).apply {
                putExtra("URL", url)
            }
            startActivity(intent)

        }
        findViewById<TextView>(R.id.tvFeedback).setOnClickListener {
            val url = "https://docs.google.com/forms/d/e/1FAIpQLScBQl72xho71K5f83_ceI5A8KNiBgfNNsvG6gkriEsN454r_Q/viewform?usp=sf_link"
            val intent = Intent(this, WebActivity::class.java).apply {
                putExtra("URL", url)
            }
            startActivity(intent)
        }
        findViewById<TextView>(R.id.tvShare).setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            intent.putExtra(Intent.EXTRA_TEXT, R.string.share)
            intent.type = "text/plain"
            startActivity(intent)
        }
        findViewById<TextView>(R.id.tvRateUs).setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse(
                    "https://play.google.com/store/apps/details?id=com.edpub.cpp")
                setPackage("com.android.vending")
            }
            startActivity(intent)
        }
        findViewById<TextView>(R.id.tvVersion).setOnClickListener {
            Toast.makeText(this, "Version 1.0.0", Toast.LENGTH_SHORT).show()
        }
        findViewById<TextView>(R.id.tvAboutUs).setOnClickListener {
            val url = "https://edpubweb.blogspot.com/2021/09/about-us.html"
            val intent = Intent(this, WebActivity::class.java).apply {
                putExtra("URL", url)
            }
            startActivity(intent)
        }

    }

}