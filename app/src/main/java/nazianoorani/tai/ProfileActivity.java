package nazianoorani.tai;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import nazianoorani.tai.dto.ProfileDto;
import nazianoorani.tai.networkmanager.AppController;
import nazianoorani.tai.util.Constants;
import nazianoorani.tai.util.NetworkUtil;
import nazianoorani.tai.util.SnackBarUtil;

/**
 * Created by nazianoorani on 13/06/16.
 */
public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    @InjectView(R.id.progressBar)
    ProgressBar mProgressBar;
    @InjectView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    @InjectView(R.id.user_name)
    TextView tvUserName;
    @InjectView(R.id.user_mail)
    TextView tvUserMail;
    @InjectView(R.id.tv_contact)
    TextView tvContact;
    @InjectView(R.id.tv_about)
    TextView tvAbout;
    @InjectView(R.id.btn_edit_profile)
    Button btnEditProfileImg;
    @InjectView(R.id.img_profile)
    ImageView imgProfile;
    @InjectView(R.id.tv_edit_about)
    TextView tvEditAbout;
    @InjectView(R.id.tv_edit_contact)
    TextView tvEditContact;
    @InjectView(R.id.btn_chng_pswd)
    Button btnChangePsswd;
    private static final int SELECT_PICTURE = 1;
    private String selectedImagePath;
    ProfileDto dto = new ProfileDto();
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.inject(this);
        setSupportActionBar(toolbar);
        btnEditProfileImg.setOnClickListener(this);
        btnChangePsswd.setOnClickListener(this);
        tvEditAbout.setOnClickListener(this);
        tvEditContact.setOnClickListener(this);
        collapsingToolbarLayout.setTitle("Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getProfile();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:
                onBackPressed();
                break;

        }
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_edit_profile:
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,
                        "Select Picture"), SELECT_PICTURE);
                break;
            case R.id.tv_edit_about:
                showEditDialog(R.layout.dialog_profie_edit);
                break;
            case R.id.tv_edit_contact:
                showEditDialog(R.layout.dialog_contact_edit);
                break;
            case R.id.btn_chng_pswd :
                showChngPsswdDialog();
                break;

        }
    }

    void showChngPsswdDialog(){
        MaterialDialog dialog = new MaterialDialog.Builder(this)
                .title("Edit Profile")
                .customView(R.layout.dialog_change_passwd, false)
                .positiveText("Submit")
                .backgroundColor(getResources().getColor(R.color.colorPrimary))
                .positiveColor(getResources().getColor(R.color.orange))
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                        mProgressBar.setVisibility(View.VISIBLE);
                        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        EditText etCurrentPasswd = (EditText) dialog.getView().findViewById(R.id.et_edit_current_psswd);
                        EditText etNewPasswd = (EditText) dialog.getView().findViewById(R.id.et_edit_current_psswd);


                        String current = etCurrentPasswd.getText().toString();
                        String newPassword = etNewPasswd.getText().toString();
                        if(valid(etNewPasswd,etCurrentPasswd)){
                            Firebase ref = new Firebase(Constants.BASE_URL);
                            ref.changePassword(dto.getEmail(), current, newPassword, new Firebase.ResultHandler() {
                                @Override
                                public void onSuccess() {
                                    // password changed
                                    mProgressBar.setVisibility(View.INVISIBLE);
                                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                    SnackBarUtil.display(ProfileActivity.this,"Password Changed Successfully!",Snackbar.LENGTH_LONG);
                                }
                                @Override
                                public void onError(FirebaseError firebaseError) {
                                    // error encountered
                                    mProgressBar.setVisibility(View.INVISIBLE);
                                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                    SnackBarUtil.display(ProfileActivity.this,firebaseError.getMessage(),Snackbar.LENGTH_LONG);
                                }
                            });
                        }else {
                            SnackBarUtil.display(ProfileActivity.this,"Please enter valid password!", Snackbar.LENGTH_LONG);
                        }

                    }
                }).show();

    }

    boolean valid(EditText etNewPasswd, EditText etCurrentPasswd){
        boolean valid = true;
        String current = etCurrentPasswd.getText().toString();
        String newPassword = etNewPasswd.getText().toString();

        if (newPassword.isEmpty() || newPassword.length() < 4 || newPassword.length() > 10) {
            etNewPasswd.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            etNewPasswd.setError(null);
        }



        if (current.isEmpty() || current.length() < 4 || current.length() > 10) {
            etCurrentPasswd.setError("enter correct password");
            valid = false;
        } else {
            etCurrentPasswd.setError(null);
        }
        return valid;

    }




    private void showEditDialog(final int layout) {

        final String email = tvUserMail.getText().toString();
        final String name = dto.getName();
        String contact = tvContact.getText().toString();
        String school = dto.getSchoolName();
        String standard = dto.getStandard();


        MaterialDialog dialog = new MaterialDialog.Builder(this)
                .title("Edit Profile")
                .customView(layout, false)
                .positiveText("Submit")
                .backgroundColor(getResources().getColor(R.color.colorPrimary))
                .positiveColor(getResources().getColor(R.color.orange))
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        Firebase.setAndroidContext(getApplicationContext());
                        final Firebase ref = new Firebase(Constants.BASE_URL);
                        String contact = tvContact.getText().toString();
                        String school = dto.getSchoolName();
                        String standard = dto.getStandard();
                        if (layout == R.layout.dialog_contact_edit) {
                            EditText etContact = (EditText) dialog.getView().findViewById(R.id.et_edit_contact);
                            if (etContact.getText().toString() != null) {
                                contact = etContact.getText().toString();
                            }

                        } else if (layout == R.layout.dialog_profie_edit) {
                            EditText etClass = (EditText) dialog.getView().findViewById(R.id.et_edit_class);
                            EditText etSchool = (EditText) dialog.getView().findViewById(R.id.et_edit_school);
                            if (!etClass.getText().toString().equals("")) {
                                standard = etClass.getText().toString();
                            }
                            if (!etSchool.getText().toString().equals("")) {
                                school = etSchool.getText().toString();
                            }
                        }


                        String email1;
                        email1 = email.replace('.', '@');
                        Firebase usersRef = ref.child("users").child(email1);
                        Map<String, String> user = new HashMap<String, String>();
                        user.put("class", standard);
                        user.put("school-name", school);
                        user.put("contact", contact);
                        user.put("name", name);
                        user.put("email", email);


                        Map<String, Map<String, String>> users = new HashMap<String, Map<String, String>>();
                        users.put(email1, user);
                        usersRef.setValue(users);
                        getProfile();
                        SnackBarUtil.display(ProfileActivity.this, "Done!!", Snackbar.LENGTH_LONG);

                    }
                })
                .show();

    }

    void getProfile() {
        SharedPreferences prefs = getSharedPreferences(Constants.USER_MAIL_PREF, MODE_PRIVATE);
        String restoredEmail = prefs.getString("email", "");
        tvUserMail.setText(restoredEmail);
        if (restoredEmail != null) {

            String email = restoredEmail.replace('.', '@');
            if (NetworkUtil.isNetworkAvailable(this)) {
                mProgressBar.setVisibility(View.VISIBLE);
                String PROFILE_URL = Constants.BASE_URL + "users/" + email + "/" + email + ".json";
                JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, PROFILE_URL, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {


                            if (response.has("class")) {
                                dto.setStandard(response.getString("class"));
                            }
                            if (response.has("contact")) {
                                dto.setContact(response.getString("contact"));
                                tvContact.setText(dto.getContact());

                            }
                            if (response.has("name")) {
                                dto.setName(response.getString("name"));
                                tvUserName.setText(dto.getName());
                            }
                            if (response.has("school-name")) {
                                dto.setSchoolName(response.getString("school-name"));
                            }
                            if (response.has("email")) {
                                dto.setEmail(response.getString("email"));
                            }
                            tvAbout.setText("I'm Studying in class " + dto.getStandard() + " in " + dto.getSchoolName() + ".");

                            mProgressBar.setVisibility(View.INVISIBLE);
                            collapsingToolbarLayout.setTitle(dto.getName() + "'s Profile");
                        } catch (JSONException e) {
                            e.printStackTrace();
                            mProgressBar.setVisibility(View.INVISIBLE);
                            SnackBarUtil.display(ProfileActivity.this, "Error!", Snackbar.LENGTH_LONG);

                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        mProgressBar.setVisibility(View.INVISIBLE);
                        SnackBarUtil.display(ProfileActivity.this, "Currently Unavailable !", Snackbar.LENGTH_LONG);
                    }
                });
                AppController.getInstance().addToRequestQueue(request);
            } else {
                SnackBarUtil.display(this, getString(R.string.no_internet), Snackbar.LENGTH_LONG);
                return;
            }

        } else {
            SnackBarUtil.display(this, "Not registered!", Snackbar.LENGTH_LONG);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                Uri selectedImageUri = data.getData();



                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                Cursor cursor = getContentResolver().query(selectedImageUri,
                        filePathColumn, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath = cursor.getString(columnIndex);
                cursor.close();
                changeImg(selectedImageUri,picturePath);

            }
        }

    }

    private void changeImg(Uri selectedImgUri, String picturePath) {
        try {
            Bitmap bm = getBitmapFromUri(selectedImgUri);
            imgProfile.setBackgroundResource(android.R.color.transparent);
            imgProfile.setImageBitmap(bm);
            uploadProfileImage(picturePath);
        } catch (IOException e) {
            e.printStackTrace();
            SnackBarUtil.display(this, "Unable to upload", Snackbar.LENGTH_LONG);
        }
    }

    private void uploadProfileImage(String imgPath) {
        Map config = getConfig();
        Cloudinary cloudinary = new Cloudinary(config);
//        ByteArrayOutputStream bos = new ByteArrayOutputStream();
//        bm.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
//        byte[] bitmapdata = bos.toByteArray();
//        ByteArrayInputStream bs = new ByteArrayInputStream(bitmapdata);
        File file = new File(imgPath);

        try {
            Map uploadResult =   cloudinary.uploader().upload(file, ObjectUtils.asMap("public_id", "profileimg"));
            Log.i("IMAGE UPLOAD",uploadResult.get("url").toString() );
        } catch (IOException e) {
            e.printStackTrace();
            SnackBarUtil.display(this, "Failed to upload!", Snackbar.LENGTH_LONG);
        }

    }

    private Bitmap getBitmapFromUri(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor =
                getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }

    public Map getConfig() {
        Map config = new HashMap();
        config.put("cloud_name", "db3zqauw7");
        config.put("api_key", "279837598375121");
        config.put("api_secret", "QK4hM8SDSeUmUxWsyRzDKTlrTyI");
        return config;

    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Profile Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://nazianoorani.tai/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Profile Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://nazianoorani.tai/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}
