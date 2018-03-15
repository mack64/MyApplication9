package dk.subbox.myapplication.activities.login.mvp;

import android.app.Activity;
import android.os.Build;

import java.util.ArrayList;

import dk.subbox.myapplication.app.network.AuthNetwork;
import dk.subbox.myapplication.ext.LoginUser;
import io.reactivex.Observable;
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

    void attemptLogin(LoginUser.Builder userBuilder){
        LoginUser user;
        try {user = userBuilder.build();}
        catch (IllegalStateException ex){return;}

        Observable<LoginUser> reponseoCall = authNetwork.LOGIN_USER_OBSERVABLE(user);

    }

}
