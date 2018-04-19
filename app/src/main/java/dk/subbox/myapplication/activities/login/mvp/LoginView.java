package dk.subbox.myapplication.activities.login.mvp;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.text.Html;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.common.SignInButton;
import com.jakewharton.rxbinding2.InitialValueObservable;
import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.jakewharton.rxbinding2.widget.TextViewTextChangeEvent;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dk.subbox.myapplication.R;
import dk.subbox.myapplication.activities.login.LoginActivity;
import dk.subbox.myapplication.activities.login.misc.SignUpBottomSheetDialogFragment;
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

    @BindView(R.id.webview_frame)
    FrameLayout WebViewContainer;

    @BindView(R.id.webview)
    WebView webView;

    @BindView(R.id.facebook_login)
    Button facebookLogin;

    //FRAGMENT
    @BindView(R.id.bottom_sheet)
    RelativeLayout layoutBottomSheet;

    @BindView(R.id.btn_register)
    Button ButtonRegister;

    @BindView(R.id.edit_age_register)
    EditText editAge;

    String getAge(){
        return editAge.getText().toString();
    }

    @BindView(R.id.edit_email_register)
    EditText editEmail;

    String getEmail(){
        return editEmail.getText().toString();
    }

    @BindView(R.id.edit_password_register)
    EditText editPassword;

    @BindView(R.id.edit_passwordagain_register)
    EditText editPasswordAgain;

    String getPassword(){
        if (editPassword.getText().toString() ==
                editPasswordAgain.getText().toString()){
            return editPassword.getText().toString();
        }else {
            return null;
        }
    }


    public android.support.v4.app.FragmentManager supportFragmentManager;

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
        //ButterKnife.bind(layoutBottomSheet);
        ButtonGoogleLogin.setSize(SignInButton.SIZE_WIDE);
        ButtonGoogleLogin.setColorScheme(SignInButton.COLOR_DARK);
        setSignUpButtonTextBold();

    }

    public FrameLayout getWebViewContainer(){
        return WebViewContainer;
    }

    public WebView getWebView(){
        return webView;
    }

    public void ShowWebViewContatiner(){
        WebViewContainer.setVisibility(VISIBLE);
        WebViewContainer.bringToFront();
        ButtonGotoRegister.setVisibility(GONE);
    }

    public void HideWebViewContatiner(){
        WebViewContainer.setVisibility(GONE);
        ButtonGotoRegister.setVisibility(VISIBLE);
    }

    public Observable<Object> ObservableFacebookLoginTestButton() {return RxView.clicks(facebookLogin);}

    public InitialValueObservable<CharSequence> ObservableEmailEdit() {return RxTextView.textChanges(EditEmail);}
    public String getEditEmailText(){return EditEmail.getText().toString();}
    public void setEditEmailError(String error) {EditEmail.setError(error);}

    public Observable<TextViewTextChangeEvent> ObservablePasswordEdit() {return RxTextView.textChangeEvents(EditEmail);}
    public String getEditPasswordText(){return EditPassword.getText().toString();}
    public void setEditPasswordError(String error) {EditPassword.setError(error);}

    public Observable<Object> ObservableLoginButton() {return RxView.clicks(ButtonLogin);}

    public Observable<Object> ObservableRegisterButton() {return RxView.clicks(ButtonRegister);}

    public Observable<Object> ObservervableGoogleLoginButton() {return RxView.clicks(ButtonGoogleLogin);}

    public Observable<Object> ObservableGotoRegisterButton() {return RxView.clicks(ButtonGotoRegister);}
    public void setSignUpButtonText(String text){
        ButtonGotoRegister.setText(text);
    }

    public android.support.v4.app.FragmentManager getFragmentSupportManager(){
        return supportFragmentManager;
    }

    public void UnsecureConnectionMessage(){
        Toast.makeText(getContext(),"You are on an unsecure connection",Toast.LENGTH_LONG).show();
    }

    public void wrongUsernameOrPasswordToast(){
        Toast.makeText(getContext(),"Wrong username or password!",Toast.LENGTH_LONG).show();
    }

    private void setSignUpButtonTextBold(){
        String text = "Dont have an account? <b>Sign up</b>";
        setSignUpButtonText(Html.fromHtml(text).toString());
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

    public void showBottomSheetDialog(Activity activity, int layoutId) {
        View view = activity.getLayoutInflater().inflate(layoutId, null);

        BottomSheetDialog dialog = new BottomSheetDialog(activity);
        dialog.setContentView(view);
        dialog.show();
    }

    /**
     * showing bottom sheet dialog fragment
     * same layout is used in both dialog and dialog fragment
     */

    public void showBottomSheetDialogFragment(LoginActivity activity, int layoutId) {
        SignUpBottomSheetDialogFragment bottomSheetFragment = new SignUpBottomSheetDialogFragment();
        bottomSheetFragment.show(activity.getSupportFragmentManager() , bottomSheetFragment.getTag());
    }

}
