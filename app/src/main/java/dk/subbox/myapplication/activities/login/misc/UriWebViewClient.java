package dk.subbox.myapplication.activities.login.misc;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.net.http.SslError;
import android.util.Log;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import timber.log.Timber;


public class UriWebViewClient extends WebViewClient {

    private static final String target_url="https://www.subbox.dk";
    private static final String target_url_prefix="subbox.dk";
    private Context mContext;
    private WebView mWebview;
    private FrameLayout mContainer;

    public UriWebViewClient(Context context,WebView webView, FrameLayout container){
        mContext = context;
        mWebview = webView;
        mContainer = container;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        String host = Uri.parse(url).getHost();
        //Log.d("shouldOverrideUrlLoading", url);
        if (host.equals(target_url_prefix))
        {
            return false;
        }

        if(host.equals("m.facebook.com"))
        {
            return false;
        }
        // Otherwise, the link is not for a page on my site, so launch
        // another Activity that handles URLs
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        mContext.startActivity(intent);
        return true;
    }

    @Override
    public void onLoadResource(WebView view, String url) {
        super.onLoadResource(view, url);
        String host = Uri.parse(url).getHost();

        if (host.equals(target_url_prefix)){
            if(mWebview!=null)
            {
                view.evaluateJavascript("(function() { return (document.getElementsByTagName('body')[0].innerHTML); })();",
                        html -> {
                            Log.d("HTML", html);
                            // code here
                        });
                mWebview.setVisibility(View.GONE);
                mContainer.removeView(mWebview);
                mWebview = null;
            }
        }
    }

    @Override
    public void onReceivedSslError(WebView view, SslErrorHandler handler,
                                   SslError error) {
        Log.d("onReceivedSslError", "onReceivedSslError");
        //super.onReceivedSslError(view, handler, error);
    }
}