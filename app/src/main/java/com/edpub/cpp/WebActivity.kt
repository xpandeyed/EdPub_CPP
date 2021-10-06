package com.edpub.cpp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient

class WebActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web)

        val url = intent.getStringExtra("URL")
        val wbMyWebView = findViewById<WebView>(R.id.wbMyWebView)
        wbMyWebView.webViewClient = WebViewClient()
        wbMyWebView.getSettings().setJavaScriptEnabled(true)
        wbMyWebView.loadUrl(url!!)
    }
}