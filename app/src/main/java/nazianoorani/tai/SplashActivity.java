package nazianoorani.tai;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;

import nazianoorani.tai.util.Constants;

/**
 * Created by nazianoorani on 15/06/16.
 */
public class SplashActivity extends AppCompatActivity {
    // Splash screen timer
    private static int SPLASH_TIME_OUT = 3000;
    Intent intent;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Firebase ref = new Firebase(Constants.BASE_URL);
        AuthData authData = ref.getAuth();
        if( authData != null){
            intent = new Intent(this,MainActivity.class);

        }else {
            intent = new Intent(this,StartUpActivity.class);
        }
        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity


                startActivity(intent);

                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);
    }

}
