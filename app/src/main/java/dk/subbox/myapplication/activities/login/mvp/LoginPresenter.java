package dk.subbox.myapplication.activities.login.mvp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

import com.facebook.CallbackManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import java.security.Key;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.SSLException;

import dk.subbox.myapplication.R;
import dk.subbox.myapplication.activities.Hometest.HomeTestActivity;
import dk.subbox.myapplication.activities.login.misc.SignUpBottomSheetBehavior;
import dk.subbox.myapplication.ext.Login.LoginUser;
import dk.subbox.myapplication.ext.SignUser;
import io.jsonwebtoken.PrematureJwtException;
import io.jsonwebtoken.SignatureException;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.HttpException;
import timber.log.Timber;


public class LoginPresenter {

    private LoginView view;

    private LoginModel model;

    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    public LoginPresenter(LoginView view, LoginModel model){
        this.view = view;
        this.model = model;
    }

    public void onCreate(){
        model.BottomSheetSetup(view.layoutBottomSheet);
        model.setEditAgeDateListener(view.editAge);
        model.PasswordErrorCheck("hi there man");
        compositeDisposable.add(LoginButtonClickSub());
        compositeDisposable.add(LoginGoogleButtonClickSub());
        compositeDisposable.add(FacebookLoginTestButtonClickSub());
        compositeDisposable.add(GotoRegisterButtonClickSub());
    }

    public void onDestroy(){
        compositeDisposable.clear();
    }

    @SuppressLint({"RestrictedApi", "CheckResult"})
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        //TODO: Google ResultCode ?!?!? find ud af hvad man gør her.
        if (requestCode == 200) {
            // The Task returned from this call is always completed, no need to attach a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                Map<String,String> nameValuePairs = new HashMap<>();
                ResponseBody googleLogin = Observable.just(task)
                        .map(googleTask -> task.getResult(ApiException.class))
                        .map(GoogleSignInAccount::getServerAuthCode)
                        .doOnNext(authCode -> nameValuePairs.put("authCode",authCode))
                        .doOnNext(__ -> nameValuePairs.put("provider","google"))
                        .observeOn(Schedulers.io())
                        .switchMap(__ -> model.GoogleBackendLogin(nameValuePairs))
                        .blockingLast();

                Timber.i(googleLogin.string());

            }catch (Exception e){
                onApiError(e);
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

    //TODO: save validated token.
    //TODO: say which characters can be used; validate user input.
    //TODO: What todo when there is an invalid token?

    private Disposable FacebookLoginTestButtonClickSub(){
        //TODO: check if they are already logged in
        return view.ObservableFacebookLoginTestButton()
                .doOnNext(__ -> view.ShowWebViewContatiner())
                .doOnNext(__ -> model.WebViewSetup(view.getWebView(),view.WebViewContainer))
                .retry()
                .subscribe();
    }

    private boolean sheetIsShown = false;
    private Disposable GotoRegisterButtonClickSub(){
        return view.ObservableGotoRegisterButton()
                .doOnNext(__ -> {
                    if (sheetIsShown){
                        model.setStateBottomSheet(4);
                        sheetIsShown = false;
                    }else {
                        model.setStateBottomSheet(3);
                        sheetIsShown = true;
                    }
                })
                .retry()
                .subscribe();
    }

    private Disposable RegisterButton(){
        return view.ObservableRegisterButton()
                .map(__ -> (new HashMap<String,String>()))
                .doOnNext(pairs -> pairs.put("name",""))
                .doOnNext(pairs -> pairs.put("email",view.getEmail()))
                .doOnNext(pairs -> pairs.put("password",view.getPassword()))
                .doOnNext(pairs -> pairs.put("age",view.getAge()))
                .doOnNext(pairs -> model.checkPairs(pairs))
                .observeOn(Schedulers.io())
                .switchMap(pairs -> model.SIGNUP_USER(pairs))
                .doOnError(this::onConnectionError)
                .retry()
                .subscribe(__ -> model.startHomeActivity());

    }

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

        }else {

        }
    }

}
