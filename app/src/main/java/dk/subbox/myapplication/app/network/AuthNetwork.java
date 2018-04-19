package dk.subbox.myapplication.app.network;

import android.provider.Settings;

import java.util.List;
import java.util.Map;

import dk.subbox.myapplication.ext.Login.LoginUser;
import io.reactivex.Observable;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
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

    @FormUrlEncoded
    @POST("SignUp.php")
    Observable<ResponseBody> SIGNUP_USER_OBSERVABLE(@FieldMap Map<String,String> nameValuePairs);

    @POST("API_SignIn.php")
    Observable<ResponseBody> API_LOGIN(@Header("Authorization") String accessToken,@Body String device_name);

    @Headers("X-Requested-With:XMLHttpRequest")
    @FormUrlEncoded
    @POST("authCode.php")
    Observable<ResponseBody> API_BACKEND_LOGIN(@FieldMap Map<String,String> nameValuePairs);

}
