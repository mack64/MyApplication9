package dk.subbox.myapplication.activities.login.mvp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.SignInButton;
import com.jakewharton.rxbinding2.InitialValueObservable;
import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.jakewharton.rxbinding2.widget.TextViewTextChangeEvent;

import butterknife.BindView;
import butterknife.ButterKnife;
import dk.subbox.myapplication.R;
import dk.subbox.myapplication.activities.login.LoginActivity;
import dk.subbox.myapplication.ext.SignUser;
import io.reactivex.Observable;

/**
 * Created by mmpa6 on 14-Mar-18.
 */

public class LoginView extends FrameLayout {

    @BindView(R.id.edit_email)
    EditText EditEmail;

    @BindView(R.id.edit_password)
    EditText EditPassword;

    @BindView(R.id.btn_login)
    Button ButtonLogin;

    @BindView(R.id.btn_goto_register)
    Button ButtonGotoRegister;

    @BindView(R.id.google_sign_in_button)
    SignInButton ButtonGoogleLogin;

    @BindView(R.id.facebook_sign_in_button)
    Button ButtonFacebookLogin;

    private final ProgressDialog progressDialog = new ProgressDialog(getContext());

    public static void start(Context context, String... extras){
        Intent intent = new Intent(context, LoginActivity.class);
        intent.putExtra("LoginActivity",extras);
        context.startActivity(intent);
    }

    public LoginView(@NonNull Activity activity) {
        super(activity);
        inflate(activity, R.layout.activity_login,this);

        ButterKnife.bind(this);
        ButtonGoogleLogin.setSize(SignInButton.SIZE_STANDARD);
        ButtonGoogleLogin.setColorScheme(SignInButton.COLOR_DARK);

    }

    public InitialValueObservable<CharSequence> ObservableEmailEdit() {return RxTextView.textChanges(EditEmail);}
    public String getEditEmailText(){return EditEmail.getText().toString();}
    public void setEditEmailError(String error) {EditEmail.setError(error);}

    public Observable<TextViewTextChangeEvent> ObservablePasswordEdit() {return RxTextView.textChangeEvents(EditEmail);}
    public String getEditPasswordText(){return EditPassword.getText().toString();}
    public void setEditPasswordError(String error) {EditPassword.setError(error);}

    public Observable<Object> ObservableLoginButton() {return RxView.clicks(ButtonLogin);}

    public Observable<Object> ObservervableGoogleLoginButton() {return RxView.clicks(ButtonGoogleLogin);}

    public Observable<Object> ObservableFacebookLoginButton() {return RxView.clicks(ButtonFacebookLogin);}

    public Observable<Object> ObservableGotoRegisterButton() {return RxView.clicks(ButtonGotoRegister);}
    public void setSignUpButtonText(String text){
        ButtonGotoRegister.setText(text);
    }

    public void UnsecureConnectionMessage(){
        Toast.makeText(getContext(),"You are on an unsecure connection",Toast.LENGTH_LONG).show();
    }

    public void wrongUsernameOrPasswordToast(){
        Toast.makeText(getContext(),"Wrong username or password!",Toast.LENGTH_LONG).show();
    }



    public SignUser getUser(){
        return SignUser.builder()
                .setUsername(EditEmail.getText().toString())
                .setPassword(EditPassword.getText().toString())
                .build();
    }

    public void showLoading(boolean loading){
        if (loading){
            progressDialog.show();
        }else {
            progressDialog.dismiss();
        }
    }


}
