package dk.subbox.myapplication.activities.login.dagger;

import android.app.Activity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

import dagger.Module;
import dagger.Provides;
import dk.subbox.myapplication.activities.login.dagger.LoginScope;
import dk.subbox.myapplication.activities.login.mvp.LoginModel;
import dk.subbox.myapplication.activities.login.mvp.LoginPresenter;
import dk.subbox.myapplication.activities.login.mvp.LoginView;
import dk.subbox.myapplication.app.dagger.module.ApiModule;
import dk.subbox.myapplication.app.network.AuthNetwork;

@Module(includes = ApiModule.class)
public class LoginModule {

    private final Activity activity;


    public LoginModule(Activity activity) {
        this.activity = activity;
    }

    @Provides
    @LoginScope
    public LoginView view(){
        return new LoginView(activity);
    }

    @Provides
    @LoginScope
    public LoginPresenter presenter(LoginView view, LoginModel model){
        return new LoginPresenter(view,model);
    }

    @Provides
    @LoginScope
    public LoginModel model(AuthNetwork authNetwork, GoogleSignInClient googleSignInClient){
        return new LoginModel(activity,authNetwork,googleSignInClient);
    }


}
