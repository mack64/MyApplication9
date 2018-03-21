package dk.subbox.myapplication.activities.login.mvp;

import android.content.res.Resources;

import javax.net.ssl.SSLException;
import javax.net.ssl.SSLPeerUnverifiedException;

import dk.subbox.myapplication.R;
import dk.subbox.myapplication.ext.LoginUser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.PrematureJwtException;
import io.jsonwebtoken.SignatureException;
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

        compositeDisposable.add(LoginButtonClickSub());
    }

    public void onDestroy(){
        compositeDisposable.clear();
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
}
