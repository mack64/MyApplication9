package dk.subbox.myapplication.activities.signUp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import dk.subbox.myapplication.R;
import dk.subbox.myapplication.activities.Hometest.HomeTestActivity;

public class SignUpActivity extends AppCompatActivity {

    public static void start(Context context){
        Intent intent = new Intent(context,SignUpActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
    }
}
