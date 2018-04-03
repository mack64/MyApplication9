package dk.subbox.myapplication.ext.Login;

import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;


/**
 * Created by mmpa6 on 15-Mar-18.
 */

@AutoValue
public abstract class LoginUser implements Parcelable {

    public static TypeAdapter<LoginUser> typeAdapter(Gson gson){
        return new $AutoValue_LoginUser.GsonTypeAdapter(gson);
    }

    @SerializedName("username")
    public abstract String username();

    @SerializedName("password")
    public abstract String password();

    @SerializedName("device_name")
    public abstract String device_name();

    public static LoginUser create(String username, String password, String device_name){
        return new AutoValue_LoginUser(username,password,device_name);
    }


    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder setUsername(String username);

        public abstract Builder setPassword(String password);

        public abstract Builder setDevice_name(String device_name);

        public abstract LoginUser build();
    }

    @NonNull
    public static Builder builder() {
        return new AutoValue_LoginUser.Builder();
    }

}