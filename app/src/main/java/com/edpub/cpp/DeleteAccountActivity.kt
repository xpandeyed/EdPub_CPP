package com.edpub.cpp

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private const val RC_SIGN_IN = 0

class DeleteAccountActivity : AppCompatActivity() {


    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_delete_account)

        setSupportActionBar(findViewById(R.id.tbDeleteAccount))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        findViewById<TextView>(R.id.tvDeleteMessage).text = "If you feel something that must be improved, let us know it. We will surely improve that as soon as possible.\n\nBut if you have made your mind to go, leaving a feedback or a report will help us to improve the app for other users."


        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)
        auth = FirebaseAuth.getInstance()

        findViewById<TextView>(R.id.bFeedback).setOnClickListener {
            val url = "https://docs.google.com/forms/d/e/1FAIpQLScBQl72xho71K5f83_ceI5A8KNiBgfNNsvG6gkriEsN454r_Q/viewform?usp=sf_link"
            val intent = Intent(this, WebActivity::class.java).apply {
                putExtra("URL", url)
            }
            startActivity(intent)
        }

        findViewById<TextView>(R.id.bReport).setOnClickListener {
            val url = "https://docs.google.com/forms/d/e/1FAIpQLScqWJta8g0dWkZKpe4TKnFf-Ud6PINh2nQoqJZDPfrvX9c7xw/viewform?usp=sf_link"
            val intent = Intent(this, WebActivity::class.java).apply {
                putExtra("URL", url)
            }
            startActivity(intent)
        }


        findViewById<Button>(R.id.bDeleteAccount).setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                val user = auth.currentUser
                val uid = user!!.uid

                user.delete()
                    .addOnCompleteListener(this@DeleteAccountActivity) { task->
                        if (task.isSuccessful){
                            Firebase.database.getReference("USERS").child(uid).setValue(null)
                            Toast.makeText(this@DeleteAccountActivity, "User Deleted Successfully.", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this@DeleteAccountActivity, SignUpActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(intent)
                        }
                        else{
                            Toast.makeText(this@DeleteAccountActivity, "Authenticating User.", Toast.LENGTH_SHORT).show()
                            signIn()
                        }
                    }
            }

        }
    }

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                Toast.makeText(this, "${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser!!
                    val uid = user!!.uid
                    user.delete()
                        .addOnCompleteListener { task->
                            if(task.isSuccessful){
                                Firebase.database.getReference("USERS").child(uid).setValue(null)
                                Toast.makeText(this, "User Deleted Successfully.", Toast.LENGTH_SHORT).show()
                                val intent = Intent(this, SignUpActivity::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                startActivity(intent)
                            }
                        }
                } else {
                    Toast.makeText(this, "${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }


}