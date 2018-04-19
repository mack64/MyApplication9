package dk.subbox.myapplication.activities.login.misc;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Message;
import android.util.Log;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.FrameLayout;


public class UriChromeClient extends WebChromeClient {

    private Context mContext;
    private FrameLayout mContainer;
    private WebView mWebview;

    public UriChromeClient(Context context,
                           WebView Webview,
                           FrameLayout frameLayout){
        mContext = context;
        mWebview = Webview;
        mContainer = frameLayout;
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public boolean onCreateWindow(WebView view, boolean isDialog,
                                  boolean isUserGesture, Message resultMsg) {
        WebView mWebviewPop = new WebView(mContext);
        mWebviewPop.setVerticalScrollBarEnabled(false);
        mWebviewPop.setHorizontalScrollBarEnabled(false);
        mWebviewPop.setWebViewClient(new UriWebViewClient(mContext,mWebview,mContainer));
        mWebviewPop.getSettings().setJavaScriptEnabled(true);
        mWebviewPop.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        mContainer.addView(mWebviewPop);
        WebView.WebViewTransport transport = (WebView.WebViewTransport) resultMsg.obj;
        transport.setWebView(mWebviewPop);
        resultMsg.sendToTarget();

        return true;
    }

    @Override
    public void onCloseWindow(WebView window) {
        Log.d("onCloseWindow", "called");
    }


}
