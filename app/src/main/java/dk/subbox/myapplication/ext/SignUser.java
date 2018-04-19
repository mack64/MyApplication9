package dk.subbox.myapplication.ext;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.gson.annotations.SerializedName;

import java.sql.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by mmpa6 on 14-Mar-18.
 */
@AutoValue
public abstract class SignUser {

    @Nullable
    @SerializedName("username")
    abstract String username();

    @SerializedName("password")
    abstract String password();

    @Nullable
    @SerializedName("email")
    abstract String email();

    @Nullable
    @SerializedName("device_name")
    abstract String device_name();

    @Nullable
    @SerializedName("first_name")
    abstract String first_name();

    @Nullable
    @SerializedName("last_name")
    abstract String last_name();

    @Nullable
    @SerializedName("birth_date")
    abstract Date birth_date();

    @Nullable
    @SerializedName("addresse")
    abstract String addresse();

    @Nullable
    @SerializedName("zip_code")
    abstract String zip_code();

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder setUsername(String username);

        public abstract Builder setPassword(String password);

        public abstract Builder setEmail(String email);

        public abstract Builder setDevice_name(String device_name);

        public abstract Builder setFirst_name(String first_name);

        public abstract Builder setLast_name(String last_name);

        public abstract Builder setBirth_date(Date birth_date);

        public abstract Builder setAddresse(String addresse);

        public abstract Builder setZip_code(String zip_code);

        public abstract SignUser build();
    }

    @NonNull
    public static Builder builder() {
        return new AutoValue_SignUser.Builder();
    }
}