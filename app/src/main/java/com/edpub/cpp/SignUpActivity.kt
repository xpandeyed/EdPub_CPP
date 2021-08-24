package com.edpub.cpp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SignUpActivity : AppCompatActivity() {

    private lateinit var firebaseAuth : FirebaseAuth

    private val database = Firebase.database
    private val myRef = database.getReference("USERS")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        firebaseAuth = Firebase.auth

        val bSignUp = findViewById<Button>(R.id.bSignUp)

        val etUserName= findViewById<EditText>(R.id.etUserName)
        val etUserEmail = findViewById<EditText>(R.id.etUserEmail)
        val etPasswordOne= findViewById<EditText>(R.id.etPasswordOne)
        val etPasswordTwo= findViewById<EditText>(R.id.etPasswordTwo)

        bSignUp.setOnClickListener {
            if(etUserName.text.toString().isEmpty() || etUserEmail.text.toString().isEmpty() || etPasswordOne.text.toString().isEmpty() || etPasswordTwo.text.toString().isEmpty())
            {
                Toast.makeText(this, "Any of the fields can not be empty.", Toast.LENGTH_SHORT).show()
            }
            else if(etPasswordOne.text.toString()!=etPasswordTwo.text.toString())
            {
                Toast.makeText(this, "Passwords do no match.", Toast.LENGTH_SHORT).show()
            }
            else
            {
                registerUser(etUserName.text.toString(), etPasswordOne.text.toString(), etUserEmail.text.toString())
            }
        }
    }
    private fun registerUser(name:String, password:String, email:String) = CoroutineScope(Dispatchers.IO).launch{
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this@SignUpActivity) { task ->
            if (task.isSuccessful) {
                val uidCurrUser = firebaseAuth.currentUser!!.uid
                myRef.child(uidCurrUser).child("NAME").setValue(name)
                myRef.child(uidCurrUser).child("EMAIL").setValue(email)
                val intent = Intent(this@SignUpActivity, HomeActivity::class.java)
                startActivity(intent)
            }
            else {
                Toast.makeText(this@SignUpActivity, "${task.exception?.message}", Toast.LENGTH_LONG).show()
            }
        }
    }
}