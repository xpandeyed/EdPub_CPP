package com.edpub.cpp

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.*
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.widget.ProgressBar
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
                val url = "https://edpubweb.blogspot.com/2021/09/Terms%20And%20Conditions.html"
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(url)
                startActivity(intent)
            }
        }
        val span2 = object : ClickableSpan(){
            override fun onClick(p0: View) {
                val url = "https://edpubweb.blogspot.com/2021/09/Privacy%20Policy.html"
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(url)
                startActivity(intent)
            }
        }
        spannable.setSpan(span1, 31, 51, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannable.setSpan(span2, 56, 70, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        val tvTermsConditionsAndPrivacyPolicy = findViewById<TextView>(R.id.tvTermsConditionsAndPrivacyPolicy)
        tvTermsConditionsAndPrivacyPolicy.movementMethod = LinkMovementMethod.getInstance()
        tvTermsConditionsAndPrivacyPolicy.text = spannable


        val troubleMessage = "Having trouble in signing in? Contact Us."
        val spannableContactUs = SpannableString(troubleMessage)
        val span3 = object : ClickableSpan(){
            override fun onClick(p0: View) {
                val intent = Intent(Intent.ACTION_SENDTO).apply {
                    data = Uri.parse("mailto:")
                    putExtra(Intent.EXTRA_EMAIL, arrayOf("lalbiharipandeyg@gmail.com")) // recipients
                    putExtra(Intent.EXTRA_SUBJECT, "Trouble in Signing In")
                }
                try {
                    startActivity(intent)
                } catch (e: ActivityNotFoundException) {
                    Toast.makeText(this@SignUpActivity, "No email app found.", Toast.LENGTH_SHORT).show()
                }
            }
        }
        spannableContactUs.setSpan(span3, 30, 40, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE )
        val tvContact = findViewById<TextView>(R.id.tvContact)
        tvContact.movementMethod = LinkMovementMethod.getInstance()
        tvContact.text = spannableContactUs


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
        findViewById<ProgressBar>(R.id.pbSignUp).visibility = View.VISIBLE
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        CoroutineScope(Dispatchers.Main).launch {
            findViewById<ProgressBar>(R.id.pbSignUp).visibility = View.GONE
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

                        val user = auth.currentUser
                        val uid = user?.uid
                        val firebaseDatabase = FirebaseDatabase.getInstance().getReference("USERS")

                        if(ObjectsCollection.isNewUser){
                            firebaseDatabase.child(uid!!).child("CURR_CHAP").setValue("C111AAA")
                            firebaseDatabase.child(uid).child("CURR_EXAM").setValue("E111AAA")
                        }
                        val intent = Intent(this@SignUpActivity, HomeActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
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