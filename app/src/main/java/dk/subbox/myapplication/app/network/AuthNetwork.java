package dk.subbox.myapplication.app.network;

import dk.subbox.myapplication.ext.LoginUser;
import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by mmpa6 on 15-Mar-18.
 */

public interface AuthNetwork {

    @POST("login.php")
    Observable<LoginUser> LOGIN_USER_OBSERVABLE(@Body LoginUser user);

}
