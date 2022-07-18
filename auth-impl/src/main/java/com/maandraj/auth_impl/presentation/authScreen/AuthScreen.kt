package com.maandraj.auth_impl.presentation.authScreen

import android.content.Context
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.webkit.CookieManager
import android.webkit.WebView
import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.maandraj.auth_impl.R
import com.maandraj.auth_impl.presentation.authScreen.webclient.AuthStatus
import com.maandraj.auth_impl.presentation.authScreen.webclient.VKWebClient
import com.maandraj.core.data.result.ResultOf
import com.maandraj.core_ui.LogoVK
import com.maandraj.core_ui.samples.TextButtonView
import com.maandraj.feature_api.utils.register
import kotlinx.coroutines.*
import java.util.regex.Pattern

@Composable
fun AuthScreen(
    navController: NavHostController,
    viewModel: AuthVM,
    modifier: Modifier,
) {
    val context = LocalContext.current
    val isAccountSave: ResultOf<Boolean> by viewModel.isSaveAccountState.observeAsState(ResultOf.Success(
        false))
    val isLogged: Boolean by viewModel.isAuth.observeAsState(false)
    val isLoading: Boolean by viewModel.loading.observeAsState(false)
    val scope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState()
    val textButton: String =
        remember {
            viewModel.isLogged()
            context.getString(if (isLogged) R.string.auth_logged_button_text else R.string.auth_sign_in_button_text)
        }
    val url: String by viewModel.tokenUri.observeAsState("")
    SetObservables(isAccountSave = isAccountSave,
        navController = navController,
        scaffoldState = scaffoldState,
        scope = scope)
    Scaffold(modifier.fillMaxSize(),
        scaffoldState = scaffoldState,
        content = {
            Box(modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally) {
                    LogoVK()
                    if (isLoading)
                        CircularProgressIndicator()
                    TextButtonView(
                        modifier = Modifier.fillMaxWidth(.7F),
                        text = textButton,
                        onClick = {
                            viewModel.isLogged()
                            if (!isLogged)
                                viewModel.getAuthUrl()
                        })
                }
            }
            if (url.isNotEmpty()) {
                VKAuthWebView(
                    scaffoldState = scaffoldState,
                    url = url,
                    scope = scope,
                    viewModel = viewModel
                )
            }
        }
    )
}


@Composable
private fun SetObservables(
    isAccountSave: ResultOf<Boolean>,
    scaffoldState: ScaffoldState,
    navController: NavHostController,
    scope: CoroutineScope,
) {
    val context = LocalContext.current
    isAccountSave.let {
        when (it) {
            is ResultOf.Success -> {
                if (it.value) {
                    LaunchedEffect(KEY_SNACKBAR) {
                        scaffoldState.snackbarHostState.showSnackbar(context.getString(R.string.auth_successful_authentication))
                    }
                    scope.cancel("Finish authentication")
                }
            }
            is ResultOf.Failure -> {
                LaunchedEffect(KEY_SNACKBAR) {
                    scaffoldState.snackbarHostState.showSnackbar(it.message)
                }
            }
        }
    }
}

@Composable
fun VKAuthWebView(
    url: String,
    viewModel: AuthVM,
    scaffoldState: ScaffoldState,
    scope: CoroutineScope,
    context: Context = LocalContext.current,
) {
    val webView: WebView = WebView(context).apply {
        layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        loadUrl(url)
    }
    if (url.isEmpty()) webView.visibility = View.GONE else webView.visibility = View.VISIBLE
    webView.webViewClient = VKWebClient(context) { status, msg ->
        scope.launch(Dispatchers.IO) {
            CoroutineScope(Dispatchers.Main).launch {
                scaffoldState.snackbarHostState.showSnackbar(msg)
            }
            when (status) {
                AuthStatus.AUTH -> {
                    Log.i(TAG, msg)
                }
                AuthStatus.CONFIRM -> {
                    Log.i(TAG, msg)
                }
                AuthStatus.ERROR_INVALID -> {
                    Log.e(TAG, msg)
                }
                AuthStatus.ERROR_USER_DENIED -> {
                    Log.e(TAG, msg)
                    goneAuthWindow(viewModel)
                }
                AuthStatus.BLOCKED -> {
                    Log.e(TAG, msg)
                    showAuthWindow(webView, url)
                }
                AuthStatus.SUCCESS -> {
                    Log.i(TAG, msg)
                    var webUrl = ""
                    withContext(Dispatchers.Main) {
                        webView.visibility = View.GONE
                        if (webView.url == null) {
                            showAuthWindow(webView, url)
                        } else {
                            webUrl = webView.url!!
                            withContext(Dispatchers.IO) {
                                val tokenIndexStart = webUrl.indexOf("=") + 1
                                val tokenIndexEnd = webUrl.indexOf("&")
                                val token = webUrl.substring(tokenIndexStart, tokenIndexEnd)
                                val expiresMatcher =
                                    Pattern.compile("expires_in=\\w+").matcher(webUrl)
                                val userIdMather = Pattern.compile("user_id=\\w+").matcher(webUrl)
                                if (token.isNotEmpty() && userIdMather.find() && expiresMatcher.find()) {
                                    val expires =
                                        expiresMatcher.group().replace("expires_in=".toRegex(), "")
                                    val userId =
                                        userIdMather.group().replace("user_id=".toRegex(), "")
                                    if (token.isNotEmpty() && userId.isNotEmpty()) {
                                        withContext(Dispatchers.Main) {
                                            viewModel.saveAccount(accessToken = token,
                                                expiresTime = expires.toLong(),
                                                userId = userId.toInt())
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    AndroidView(
        factory = {
            webView
        },
        update = {
            it.loadUrl(url)
        },
    )
}

private suspend fun showAuthWindow(webView: WebView, url: String) {
    CookieManager.getInstance().removeAllCookies(null)
    webView.loadUrl(url)
}

private fun goneAuthWindow(viewModel: AuthVM) {
    viewModel.authUrlClear()
}

private const val TAG = "AuthScreen"
private const val KEY_SNACKBAR = "SNACKBAR"