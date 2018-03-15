package dk.subbox.myapplication.activities.login;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import javax.inject.Inject;

import dk.subbox.myapplication.activities.login.dagger.DaggerLoginComponent;
import dk.subbox.myapplication.activities.login.dagger.LoginModule;
import dk.subbox.myapplication.activities.login.mvp.LoginPresenter;
import dk.subbox.myapplication.activities.login.mvp.LoginView;
import dk.subbox.myapplication.app.network.AuthApplication;
import dk.subbox.myapplication.app.network.AuthNetwork;

public class LoginActivity extends AppCompatActivity {

    @Inject
    LoginView view;

    @Inject
    LoginPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DaggerLoginComponent.builder()
                .appComponent(AuthApplication.get(this).component())
                .loginModule(new LoginModule(this))
                .build().injectLoginActivity(this);

        setContentView(view);

        presenter.onCreate();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }
}
