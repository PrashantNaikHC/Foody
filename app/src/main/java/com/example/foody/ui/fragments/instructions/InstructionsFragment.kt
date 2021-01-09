package com.example.foody.ui.fragments.instructions

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import com.example.foody.R
import com.example.foody.models.Result
import com.example.foody.util.Constants
import kotlinx.android.synthetic.main.fragment_instructions.view.*

class InstructionsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_instructions, container, false)

        val result: Result? = arguments?.getParcelable(Constants.RECIPE_BUNDLE_RESULT)

        val webViewUrl: String = result!!.sourceUrl
        rootView.instructions_webview.apply {
            webViewClient = object : WebViewClient() {}
            loadUrl(webViewUrl)
        }
        return rootView
    }

}