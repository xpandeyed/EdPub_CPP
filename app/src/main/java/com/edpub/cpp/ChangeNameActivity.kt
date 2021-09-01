package com.edpub.cpp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class ChangeNameActivity : AppCompatActivity() {

    private lateinit var firebaseAuth : FirebaseAuth

    private val database = Firebase.database
    private val myRef = database.getReference("USERS")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_name)

        firebaseAuth = Firebase.auth

        val etUserEmail = findViewById<EditText>(R.id.etUserEmail)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val etNewName = findViewById<EditText>(R.id.etNewName)

        findViewById<Button>(R.id.bChangeName).setOnClickListener {
            val email = etUserEmail.text.toString()
            val password = etPassword.text.toString()
            val newName = etNewName.text.toString()

            val user = Firebase.auth.currentUser!!

            val credential = EmailAuthProvider
                .getCredential(email, password)

            user.reauthenticate(credential)
                .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                val uidCurrUser = firebaseAuth.currentUser!!.uid
                                myRef.child(uidCurrUser).child("NAME").setValue(newName)
                            }
                            else {
                                Toast.makeText(this, "${task.exception?.message}", Toast.LENGTH_SHORT).show()
                            }
                        }
                }


        }

    }
