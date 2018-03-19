package dk.subbox.myapplication.activities.login.mvp;

import android.app.Activity;
import android.os.Build;
import android.util.Log;

import dk.subbox.myapplication.activities.Hometest.HomeTestActivity;
import dk.subbox.myapplication.app.network.AuthNetwork;
import dk.subbox.myapplication.ext.LoginResponse;
import dk.subbox.myapplication.ext.LoginUser;
import io.reactivex.Observable;
import okhttp3.ResponseBody;
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
        HomeTestActivity.start(activity);
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

        Observable<ResponseBody> reposne = authNetwork.LOGIN_USER_OBSERVABLE(user);
        return reposne;
    }


}
