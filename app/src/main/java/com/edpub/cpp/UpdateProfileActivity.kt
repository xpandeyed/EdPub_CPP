package com.edpub.cpp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class UpdateProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_profile)
        setSupportActionBar(findViewById(R.id.tbUpdateProfileToolBar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
}