package com.eagleminds.jciindia

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.eagleminds.jciindia.databinding.ActivityNotificationWebviewBinding

class NotificationWebviewActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNotificationWebviewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotificationWebviewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupWebView()
        handleIntent(intent)
    }

    private fun setupWebView() {
        binding.notificationWebview.settings.javaScriptEnabled = true
        binding.notificationWebview.settings.domStorageEnabled = true
        binding.notificationWebview.setLayerType(View.LAYER_TYPE_SOFTWARE, null)
        binding.notificationWebview.settings.userAgentString = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537"
        binding.notificationWebview.webViewClient= object : WebViewClient() {
            var progressDialog: ProgressDialog?= ProgressDialog(this@NotificationWebviewActivity)
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                progressDialog?.setTitle("Loading...")
                progressDialog?.setMessage("Please wait...")
                progressDialog?.setCancelable(false)
                progressDialog?.show()
            }
            override fun onPageCommitVisible(view: WebView?, url: String?) {
                super.onPageCommitVisible(view, url)
                progressDialog?.dismiss()
            }
        }
        binding.notificationWebview.settings.useWideViewPort=true
        binding.notificationWebview.settings.loadWithOverviewMode=true
    }

    private fun handleIntent(intent: Intent?) {
        val url = intent?.getStringExtra("urls")
        Log.d("NotificationWebviewActivity", "URL: $url")
        if (!url.isNullOrEmpty()) {
            binding.notificationWebview.loadUrl(url)  // Here, use the ID from your layout file
        }
    }
}
