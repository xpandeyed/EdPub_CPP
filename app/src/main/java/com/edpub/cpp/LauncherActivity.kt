package com.edpub.cpp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LauncherActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launcher)

        val auth: FirebaseAuth = Firebase.auth
        if(auth.currentUser==null)
        {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
        else
        {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }

    }
}