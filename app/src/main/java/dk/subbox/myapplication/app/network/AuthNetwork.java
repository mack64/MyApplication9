package dk.subbox.myapplication.app.network;

import dk.subbox.myapplication.ext.LoginResponse;
import dk.subbox.myapplication.ext.LoginUser;
import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by mmpa6 on 15-Mar-18.
 */

public interface AuthNetwork {

    //@FormUrlEncoded
    @POST("SignIn.php")
    Observable<ResponseBody> LOGIN_USER_OBSERVABLE(@Body LoginUser user);

}
