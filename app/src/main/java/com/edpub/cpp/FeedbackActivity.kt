package com.edpub.cpp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class FeedbackActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feedback)

        setSupportActionBar(findViewById(R.id.tbFeedback))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
}