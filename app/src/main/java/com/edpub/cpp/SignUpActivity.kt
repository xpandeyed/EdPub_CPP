package com.edpub.cpp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.*
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private const val RC_SIGN_IN = 0

class SignUpActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        val privacyPolicyMessage = "By Signing In you agree to our Terms and Conditions and Privacy Policy."
        val spannable = SpannableString(privacyPolicyMessage)
        val span1 = object : ClickableSpan(){
            override fun onClick(p0: View) {
                Toast.makeText(this@SignUpActivity, "Privacy", Toast.LENGTH_SHORT).show()
            }
        }
        val span2 = object : ClickableSpan(){
            override fun onClick(p0: View) {
                Toast.makeText(this@SignUpActivity, "Terms", Toast.LENGTH_SHORT).show()
            }
        }
        spannable.setSpan(span1, 31, 51, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannable.setSpan(span2, 56, 70, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        findViewById<TextView>(R.id.tvTermsConditionsAndPrivacyPolicy).movementMethod = LinkMovementMethod.getInstance()
        findViewById<TextView>(R.id.tvTermsConditionsAndPrivacyPolicy).text = spannable


        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)
        auth = FirebaseAuth.getInstance()

        val cvSignIn = findViewById<CardView>(R.id.cvSignIn)
        cvSignIn.setOnClickListener {
            signIn()
        }

    }
    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        CoroutineScope(Dispatchers.Main).launch {
            super.onActivityResult(requestCode, resultCode, data)
            if (requestCode == RC_SIGN_IN) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                if(task.isSuccessful){
                    try {
                        val account = task.getResult(ApiException::class.java)!!
                        Toast.makeText(this@SignUpActivity, "Authenticating with Google.", Toast.LENGTH_SHORT).show()
                        firebaseAuthWithGoogle(account.idToken!!)
                    } catch (e: ApiException) {
                        Toast.makeText(this@SignUpActivity, "${e.message}", Toast.LENGTH_SHORT).show()
                    }
                }

            }
        }

    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        CoroutineScope(Dispatchers.IO).launch {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this@SignUpActivity){ task ->
                if (task.isSuccessful) {
                    Toast.makeText(
                        this@SignUpActivity,
                        "Sign in with credential is successful",
                        Toast.LENGTH_SHORT
                    ).show()
                    CoroutineScope(Dispatchers.IO).launch {
                        //checking if it is new user
                        ObjectsCollection.isNewUser = task.result.additionalUserInfo!!.isNewUser
                        FunctionCollection.loadChapters()

                        val user = auth.currentUser
                        val uid = user?.uid
                        val firebaseDatabase = FirebaseDatabase.getInstance().getReference("USERS")

                        if(ObjectsCollection.isNewUser){
                            firebaseDatabase.child(uid!!).child("CURR_CHAP").setValue("C111")
                            firebaseDatabase.child(uid).child("NAME").setValue(user.displayName)
                            firebaseDatabase.child(uid).child("EMAIL").setValue(user.email)
                        }
                        else{
                            FunctionCollection.loadFavouriteChapterKeys()
                            firebaseDatabase.child(uid!!).child("CURR_CHAP").get().addOnSuccessListener {
                                ObjectsCollection.currentChapterPosition=it.value.toString().toInt()
                            }
                        }
                        val intent = Intent(this@SignUpActivity, HomeActivity::class.java)
                        startActivity(intent)
                    }
                } else {
                    Toast.makeText(this@SignUpActivity, "${task.exception}", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }
}