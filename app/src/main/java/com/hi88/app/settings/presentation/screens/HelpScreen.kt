package com.hi88.app.settings.presentation.screens

import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.hi88.app.game.R
import com.hi88.app.util.RollitTopAppBar


@Composable
fun HelpScreen(
    navController: NavController
) {
    Scaffold(
        topBar = {
            RollitTopAppBar(
                title = R.string.help_faq,
                showNavigationIcons = true,
                showActions = true,
                titleAlignment = TextAlign.Center,
                navController = navController
            )
        },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        AndroidView(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            factory = { context ->
                WebView(context).apply {
                    webViewClient = WebViewClient()

                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )

                    settings.apply {
                        javaScriptEnabled = true
                        domStorageEnabled = true
                        loadWithOverviewMode = true
                        useWideViewPort = true
                        builtInZoomControls = false
                        displayZoomControls = false
                        setSupportZoom(false)
                        allowFileAccess = true
                        allowContentAccess = true
                    }

                    loadUrl("file:///android_asset/help.html")
                }
            }
        )
    }
}
