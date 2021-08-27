package com.edpub.cpp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toolbar

class ProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        setSupportActionBar(findViewById(R.id.tbProfileToolBar))
        findViewById<androidx.appcompat.widget.Toolbar>(R.id.tbProfileToolBar)
    }
}