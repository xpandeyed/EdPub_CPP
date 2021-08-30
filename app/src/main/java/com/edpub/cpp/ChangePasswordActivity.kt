package com.edpub.cpp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class ChangePasswordActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password)
        setSupportActionBar(findViewById(R.id.tbChangePassword))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
}