package com.eagleminds.jciindia

import android.app.ProgressDialog
import android.content.BroadcastReceiver
import android.content.Context

import android.content.Intent
import android.content.IntentFilter
import android.graphics.Bitmap
import android.net.ConnectivityManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.os.Handler
import android.os.Looper
import android.widget.Toast


import com.eagleminds.jciindia.databinding.ActivityMainBinding
import com.google.firebase.messaging.FirebaseMessaging

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var offlineLayout: View

    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        offlineLayout = findViewById(R.id.offlineLayout)
        setupWebView()
        handleIntent(intent)
       }
    private fun handleIntent(intent: Intent?) {
        val url = intent?.getStringExtra("urls")
        Log.d("MainActivity", "URL: $url")
        if (!url.isNullOrEmpty()) {
            binding.webView.loadUrl(url)
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    private fun setupWebView(){
        binding.webView.settings.javaScriptEnabled = true
        binding.webView.settings.domStorageEnabled = true
        binding.webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null)
        binding.webView.settings.userAgentString = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true)
        }

        binding.webView.scrollBarStyle= View.SCROLLBARS_INSIDE_OVERLAY
        binding.webView.webViewClient= object : WebViewClient(){
            var progressDialog:ProgressDialog?= ProgressDialog(this@MainActivity)
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
        binding.webView.settings.useWideViewPort=true
        binding.webView.settings.loadWithOverviewMode=true
        binding.webView.loadUrl("https://www.tamilanjobs.in/web/pdf.html")
    }
    private var doubleBackToExitPressedOnce = false
    private val DOUBLE_BACK_PRESS_INTERVAL = 2000 // time interval in milliseconds (2000ms = 2s)

    override fun onBackPressed() {
        if (binding.webView != null && binding.webView.canGoBack()) {
            binding.webView.goBack()
        } else {
            if (doubleBackToExitPressedOnce) {
                finish()  // Finish the current activity, exiting the app
                return
            }
            this.doubleBackToExitPressedOnce = true
            Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show()
            Handler(Looper.getMainLooper()).postDelayed({ doubleBackToExitPressedOnce = false }, DOUBLE_BACK_PRESS_INTERVAL.toLong())
        }
    }
    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }
    override fun onResume() {
        super.onResume()
        val intentFilter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        registerReceiver(networkChangeReceiver, intentFilter)
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(networkChangeReceiver)
    }

    private val networkChangeReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (isNetworkAvailable()) {
                // Internet connection available
                binding.webView.visibility = View.VISIBLE
                offlineLayout.visibility = View.GONE
            } else {
                // No internet connection
                binding.webView.visibility = View.GONE
                offlineLayout.visibility = View.VISIBLE
            }
        }
    }


}