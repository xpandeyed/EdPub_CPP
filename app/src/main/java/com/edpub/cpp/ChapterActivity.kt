package com.edpub.cpp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar

class ChapterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chapter)
        setSupportActionBar(findViewById(R.id.tbChapterToolBar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
}