package dk.subbox.myapplication.app.dagger.module;

import android.app.Activity;
import android.content.Context;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

import dagger.Module;
import dagger.Provides;
import dk.subbox.myapplication.activities.login.dagger.LoginScope;
import dk.subbox.myapplication.app.dagger.AppScope;

/**
 * Created by mmpa6 on 26-Mar-18.
 */
@Module
public class ApiModule {

    @Provides
    @AppScope
    GoogleSignInOptions googleSignInOptions(){
        return new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestProfile()
                .requestIdToken("28951304053-f840sn63j4qlf55id6j3lq7qlk4bts4k.apps.googleusercontent.com")
                .build();
    }

    @Provides
    @AppScope
    GoogleSignInClient googleSignInClient(GoogleSignInOptions googleSignInOptions, Context context){
        return GoogleSignIn.getClient(context,googleSignInOptions);
    }

    @Provides
    @AppScope
    GoogleSignInAccount googleSignInAccount(Context context){
        return GoogleSignIn.getLastSignedInAccount(context);
    }
}
