package nazianoorani.tai;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import nazianoorani.tai.util.Constants;
import nazianoorani.tai.util.NetworkUtil;
import nazianoorani.tai.util.SnackBarUtil;
import nazianoorani.tai.util.SoftInputCloseUtil;

/**
 * Created by nazianoorani on 13/06/16.
 */
public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "SignupActivity";
    @InjectView(R.id.progressBarDetails)
    ProgressBar mProgressBar;
    @InjectView(R.id.et_name)
    EditText etName;
    @InjectView(R.id.et_email)
    EditText etEmail;
    @InjectView(R.id.et_password)
    EditText etPassword;
    @InjectView(R.id.et_phone)
    EditText etPhone;
    @InjectView(R.id.et_school)
    EditText etSchool;
    @InjectView(R.id.et_class)
    EditText etClass;
    @InjectView(R.id.btn_signup)
    Button btn_signup;
    @InjectView(R.id.link_login)
    TextView tvLinkLogin;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.inject(this);
        btn_signup.setOnClickListener(this);
        tvLinkLogin.setOnClickListener(this);

    }

    public void signup() {
        Log.d(TAG, "Signup");

        if (!validate()) {
            onSignupFailed();
            return;
        }

        btn_signup.setEnabled(false);
        SoftInputCloseUtil.closeInput(this);
        mProgressBar.setVisibility(View.VISIBLE);
        mProgressBar.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.colorPrimary), android.graphics.PorterDuff.Mode.MULTIPLY);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        if(NetworkUtil.isNetworkAvailable(this)) {
            final String name = etName.getText().toString();
            final String email = etEmail.getText().toString();
            String password = etPassword.getText().toString();
            final String phone = etPhone.getText().toString();
            final String school = etSchool.getText().toString();
            final String standard = etClass.getText().toString();

            Firebase.setAndroidContext(getApplicationContext());
            final Firebase ref = new Firebase(Constants.BASE_URL);

            ref.createUser(email, password, new Firebase.ValueResultHandler<Map<String, Object>>() {
                @Override
                public void onSuccess(Map<String, Object> result) {
                    mProgressBar.setVisibility(View.INVISIBLE);
                    SharedPreferences.Editor editor = getSharedPreferences("UserMail", MODE_PRIVATE).edit();
                    editor.putString("email", email);
                    editor.commit();
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    System.out.println("Successfully created user account with uid: " + result.get("uid"));
                    onSignupSuccess(ref, name, email, phone, standard, school);

                }

                @Override
                public void onError(FirebaseError firebaseError) {
                    // there was an error
                    mProgressBar.setVisibility(View.INVISIBLE);
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    SnackBarUtil.display(SignUpActivity.this, firebaseError.getMessage(), Snackbar.LENGTH_LONG);
                    onSignupFailed();
                }
            });

//        new android.os.Handler().postDelayed(
//                new Runnable() {
//                    public void run() {
//                        // On complete call either onSignupSuccess or onSignupFailed
//                        // depending on success
////                        onSignupSuccess();
//                        // onSignupFailed();
//                        progressDialog.dismiss();
//                    }
//                }, 3000);
        } else {
            SnackBarUtil.display(this,getString(R.string.no_internet),Snackbar.LENGTH_LONG);
            onSignupFailed();
        }
    }


    public void onSignupSuccess(Firebase ref,String name , String email ,String phone , String standard , String school) {
//        Firebase usersRef = ref.child("users");
//        Map<String, String> user = new HashMap<String, String>();
//        user.put("name", name);
//        user.put("email", email);
//        user.put("class", standard);
//        user.put("school-name", school);
//        user.put("contact",phone);
//        Map<String, Map<String, String>> users = new HashMap<String, Map<String, String>>();
//        users.put(email, user);
//        usersRef.setValue(users);

//        Firebase usersRef = ref.child("users");
//        Map<String, String> user = new HashMap<String, String>();
//        user.put("name", name);
//        user.put("email", email);
//        user.put("class", standard);
//        user.put("school-name", school);
//        user.put("contact", phone);
//
//        Map<String, Map<String, String>> users = new HashMap<String, Map<String, String>>();
//        String email1 ;
//        email1 = email.replace('.', '@');
//        users.put(email1, user);
//        usersRef.setValue(users);
//        btn_signup.setEnabled(true);
//        setResult(RESULT_OK, null);

        String email1 ;
        email1 = email.replace('.', '@');
        Firebase usersRef = ref.child("users").child(email1);
        Map<String, String> user = new HashMap<String, String>();
        user.put("class", standard);
        user.put("school-name", school);
        user.put("contact", phone);
        user.put("name", name);
        user.put("email", email);

        Map<String, Map<String, String>> users = new HashMap<String, Map<String, String>>();
        users.put(email1, user);
        usersRef.setValue(users);
        SnackBarUtil.display(this, "Successfuly signed up!", Snackbar.LENGTH_LONG);
        finish();

        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }

    public void onSignupFailed() {

        btn_signup.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String name = etName.getText().toString();
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();
        String phone = etPhone.getText().toString();
        String school = etSchool.getText().toString();
        String standard = etClass.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            etName.setError("at least 3 characters");
            valid = false;
        } else {
            etName.setError(null);
        }

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

        if (phone.isEmpty() || phone.length() == 9) {
            etPhone.setError("Enter valid phone number");
            valid = false;
        } else {
            etPhone.setError(null);
        }
        if (standard.isEmpty()) {
            etClass.setError("required field");
            valid = false;
        } else {
            etClass.setError(null);
        }
        if (school.isEmpty()) {
            etSchool.setError("required field");
            valid = false;
        } else {
            etSchool.setError(null);
        }
        return valid;
    }
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btn_signup : signup();
                break;
            case R.id.link_login :
                finish();
                Intent intent = new Intent(this,LoginActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }
}
