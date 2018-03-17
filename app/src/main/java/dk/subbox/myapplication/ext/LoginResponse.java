package dk.subbox.myapplication.ext;

import android.support.annotation.NonNull;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

/**
 * Created by mmpa6 on 17-Mar-18.
 */

@AutoValue
public abstract class LoginResponse {

    public static TypeAdapter<LoginResponse> typeAdapter(Gson gson){
        return new AutoValue_LoginResponse.GsonTypeAdapter(gson);
    }

    @SerializedName("iss")
    public abstract String iss();

    @SerializedName("uid")
    public abstract String uid();

    @SerializedName("aud")
    public abstract String aud();

    @SerializedName("iat")
    public abstract int iat();

    @SerializedName("nbf")
    public abstract int nbf();

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract LoginResponse.Builder setIss(String iss);

        public abstract LoginResponse.Builder setUid(String uid);

        public abstract LoginResponse.Builder setAud(String aud);

        public abstract LoginResponse.Builder setIat(int iat);

        public abstract LoginResponse.Builder setNbf(int nbf);

        public abstract LoginResponse build();
    }

    @NonNull
    public static LoginResponse.Builder builder() {
        return new AutoValue_LoginResponse.Builder();
    }


}
