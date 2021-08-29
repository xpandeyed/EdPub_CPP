package com.edpub.cpp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog

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
                Toast.makeText(this, "We are sad to see you going.", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Cancel") { _, _ ->
                Toast.makeText(this, "Account deletion cancelled.", Toast.LENGTH_SHORT).show()
            }.create()


        findViewById<Button>(R.id.bToChangeName).setOnClickListener {

        }
        findViewById<Button>(R.id.bToChangePassword).setOnClickListener {

        }
        findViewById<Button>(R.id.bToDeleteAccount).setOnClickListener {
            deleteAlertDialogBox.show()
        }
    }
}