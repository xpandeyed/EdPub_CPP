package com.edpub.cpp

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthCredential
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase



class UpdateProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_profile)
        setSupportActionBar(findViewById(R.id.tbUpdateProfileToolBar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        findViewById<Button>(R.id.bLogOut).setOnClickListener {
            Firebase.auth.signOut()
            ObjectsCollection.favouriteChapters.clear()
            ObjectsCollection.favouriteChapterKeysList.clear()
            ObjectsCollection.favouriteExampleKeysList.clear()
            ObjectsCollection.favouriteExamples.clear()
            val intent = Intent(this, LauncherActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        findViewById<Button>(R.id.bToDeleteAccount).setOnClickListener {
            val intent = Intent(this, DeleteAccountActivity::class.java)
            startActivity(intent)
        }
        val troubleMessage = "Having trouble in managing account? Contact Us."
        val spannableContactUs = SpannableString(troubleMessage)
        val span3 = object : ClickableSpan(){
            override fun onClick(p0: View) {
                val intent = Intent(Intent.ACTION_SENDTO).apply {
                    data = Uri.parse("mailto:")
                    putExtra(Intent.EXTRA_EMAIL, arrayOf("lalbiharipandeyg@gmail.com")) // recipients
                    putExtra(Intent.EXTRA_SUBJECT, "Trouble in Managing Account")
                }
                try {
                    startActivity(intent)
                } catch (e: ActivityNotFoundException) {
                    Toast.makeText(this@UpdateProfileActivity, "No email app found.", Toast.LENGTH_SHORT).show()
                }
            }
        }
        spannableContactUs.setSpan(span3, 36, 46, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE )
        val tvContactUs = findViewById<TextView>(R.id.tvContactUs)
        tvContactUs.movementMethod = LinkMovementMethod.getInstance()
        tvContactUs.text = spannableContactUs
    }

}