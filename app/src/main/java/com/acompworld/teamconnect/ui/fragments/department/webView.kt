package com.acompworld.teamconnect.ui.fragments.department

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.acompworld.teamconnect.databinding.FragmentWebviewBinding
import com.acompworld.teamconnect.ui.MainActivity
import com.acompworld.teamconnect.utils.Constants.KEY_FILE

class webView : Fragment() {

   private var binding : FragmentWebviewBinding?= null

    private var url : String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.apply{
            url = getString(KEY_FILE)
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (requireActivity() as MainActivity).apply {
//            toolbar?.isVisible = false
//            footer?.isVisible = false
        }
        binding = FragmentWebviewBinding.inflate(inflater,container,false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        url?.let {
            binding?.apply {
                webView.apply {
                    settings.javaScriptEnabled = true
                   val mobileUserAgentString = "Mozilla/5.0 (Linux; Android 5.1.1; Nexus 5 Build/LMY48B; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/43.0.2357.65 Mobile Safari/537.36"
                    settings.userAgentString=mobileUserAgentString
                    webChromeClient = object : WebChromeClient() {
                        override fun onProgressChanged(view: WebView?, newProgress: Int) {
                            super.onProgressChanged(view, newProgress)
                            if (newProgress < 100 &&  !progressCircular.isVisible) {
                               progressCircular.isVisible = true
                            }
                            if (newProgress == 100) {
                               progressCircular.isVisible = false
                            }
                        }

                        override fun onReceivedTitle(view: WebView?, title: String?) {
                            super.onReceivedTitle(view, title)
                            (requireActivity() as MainActivity).toolbar?.title = title
                        }
                    }
                webViewClient = WebViewClient()
                loadUrl(it)
            }
        }
        }
        url?: Log.d("omega_url","url null")
    }

    override fun onDestroy() {
        (requireActivity() as MainActivity).apply {
//            toolbar?.isVisible = true
//            footer?.isVisible = true
        }
        binding = null
        super.onDestroy()
    }

}