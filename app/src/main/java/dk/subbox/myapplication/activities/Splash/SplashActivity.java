package dk.subbox.myapplication.activities.Splash;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import dk.subbox.myapplication.activities.login.LoginActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //TODO: Do they have a valid token? : yes -> goto HomeActivity | no -> goto loginActivity.

        startActivity(new Intent(this, LoginActivity.class));
        finish();

    }
}
