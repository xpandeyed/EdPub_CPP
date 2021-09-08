package com.edpub.cpp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class TermsAndConditionsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_terms_and_conditions)

        setSupportActionBar(findViewById(R.id.tbTermsConditions))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
}