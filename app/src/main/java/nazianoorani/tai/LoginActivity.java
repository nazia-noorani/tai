package nazianoorani.tai;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import butterknife.ButterKnife;
import butterknife.InjectView;
import nazianoorani.tai.util.Constants;
import nazianoorani.tai.util.NetworkUtil;
import nazianoorani.tai.util.SnackBarUtil;
import nazianoorani.tai.util.SoftInputCloseUtil;

/**
 * Created by nazianoorani on 12/06/16.
 */

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "LoginActivity";
//    private static final int REQUEST_SIGNUP = 0;

    @InjectView(R.id.progressBar)
    ProgressBar mProgressBar;
    @InjectView(R.id.input_email) EditText etEmail;
    @InjectView(R.id.input_password) EditText etPassword;
    @InjectView(R.id.btn_login) Button btnLogin;
    @InjectView(R.id.tv_forgot_password) TextView tvForgotPassword;
    @InjectView(R.id.tv_link_signup) TextView tvSignup;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.inject(this);
        btnLogin.setOnClickListener(this);
        tvForgotPassword.setOnClickListener(this);
        tvSignup.setOnClickListener(this);



//        _signupLink.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                // Start the Signup activity
////                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
////                startActivityForResult(intent, REQUEST_SIGNUP);
//            }
//        });
    }

    public void login() {
        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed();
            return;
        }

        btnLogin.setEnabled(false);
        SoftInputCloseUtil.closeInput(this);
        mProgressBar.setVisibility(View.VISIBLE);
        mProgressBar.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.colorPrimary), android.graphics.PorterDuff.Mode.MULTIPLY);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);


        final String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();

        if(NetworkUtil.isNetworkAvailable(this)) {

            Firebase ref = new Firebase("https://tai.firebaseio.com");
            ref.authWithPassword(email, password, new Firebase.AuthResultHandler() {
                @Override
                public void onAuthenticated(AuthData authData) {
                    mProgressBar.setVisibility(View.INVISIBLE);
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    SharedPreferences.Editor editor = getSharedPreferences(Constants.USER_MAIL_PREF, MODE_PRIVATE).edit();
                    editor.putString("email", email);
                    editor.commit();
                    System.out.println("User ID: " + authData.getUid() + ", Provider: " + authData.getProvider());
                    onLoginSuccess();
                }

                @Override
                public void onAuthenticationError(FirebaseError firebaseError) {
                    // there was an error
                    mProgressBar.setVisibility(View.INVISIBLE);
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    Log.e("faied to register", firebaseError.getMessage());
                    SnackBarUtil.display(LoginActivity.this,firebaseError.getMessage(),Snackbar.LENGTH_LONG);
                    onLoginFailed();
                }
            });

        }
        else{
            SnackBarUtil.display(this,getString(R.string.no_internet),Snackbar.LENGTH_LONG);
            return;
        }
    }


//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == REQUEST_SIGNUP) {
//            if (resultCode == RESULT_OK) {
//
//                // TODO: Implement successful signup logic here
//                // By default we just finish the Activity and log them in automatically
//                this.finish();
//            }
//        }
//    }

//    @Override
//    public void onBackPressed() {
//        // disable going back to the MainActivity
//        moveTaskToBack(true);
//    }

    public void onLoginSuccess() {
        btnLogin.setEnabled(true);
        SnackBarUtil.display(getBaseContext(),"Logged In", Snackbar.LENGTH_LONG);
        finish();
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }

    public void onLoginFailed() {
        btnLogin.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("enter a valid email address");
            valid = false;
        } else {
            etEmail.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            etPassword.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            etPassword.setError(null);
        }

        return valid;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_login :  login();
                break;
            case R.id.tv_forgot_password :
                finish();
                Intent intent = new Intent(this,ForgotPasswordActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_link_signup : finish();
                Intent intentSignup = new Intent(this,SignUpActivity.class);
                startActivity(intentSignup);
                break;


        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }
}