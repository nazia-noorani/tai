package nazianoorani.tai;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.andraskindler.parallaxviewpager.ParallaxViewPager;
import com.firebase.client.AuthData;
import com.firebase.client.Firebase;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.ButterKnife;
import butterknife.InjectView;
import nazianoorani.tai.adapters.SlidingImageAdapter;
import nazianoorani.tai.ui.CirclePageIndicator;
import nazianoorani.tai.util.Constants;

/**
 * Created by nazianoorani on 12/06/16.
 */



public class StartUpActivity extends AppCompatActivity{
    @InjectView(R.id.btnLogin)
    Button btnLogin;
    @InjectView(R.id.btnRegister)
    Button btnRegister;
    @InjectView(R.id.pager)
    ParallaxViewPager mViewPager;
    @InjectView(R.id.indicator)
    CirclePageIndicator indicator;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    private  final int pageSize = 4;
    private ArrayList<Integer> ImagesArray = new ArrayList<Integer>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startup);

        ImagesArray.add(R.mipmap.img_startup_1);
        ImagesArray.add(R.mipmap.img_startup_2);
        ImagesArray.add(R.mipmap.img_startup_3);
        ImagesArray.add(R.mipmap.img_startup_4);
        populate();

    }


    private void populate(){
        ButterKnife.inject(this);
        init(indicator,mViewPager);

        mViewPager.setAdapter(new SlidingImageAdapter(this,ImagesArray));
        mViewPager.setBackgroundResource(R.mipmap.ic_launcher);
        mViewPager.setScaleType(ParallaxViewPager.FIT_WIDTH);
        mViewPager.setOverlapPercentage(0.147f);
        if(android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            btnRegister.setBackgroundResource(R.drawable.ripple);
            btnLogin.setBackgroundResource(R.drawable.ripple);

        }



        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StartUpActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                  Intent intent = new Intent(StartUpActivity.this, SignUpActivity.class);
                   startActivity(intent);
                  finish();
            }
        });
    }


    private void init(CirclePageIndicator indicator, final ViewPager viewPager) {

        viewPager.setAdapter(new SlidingImageAdapter(this,ImagesArray));
        indicator.setViewPager(viewPager);
        final float density = getResources().getDisplayMetrics().density;
        //Set circle indicator radius
        indicator.setRadius(5 * density);
        //
        //       No of pages set to 3!!
        NUM_PAGES = pageSize;

        // Auto start of viewpager
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES) {
                    currentPage = 0;
                }
                viewPager.setCurrentItem(currentPage++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 1000, 1000);

        // Pager listener over indicator
        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                currentPage = position;

            }

            @Override
            public void onPageScrolled(int pos, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int pos) {

            }
        });

    }


}
