package com.edpub.cpp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import com.google.android.material.progressindicator.CircularProgressIndicator

class WebActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web)

        val wbMyWebView = findViewById<WebView>(R.id.wbMyWebView)
        val pbWebPageProgress = findViewById<CircularProgressIndicator>(R.id.pbWebPageProgress)

        val url = intent.getStringExtra("URL")
        if(url!=null){
            wbMyWebView.settings.javaScriptEnabled = true
            wbMyWebView.webViewClient = object : WebViewClient(){
                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    wbMyWebView.visibility = View.VISIBLE
                    pbWebPageProgress.visibility = View.GONE
                }
            }
        }
        wbMyWebView.loadUrl(url!!)
    }
}