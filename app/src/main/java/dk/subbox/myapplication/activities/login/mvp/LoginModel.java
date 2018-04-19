package dk.subbox.myapplication.activities.login.mvp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewTreeObserver;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.IllegalCharsetNameException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Map;

import dk.subbox.myapplication.R;
import dk.subbox.myapplication.activities.Hometest.HomeTestActivity;
import dk.subbox.myapplication.activities.login.misc.JavaScriptGetWebBodyInterface;
import dk.subbox.myapplication.activities.login.misc.SignUpBottomSheetBehavior;
import dk.subbox.myapplication.activities.login.misc.UriChromeClient;
import dk.subbox.myapplication.activities.login.misc.UriWebViewClient;
import dk.subbox.myapplication.app.network.AuthNetwork;
import dk.subbox.myapplication.ext.Login.LoginUser;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.PrematureJwtException;
import io.jsonwebtoken.SignatureException;
import io.reactivex.Observable;
import io.reactivex.internal.operators.observable.ObservableFlatMap;
import okhttp3.ResponseBody;
import timber.log.Timber;


public class LoginModel {

    private final Activity activity;
    private final AuthNetwork authNetwork;

    private final GoogleSignInClient googleSignInClient;

    public LoginModel(Activity activity, AuthNetwork authNetwork, GoogleSignInClient googleSignInClient){
        this.activity = activity;
        this.authNetwork = authNetwork;
        this.googleSignInClient = googleSignInClient;
    }

    void startHomeActivity(){
        HomeTestActivity.start(activity);
        activity.finish();
    }

    void startHomeActivity(Bundle bundle){
        HomeTestActivity.start(activity, bundle);
        activity.finish();
    }

    Activity getActivity(){
        return activity;
    }

    String getDeviceName(){
        return Build.DEVICE;
    }

    Observable<ResponseBody> attemptLogin(LoginUser.Builder userBuilder){
        LoginUser user;
        user = userBuilder.build();
        Timber.i("++++++++++++++++++++++++ username: " + user.username());
        Timber.i("++++++++++++++++++++++++ password: " + user.password());
        Timber.i("++++++++++++++++++++++++ device_name: " + user.device_name());

        return authNetwork.LOGIN_USER_OBSERVABLE(user);
    }

    Observable<ResponseBody> GoogleBackendLogin(Map<String,String> nameValuePairs){
        return authNetwork.API_BACKEND_LOGIN(nameValuePairs);
    }

    private BottomSheetBehavior behavior;

    public void BottomSheetSetup(RelativeLayout bottomSheet){
        behavior = BottomSheetBehavior.from(bottomSheet);
        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                Button cancelButton = bottomSheet.findViewById(R.id.btn_goto_register);

                switch (newState){
                    case BottomSheetBehavior.STATE_DRAGGING:
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                        cancelButton.setText("Sign Up");
                        bottomSheet.setClickable(true);
                        break;
                    case  BottomSheetBehavior.STATE_COLLAPSED:
                        cancelButton.setText("Dont have an account?");
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
        behavior.setHideable(false);

    }

    private void StringErrorCheck(String str, String allowedChars, boolean checkForExcluded){
        if (checkForExcluded){
            Observable.range(0,str.length())
                    .map(i -> str.toCharArray()[i])
                    .doOnNext(ch -> {if(!allowedChars.contains(ch.toString()))
                    {throw new IllegalCharsetNameException("Name Error");}})
                    .subscribe();
        }else {
            Observable.range(0,str.length())
                    .map(i -> str.toCharArray()[i])
                    .any(i -> !str.contains(i.toString()))
                    .doOnSuccess(failure -> {if (failure){throw new IllegalCharsetNameException("Name Error");}})
                    .subscribe();
        }

    }

    private void NameErrorCheck(String name){
        String allowChars = " abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ-";
        StringErrorCheck(name,allowChars, true);
    }

    void PasswordErrorCheck(String password){
        String allowChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringErrorCheck(password,allowChars,false);
    }

    private void EmailErrorCheck(String email){

    }

    void checkPairs(Map<String,String> pairs){
        for (String key: pairs.keySet()) {
            switch (key){
                case "name":
                    NameErrorCheck(pairs.get(key));
                    break;

                case "password":
                    PasswordErrorCheck(pairs.get(key));
                    break;

                case "email":
                    EmailErrorCheck(pairs.get(key));
                    break;

                case "age":

                    break;

            }
        }
    }

    Observable<ResponseBody> SIGNUP_USER(Map<String,String> pairs){
        pairs.put("device_name",getDeviceName());
        return authNetwork.SIGNUP_USER_OBSERVABLE(pairs);
    }

    void setEditAgeDateListener(EditText editAge){
        TextWatcher tw = new TextWatcher() {
            private String current = "";
            private String ddmmyyyy = "DDMMYYYY";
            private Calendar cal = Calendar.getInstance();
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().equals(current)) {
                    String clean = s.toString().replaceAll("[^\\d.]|\\.", "");
                    String cleanC = current.replaceAll("[^\\d.]|\\.", "");

                    int cl = clean.length();
                    int sel = cl;
                    for (int i = 2; i <= cl && i < 6; i += 2) {
                        sel++;
                    }
                    //Fix for pressing delete next to a forward slash
                    if (clean.equals(cleanC)) sel--;

                    if (clean.length() < 8) {
                        clean = clean + ddmmyyyy.substring(clean.length());
                    } else {
                        //This part makes sure that when we finish entering numbers
                        //the date is correct, fixing it otherwise
                        int day = Integer.parseInt(clean.substring(0, 2));
                        int mon = Integer.parseInt(clean.substring(2, 4));
                        int year = Integer.parseInt(clean.substring(4, 8));

                        mon = mon < 1 ? 1 : mon > 12 ? 12 : mon;
                        cal.set(Calendar.MONTH, mon - 1);
                        year = (year < 1900) ? 1900 : (year > 2100) ? 2100 : year;
                        cal.set(Calendar.YEAR, year);
                        // ^ first set year for the line below to work correctly
                        //with leap years - otherwise, date e.g. 29/02/2012
                        //would be automatically corrected to 28/02/2012

                        day = (day > cal.getActualMaximum(Calendar.DATE)) ? cal.getActualMaximum(Calendar.DATE) : day;
                        clean = String.format("%02d%02d%02d", day, mon, year);
                    }

                    clean = String.format("%s/%s/%s", clean.substring(0, 2),
                            clean.substring(2, 4),
                            clean.substring(4, 8));

                    sel = sel < 0 ? 0 : sel;
                    current = clean;
                    editAge.setText(current);
                    editAge.setSelection(sel < current.length() ? sel : current.length());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        editAge.addTextChangedListener(tw);
    }

    public void setStateBottomSheet(int state){
        behavior.setState(state);
    }

    public int getStateBottomSheet(){
        return behavior.getState();
    }

    void WebViewSetup(WebView mWebview, FrameLayout mContainer){
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        WebSettings webSettings = mWebview.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAppCacheEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setSupportMultipleWindows(true);
        mWebview.setWebViewClient(new UriWebViewClient(activity,mWebview,mContainer));
        mWebview.setWebChromeClient(new UriChromeClient(activity,mWebview,mContainer));
        mWebview.addJavascriptInterface(new JavaScriptGetWebBodyInterface(),"HTMLOUT");
        mWebview.loadUrl(activity.getString(R.string.fb_login_url));
    }

    Jwt VerifyJWT(String token) throws IOException, CertificateException, SignatureException, PrematureJwtException{
        InputStream is = activity.getAssets().open("certs/domain1.crt");

        BufferedInputStream bis = new BufferedInputStream(is);

        CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");

        X509Certificate certificate = (X509Certificate) certificateFactory.generateCertificate(bis);

        JwtParser jwtParser = Jwts.parser().setSigningKey(certificate.getPublicKey());
        return jwtParser.parse(token);
    }

    @SuppressLint("RestrictedApi")
    Intent getGoogleSignInIntent(){
        return googleSignInClient.getSignInIntent();
    }

    void startGoogleActivityForResult(Intent intent){
        activity.startActivityForResult(intent,200);
    }
}
