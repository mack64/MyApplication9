package dk.subbox.myapplication.activities.login.mvp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.common.api.ApiException;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import dk.subbox.myapplication.activities.Hometest.HomeTestActivity;
import dk.subbox.myapplication.app.network.AuthNetwork;
import dk.subbox.myapplication.ext.Login.LoginUser;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.PrematureJwtException;
import io.jsonwebtoken.SignatureException;
import io.reactivex.Observable;
import okhttp3.Response;
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

    Observable<ResponseBody> apiBackendVerification(String accessToken){
        return authNetwork.API_LOGIN("Bearer " + accessToken, getDeviceName());
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
