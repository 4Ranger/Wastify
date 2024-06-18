package com.capstonewahwah.wastify.ui.main.home.articleDetails

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import com.capstonewahwah.wastify.R
import com.capstonewahwah.wastify.databinding.FragmentArticleDetailsBinding

class ArticleDetailsFragment : Fragment() {
    private var _binding: FragmentArticleDetailsBinding? = null
    private val binding get() = _binding

    private var webView: WebView? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentArticleDetailsBinding.inflate(inflater, container, false)
        return binding?.root
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val url = ArticleDetailsFragmentArgs.fromBundle(arguments as Bundle).url

        webView = binding?.webView
        webView?.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                view?.loadUrl(request?.url.toString())
                return super.shouldOverrideUrlLoading(view, request)
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                binding?.loader?.visibility = View.GONE
            }
        }
        webView?.settings?.loadsImagesAutomatically = true
        webView?.settings?.javaScriptEnabled = true
        webView?.loadUrl(url)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}