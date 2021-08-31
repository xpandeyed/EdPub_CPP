package com.edpub.cpp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DeleteAccountActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_delete_account)

        val etUserEmail = findViewById<EditText>(R.id.etUserEmail)
        val etPassword = findViewById<EditText>(R.id.etPassword)


        findViewById<Button>(R.id.tbDeleteAccount).setOnClickListener {
            val email = etUserEmail.text.toString()
            val password = etPassword.text.toString()

            val user = Firebase.auth.currentUser!!

            val credential = EmailAuthProvider
                .getCredential(email, password)

            user.reauthenticate(credential)
                .addOnCompleteListener {
                    Firebase.auth.currentUser!!.delete()
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(
                                    this@DeleteAccountActivity,
                                    "We are sad to see you going.",
                                    Toast.LENGTH_LONG
                                ).show()
                                val intent = Intent(this@DeleteAccountActivity, SignUpActivity::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                startActivity(intent)
                            }
                            else {
                                Toast.makeText(this@DeleteAccountActivity,"${task.exception?.message}",Toast.LENGTH_LONG).show()
                            }
                        }
                    }


        }
    }
}