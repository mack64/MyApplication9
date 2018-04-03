package dk.subbox.myapplication.activities.login.mvp;

import android.annotation.SuppressLint;
import android.content.Intent;

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

import dk.subbox.myapplication.ext.Login.LoginUser;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.PrematureJwtException;
import io.jsonwebtoken.SignatureException;
import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import okhttp3.internal.http.HttpCodec;
import retrofit2.HttpException;
import retrofit2.Response;
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

        compositeDisposable.add(LoginButtonClickSub());
        compositeDisposable.add(LoginGoogleButtonClickSub());
        FaceBookLoginSetup();
    }

    public void onDestroy(){
        compositeDisposable.clear();
    }

    @SuppressLint({"RestrictedApi", "CheckResult"})
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        //TODO: Google ResultCode ?!?!? find ud af hvad man gør her.
        if (requestCode == 200) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.

            try {
                Task<GoogleSignInAccount> googleSignInAccountTask = GoogleSignIn.getSignedInAccountFromIntent(data);
                //account = handleSignInResult(googleSignInAccountTask);
                GoogleSignInAccount googleSignInAccount = handleSignInResult(googleSignInAccountTask);
                Observable<ResponseBody> responseBodyObservable = Observable.just(googleSignInAccount)
                        .observeOn(Schedulers.io())
                        .switchMap(__ -> model.apiBackendVerification(googleSignInAccount.getIdToken()));
                ResponseBody responseBody = responseBodyObservable.blockingSingle();
                Jwt jwt = model.VerifyJWT(responseBody.string());
            }catch (Exception ex){
                onConnectionError(ex);
            }

        }
    }

    private Disposable LoginButtonClickSub(){
        return view.ObservableLoginButton()
                .doOnNext(__ -> view.showLoading(true))
                .map(__ -> LoginUser.builder())
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
                .map(responseBodyToken -> model.VerifyJWT(responseBodyToken.string()))
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(this::onConnectionError)
                .doOnEach(__ -> view.showLoading(false))
                .retry()
                .subscribe(data -> {model.startHomeActivity();});
    }


    private Disposable LoginGoogleButtonClickSub(){
        return view.ObservervableGoogleLoginButton()
                .map(__ -> model.getGoogleSignInIntent())
                .doOnNext(intent -> model.startGoogleActivityForResult(intent))
                .doOnError(this::onConnectionError)
                .retry()
                .subscribe();
    }

    GoogleSignInAccount handleSignInResult(com.google.android.gms.tasks.Task<GoogleSignInAccount> completedTask) throws ApiException {
        return completedTask.getResult(ApiException.class);
    }

    private void UpdateUI(GoogleSignInAccount googleSignInAccount){


    }

    //TODO: save validated token.
    //TODO: say which characters can be used; validate user input.
    //TODO: What todo when there is an invalid token?

    private void onConnectionError(Throwable error){
        Timber.e(error);
        if (error instanceof SSLException){
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
    }

    private void onApiError(Throwable error){
        Timber.e(error);
        if (error instanceof HttpException){

        }
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
