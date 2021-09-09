package com.edpub.cpp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LauncherActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val auth: FirebaseAuth = Firebase.auth

        if(auth.currentUser==null) {
            val intent = Intent(this@LauncherActivity, SignUpActivity::class.java)
            startActivity(intent)
        }
        else {
            FunctionCollection.loadChapters()
            FunctionCollection.loadFavouriteChapterKeys()
            Firebase.database.getReference("USERS").child(Firebase.auth.currentUser?.uid!!).child("CURR_CHAP").get().addOnSuccessListener {
                ObjectsCollection.currentChapterPosition=it.value.toString().toInt()
            }
            val intent = Intent(this@LauncherActivity, HomeActivity::class.java)
            startActivity(intent)
        }
        setContentView(R.layout.activity_launcher)
    }
}