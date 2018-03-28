package dk.subbox.myapplication.activities.login.mvp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Resources;
import android.text.Html;
import android.view.View;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import javax.net.ssl.SSLException;
import javax.net.ssl.SSLPeerUnverifiedException;

import dk.subbox.myapplication.R;
import dk.subbox.myapplication.ext.LoginUser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.PrematureJwtException;
import io.jsonwebtoken.SignatureException;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.HttpException;
import timber.log.Timber;


public class LoginPresenter {

    LoginView view;

    LoginModel model;

    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    public LoginPresenter(LoginView view, LoginModel model){
        this.view = view;
        this.model = model;
    }

    public void onCreate(){


        setSignUpButtonTextBold();
        compositeDisposable.add(LoginButtonClickSub());
        compositeDisposable.add(LoginGoogleButtonClickSub());
        FaceBookLoginSetup();
    }

    public void onDestroy(){
        compositeDisposable.clear();
    }

    @SuppressLint("RestrictedApi")
    public void onActivityResult(int requestCode, int resultCode, Intent data) throws ApiException {
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == 200) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.

            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            model.handleSignInResult(task);

          /*  Observable<Object> googleSigninObservable = Observable.create(new ObservableOnSubscribe<Object>() {
                @Override
                public void subscribe(ObservableEmitter<Object> emitter) throws Exception {
                    model.startHomeActivity();
                }
            });
*/
            /*googleSigninObservable
                    .doOnError(error -> {view.wrongUsernameOrPasswordToast(); Timber.e(error);})
                    .map(__ -> GoogleSignIn.getSignedInAccountFromIntent(data))
                    .doOnNext(task -> model.handleSignInResult(task))
                    .subscribe();
        */
        }
    }

    private Disposable LoginButtonClickSub(){
        return view.ObservableLoginButton()
                .doOnNext(__ -> view.showLoading(true))
                .map(__ -> {return LoginUser.builder();})
                .doOnNext(user -> {
                    if (view.getEditEmailText().isEmpty())
                        view.setEditEmailError("This field cannot be empty");
                    else
                        user.setUsername(view.getEditEmailText());

                    if (view.getEditPasswordText().isEmpty())
                        view.setEditPasswordError("This field cannot be empty");
                    else
                        user.setPassword(view.getEditPasswordText());})
                .doOnNext(user -> user.setDevice_name(model.getDeviceName()))
                .observeOn(Schedulers.io())
                .switchMap(user -> model.attemptLogin(user))
                .map(reponseBodyToken -> model.VerifyJWTTest2(reponseBodyToken.string()))
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(error -> onConnectionError(error))
                .doOnEach(__ -> view.showLoading(false))
                .retry()
                .subscribe(data -> {model.startHomeActivity();});
    }

    private Disposable LoginGoogleButtonClickSub(){
        return view.ObservervableGoogleLoginButton()
                .map(__ -> model.getGoogleSignInIntent())
                .doOnNext(intent -> model.startGoogleActivityForResult(intent))
                .subscribe();
    }

    //TODO: save validated token.
    //TODO: say which characters can be used; validate user input.
    //TODO: What todo when there is an invalid token?

    private void onConnectionError(Throwable error){
        Timber.e(error);
        if (error instanceof SSLException || error instanceof SSLPeerUnverifiedException){
            view.UnsecureConnectionMessage();
        }else if (error instanceof HttpException){
            view.wrongUsernameOrPasswordToast();
        }else if (error instanceof IllegalStateException) {
            view.UnsecureConnectionMessage();
        }else if (error instanceof PrematureJwtException){
            view.wrongUsernameOrPasswordToast();
        }else if (error instanceof SignatureException){
            view.wrongUsernameOrPasswordToast();
        }else {
            view.wrongUsernameOrPasswordToast();
        }
        Timber.e(error);
    }

    private void setSignUpButtonTextBold(){
        String text = "Dont have an account? <b>Sign up</b>";
        view.setSignUpButtonText(Html.fromHtml(text).toString());
    }

    private void FaceBookLoginSetup(){

        CallbackManager callbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        // App code
                        Timber.i("Success");
                    }

                    @Override
                    public void onCancel() {
                        // App code
                        Timber.i("Cancel");
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                        Timber.i(exception);
                    }
                });
    }

    boolean isLoggedInFacebook(){
        return AccessToken.getCurrentAccessToken() == null;
    }
}
