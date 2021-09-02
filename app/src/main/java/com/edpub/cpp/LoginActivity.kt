package com.edpub.cpp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val etEmail = findViewById<EditText>(R.id.etUserEmail)
        val etPassword = findViewById<EditText>(R.id.etPassword)

        val bLogIn = findViewById<Button>(R.id.bLogIn)
        bLogIn.setOnClickListener {
            if(etEmail.text.toString().isEmpty() || etPassword.text.toString().isEmpty())
            {
                Toast.makeText(this, "Any of the fields can not be empty.", Toast.LENGTH_SHORT).show()
            }
            else
            {
                logIn(etEmail.text.toString(), etPassword.text.toString())
            }
        }
    }

    private fun logIn(email:String, password:String) = CoroutineScope(Dispatchers.IO).launch {
        val firebaseAuth = Firebase.auth
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this@LoginActivity){task->
            if(task.isSuccessful)
            {


                val intent = Intent(this@LoginActivity, HomeActivity::class.java)
                startActivity(intent)
            }
            else
            {
                Toast.makeText(this@LoginActivity, "${task.exception?.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}