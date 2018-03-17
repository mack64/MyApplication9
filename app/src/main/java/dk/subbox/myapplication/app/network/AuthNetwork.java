package dk.subbox.myapplication.app.network;

import dk.subbox.myapplication.ext.LoginResponse;
import dk.subbox.myapplication.ext.LoginUser;
import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by mmpa6 on 15-Mar-18.
 */

public interface AuthNetwork {

    @POST("SignIn.php")
    Observable<Response<LoginResponse>> LOGIN_USER_OBSERVABLE(@Body LoginUser user);

}
