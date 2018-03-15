package dk.subbox.myapplication.activities.login.mvp;

import android.util.Log;

import java.util.concurrent.ExecutionException;

import dk.subbox.myapplication.ext.LoginUser;
import dk.subbox.myapplication.ext.SignUser;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by mmpa6 on 14-Mar-18.
 */

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
                        user.setUsername(view.getEditEmailText());})
                .doOnNext(user -> {
                    if (view.getEditPasswordText().isEmpty())
                        view.setEditPasswordError("This field cannot be empty");
                    else
                        user.setPassword(view.getEditPasswordText());})
                .observeOn(Schedulers.io())
                .doOnNext(user -> user.setDevice_name(model.getDeviceName()))
                .doOnNext(user -> model.attemptLogin(user))
                .retry()
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(__ -> view.showLoading(false))
                .subscribe();
    }

}
