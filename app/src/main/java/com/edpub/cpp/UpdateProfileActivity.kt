package com.edpub.cpp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class UpdateProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_profile)
        setSupportActionBar(findViewById(R.id.tbUpdateProfileToolBar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val deleteAlertDialogBox = AlertDialog.Builder(this)
            .setTitle("Are you sure you want to delete your account?")
            .setMessage("Deleting your account will delete all of your data stored with us.")
            .setPositiveButton("Delete Account") { _, _ ->
                    Firebase.auth.currentUser!!.delete()
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(this@UpdateProfileActivity, "We are sad to see you going.", Toast.LENGTH_LONG).show()
                                val intent = Intent(this@UpdateProfileActivity, SignUpActivity::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                startActivity(intent)
                            }
                            else{
                                Toast.makeText(this@UpdateProfileActivity, "${task.exception?.message}", Toast.LENGTH_LONG).show()
                            }
                        }

            }
            .setNegativeButton("Cancel") { _, _ ->
                Toast.makeText(this, "Account deletion cancelled.", Toast.LENGTH_SHORT).show()
            }.create()


        findViewById<Button>(R.id.bLogOut).setOnClickListener {
            Firebase.auth.signOut()
            /*as soon as the user signs out we must clear the favourites list
            * because if there are some elements in list and users logs in immediately
            * the previously fetched data will be there and new data will be added
            * this will cause trouble*/
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }


        findViewById<Button>(R.id.bToChangeName).setOnClickListener {
            val intent = Intent(this, ChangeNameActivity::class.java)
            startActivity(intent)
        }
        findViewById<Button>(R.id.bToChangePassword).setOnClickListener {
            val intent = Intent(this, ChangePasswordActivity::class.java)
            startActivity(intent)
        }
        findViewById<Button>(R.id.bToDeleteAccount).setOnClickListener {
            deleteAlertDialogBox.show()
        }
    }
}