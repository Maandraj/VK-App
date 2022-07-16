package com.maandraj.auth_impl.presentation.authScreen

import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import com.maandraj.auth_impl.domain.AuthInteractor
import com.maandraj.core.BuildConfig
import com.maandraj.core_ui.mainColor


@Composable
fun AuthScreen(
    navController: NavHostController,
    viewModel:AuthVM,
    modifier: Modifier
    ){
    Scaffold(modifier.fillMaxSize(),
        content = { MyContent() }
    )
}

@Composable
fun MyContent(){
    val mUrl = BuildConfig.BASE_URL
    AndroidView(factory = {
        WebView(it).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            webViewClient = WebViewClient()
            loadUrl(mUrl)
        }
    }, update = {
        it.loadUrl(mUrl)
    })
}


//@Preview(showBackground = true)
//@Composable
//fun DefaultPreview() {
//    AuthScreen(NavHostController(LocalContext.current), AuthVM(AuthInteractor(())), Modifier)
//}