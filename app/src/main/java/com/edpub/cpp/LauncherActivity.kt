package com.edpub.cpp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class LauncherActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launcher)

        CoroutineScope(Dispatchers.IO).launch {
                val databaseReference : DatabaseReference = FirebaseDatabase.getInstance().getReference("CHAPTERS")
                databaseReference.addValueEventListener(object: ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if(snapshot.exists()){
                            for(chapter in snapshot.children){
                                val currChapter = chapter.getValue(Chapter::class.java)
                                ObjectsCollection.chaptersList.add(currChapter!!)
                            }
                        }
                    }
                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(this@LauncherActivity, "$error", Toast.LENGTH_SHORT).show()
                    }
                })
            val auth: FirebaseAuth = Firebase.auth
            if(auth.currentUser==null)
            {
                val intent = Intent(this@LauncherActivity, SignUpActivity::class.java)
                startActivity(intent)
            }
            else
            {
                val intent = Intent(this@LauncherActivity, HomeActivity::class.java)
                startActivity(intent)
            }
        }
    }
}