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
    private var reason = "No Reason by User"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_delete_account)

        setSupportActionBar(findViewById(R.id.tbDeleteAccount))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)
        auth = FirebaseAuth.getInstance()

        findViewById<TextView>(R.id.bReport).setOnClickListener {
            try{
                val intent = Intent(Intent.ACTION_SENDTO).apply {
                    data = Uri.parse("mailto:")
                    putExtra(Intent.EXTRA_EMAIL, arrayOf("lalbiharipandeyg@gmail.com")) // recipients
                    putExtra(Intent.EXTRA_SUBJECT, "Report regarding EdPub C++")
                }
                startActivity(intent)
            }catch(exception : ActivityNotFoundException) {
                Toast.makeText(this, "No Email Client Found.\nSend an mail on lalbiharipandeyg@gmail.com", Toast.LENGTH_LONG).show()
            }
        }


        findViewById<Button>(R.id.bDeleteAccount).setOnClickListener {
            val user = auth.currentUser
            val uid = user!!.uid
            reason = findViewById<EditText>(R.id.etDeleteReason).text.toString()

            user!!.delete()
                .addOnCompleteListener(this) { task->
                    if (task.isSuccessful){
                        Firebase.database.getReference("USERS").child(uid).child("ACCOUNT_DELETE_REASON").setValue(reason)
                        Firebase.database.getReference("USERS").child(uid).child("CURR_CHAP").setValue(null)
                        Firebase.database.getReference("USERS").child(uid).child("CURR_EXAM").setValue(null)
                        Firebase.database.getReference("USERS").child(uid).child("FAV_CHAP").setValue(null)
                        Firebase.database.getReference("USERS").child(uid).child("FAV_EXAM").setValue(null)
                        Toast.makeText(this, "User Deleted Successfully.", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, SignUpActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                    }
                    else{
                        Toast.makeText(this, "Authenticating User.", Toast.LENGTH_SHORT).show()
                        signIn()
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
                                Firebase.database.getReference("USERS").child(uid).child("ACCOUNT_DELETE_REASON").setValue(reason)
                                Firebase.database.getReference("USERS").child(uid).child("CURR_CHAP").setValue(null)
                                Firebase.database.getReference("USERS").child(uid).child("CURR_EXAM").setValue(null)
                                Firebase.database.getReference("USERS").child(uid).child("FAV_CHAP").setValue(null)
                                Firebase.database.getReference("USERS").child(uid).child("FAV_EXAM").setValue(null)
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