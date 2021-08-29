package com.edpub.cpp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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
                val deleteUser = Firebase.auth.currentUser!!
                CoroutineScope(Dispatchers.IO).launch {
                    deleteUser.delete()
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(this@UpdateProfileActivity, "We are sad to see you going.", Toast.LENGTH_SHORT).show()
                                val intent = Intent(this@UpdateProfileActivity, SignUpActivity::class.java)
                                startActivity(intent)
                            }
                        }
                }
            }
            .setNegativeButton("Cancel") { _, _ ->
                Toast.makeText(this, "Account deletion cancelled.", Toast.LENGTH_SHORT).show()
            }.create()


        findViewById<Button>(R.id.bToChangeName).setOnClickListener {

        }
        findViewById<Button>(R.id.bToChangePassword).setOnClickListener {

        }
        findViewById<Button>(R.id.bToDeleteAccount).setOnClickListener {
            Toast.makeText(this, "Make sure you have logged in recently. If not so, please logout and login once.", Toast.LENGTH_LONG).show()
            deleteAlertDialogBox.show()
        }
    }
}