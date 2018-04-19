package dk.subbox.myapplication.activities.login;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import javax.inject.Inject;

import dk.subbox.myapplication.activities.login.dagger.DaggerLoginComponent;
import dk.subbox.myapplication.activities.login.dagger.LoginModule;
import dk.subbox.myapplication.activities.login.mvp.LoginPresenter;
import dk.subbox.myapplication.activities.login.mvp.LoginView;
import dk.subbox.myapplication.app.network.AuthApplication;

public class LoginActivity extends FragmentActivity {

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
        view.supportFragmentManager = getSupportFragmentManager();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        presenter.onActivityResult(requestCode,resultCode,data);
    }

    @Override
    public FragmentManager getSupportFragmentManager() {
        return super.getSupportFragmentManager();
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
    }
}
