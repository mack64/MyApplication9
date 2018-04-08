package dk.subbox.myapplication.app.network;

import dk.subbox.myapplication.ext.Login.LoginUser;
import io.reactivex.Observable;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by mmpa6 on 15-Mar-18.
 */

public interface AuthNetwork {

    //TODO: set form encoded header.
    //TODO: set token bearer token header in php.
    //@FormUrlEncoded
    @POST("SignIn.php")
    Observable<ResponseBody> LOGIN_USER_OBSERVABLE(@Body LoginUser user);

    @POST("API_SignIn.php")
    Observable<ResponseBody> API_LOGIN(@Header("Authorization") String accessToken,@Body String device_name);

}
