package com.maandraj.auth_impl.presentation.authScreen.webclient

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import com.maandraj.auth_impl.R
import com.maandraj.core.BuildConfig

class VKWebClient(
    private val context: Context,
    private val onStatusChange: (status: AuthStatus, msg:String) -> Unit,
) : WebViewClient() {
    private var _currentUrl = ""
    override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
        view?.loadUrl(request?.url.toString())
        return super.shouldOverrideUrlLoading(view, request)
    }

    /**
     * Check URL and return result for event
     */
    override fun onPageFinished(wv: WebView, url: String) {
        if (_currentUrl != url) {
            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            if (url.contains(AUTH_URL)) {
                if (url.contains(BuildConfig.SCOPE)) {
                    imm.showSoftInput(wv, 0)
                    wv.visibility = View.VISIBLE
                    onStatusChange(AuthStatus.AUTH, context.getString(R.string.auth_log_in_text))
                }
                if (url.contains(CONFIRM_PARAMS)) {
                    onStatusChange(AuthStatus.CONFIRM,  context.getString(R.string.auth_accept_permissions))
                }
                if (url.contains(ERROR_PARAMS)) {
                    onStatusChange(AuthStatus.ERROR_INVALID,  context.getString(R.string.auth_invalid_username_or_password))
                }
            }
            if (url.contains(BLOCKED_URL)) {
                onStatusChange(AuthStatus.BLOCKED,  context.getString(R.string.auth_account_blocked))
            }
            if (url.contains(SUCCESS_URL)) {
                if (url.contains(ERROR_USER_DENIED)){
                    wv.visibility = View.GONE
                    onStatusChange(AuthStatus.ERROR_USER_DENIED, context.getString(R.string.auth_error_blank))
                }else{
                    wv.visibility = View.GONE
                    onStatusChange(AuthStatus.SUCCESS, context.getString(R.string.auth_successful_authentication))
                }
            }
        }
        _currentUrl = url
    }
}

private const val AUTH_URL = "https://oauth.vk.com/authorize"
private const val SUCCESS_URL = "https://oauth.vk.com/blank.html"
private const val BLOCKED_URL = "https://m.vk.com/login?act=blocked"
private const val CONFIRM_PARAMS = "q_hash"
private const val ERROR_PARAMS = "email"
private const val ERROR_USER_DENIED = "user_denied"
