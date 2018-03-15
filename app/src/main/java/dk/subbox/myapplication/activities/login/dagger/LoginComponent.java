package dk.subbox.myapplication.activities.login.dagger;

import dagger.Component;
import dk.subbox.myapplication.activities.login.LoginActivity;
import dk.subbox.myapplication.app.dagger.AppComponent;
import dk.subbox.myapplication.app.dagger.module.NetworkModule;

/**
 * Created by mmpa6 on 14-Mar-18.
 */
@LoginScope
@Component(modules = {LoginModule.class},dependencies = AppComponent.class)
public interface LoginComponent {

    void injectLoginActivity(LoginActivity loginActivity);

}
