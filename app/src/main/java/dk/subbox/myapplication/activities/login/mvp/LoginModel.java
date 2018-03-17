package dk.subbox.myapplication.activities.login.mvp;

import android.app.Activity;
import android.os.Build;

import java.util.ArrayList;
import java.util.concurrent.TimeoutException;

import dk.subbox.myapplication.app.network.AuthNetwork;
import dk.subbox.myapplication.ext.LoginResponse;
import dk.subbox.myapplication.ext.LoginUser;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import retrofit2.Response;
import timber.log.Timber;

/**
 * Created by mmpa6 on 14-Mar-18.
 */

public class LoginModel {

    private final Activity activity;
    private final AuthNetwork authNetwork;

    public LoginModel(Activity activity, AuthNetwork authNetwork){
        this.activity = activity;
        this.authNetwork = authNetwork;
    }

    void startHomeActivity(){

    }

    String getDeviceName(){
        return Build.DEVICE;
    }

    Observable<Response<LoginResponse>> attemptLogin(LoginUser.Builder userBuilder){
        LoginUser user;
        try {user = userBuilder.build();}
        catch (IllegalStateException ex){return null;}

        Observable<Response<LoginResponse>> responseoCall = authNetwork.LOGIN_USER_OBSERVABLE(user);

        return responseoCall;
    }

    /*void onErrorResponse(Flowable<Throwable> errors){

        errors.flatMap(error -> {
            if (error instanceof TimeoutException){
                return Observable.just(null);
            }
                return Observable.error(error);
            }
        );*/


}
