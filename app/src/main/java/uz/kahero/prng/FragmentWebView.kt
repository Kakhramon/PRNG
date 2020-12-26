package uz.kahero.prng

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment

class FragmentWebView : Fragment(R.layout.fragment_web_view) {
    @SuppressLint("SetJavaScriptEnabled")
    private var uri:String=""
    private var downloadUrl = ""
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<WebView>(R.id.web).apply {
            webViewClient = WebViewClient()
            settings.setSupportZoom(true)
            settings.cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK
            settings.javaScriptEnabled = true
            loadUrl(uri)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.menu_web, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.download) {
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse(downloadUrl)
            }
            startActivity(intent)
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        fun newInstance(url:String,downloadUrl:String) = FragmentWebView().apply {
            this.uri = url
            this.downloadUrl = downloadUrl
        }
    }
}