package nazianoorani.tai;

import android.app.ProgressDialog;
import android.content.Intent;
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
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import butterknife.ButterKnife;
import butterknife.InjectView;
import nazianoorani.tai.util.Constants;
import nazianoorani.tai.util.SnackBarUtil;
import nazianoorani.tai.util.SoftInputCloseUtil;

/**
 * Created by nazianoorani on 13/06/16.
 */
public class ForgotPasswordActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "ForgotPasswordActivity";
    @InjectView(R.id.progressBarDetails)
    ProgressBar mProgressBar;

    @InjectView(R.id.btn_submit)
    Button btnSubmit;
    @InjectView(R.id.et_email)
    EditText etEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        ButterKnife.inject(this);
        btnSubmit.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_submit : changingPasswords();
                break;

        }
    }

    public void changingPasswords(){

        if (!validate()) {
            onResetFailed();
            return;
        }

//        final ProgressDialog progressDialog = new ProgressDialog(ForgotPasswordActivity.this,
//                R.style.Theme_Dialog);
//        progressDialog.setIndeterminate(true);
//        progressDialog.setMessage("Requesting..");
//        progressDialog.getWindow().setGravity(Gravity.BOTTOM);
//        progressDialog.show();
        SoftInputCloseUtil.closeInput(this);
        mProgressBar.setVisibility(View.VISIBLE);
        mProgressBar.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.colorPrimary), android.graphics.PorterDuff.Mode.MULTIPLY);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        String email = etEmail.getText().toString();
//        String password = etCurrentPassword.getText().toString();
//        String newPassword = etNewPassword.getText().toString();
//        Firebase ref = new Firebase("https://tai.firebaseio.com");
//        ref.changePassword(email,password, newPassword, new Firebase.ResultHandler() {
//            @Override
//            public void onSuccess() {
//                // password changed
//                mProgressBar.setVisibility(View.INVISIBLE);
//                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
//                SnackBarUtil.display(ForgotPasswordActivity.this,"Password changed successfully!", Snackbar.LENGTH_LONG);
//                onResetSuccess();
//            }
//            @Override
//            public void onError(FirebaseError firebaseError) {
//                // error encountered
//                mProgressBar.setVisibility(View.INVISIBLE);
//                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
//                Log.e(TAG,firebaseError.getMessage());
//                SnackBarUtil.display(ForgotPasswordActivity.this,firebaseError.getMessage(),Snackbar.LENGTH_LONG);
//            }
//        });



        Firebase.setAndroidContext(getApplicationContext());
        Firebase ref = new Firebase(Constants.BASE_URL);
        ref.resetPassword(email, new Firebase.ResultHandler() {
            @Override
            public void onSuccess() {
                // password reset email sent
                mProgressBar.setVisibility(View.INVISIBLE);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                SnackBarUtil.display(ForgotPasswordActivity.this,"Password will be sent to your registered email!", Snackbar.LENGTH_LONG);
                onResetSuccess();
            }
            @Override
            public void onError(FirebaseError firebaseError) {
                // error encountered

                mProgressBar.setVisibility(View.INVISIBLE);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                Log.e(TAG,firebaseError.getMessage());
                SnackBarUtil.display(ForgotPasswordActivity.this,firebaseError.getMessage(),Snackbar.LENGTH_LONG);
            }
        });


    }

    public boolean validate() {
        boolean valid = true;

        String email = etEmail.getText().toString();
//        String password = etCurrentPassword.getText().toString();
//        String newPassword = etNewPassword.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("enter a valid email address");
            valid = false;
        } else {
            etEmail.setError(null);
        }

//        if (password.isEmpty()  || password.length() < 4 || password.length() > 10) {
//            etCurrentPassword.setError("between 4 and 10 alphanumeric characters");
//            valid = false;
//        } else {
//                etCurrentPassword.setError(null);
//        }
//
//        if (newPassword.isEmpty()  || newPassword.length() < 4 || newPassword.length() > 10) {
//            etNewPassword.setError("between 4 and 10 alphanumeric characters");
//            valid = false;
//        } else {
//            etNewPassword.setError(null);
//        }
        return valid;
    }

    public void onResetFailed() {
        btnSubmit.setEnabled(true);
    }

    public void onResetSuccess() {
        btnSubmit.setEnabled(true);
        finish();
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }
}
