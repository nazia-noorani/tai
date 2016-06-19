package nazianoorani.tai;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.firebase.client.Firebase;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import butterknife.ButterKnife;
import butterknife.InjectView;
import nazianoorani.tai.adapters.SectionsPagerAdapter;
import nazianoorani.tai.dto.SubTopicDto;
import nazianoorani.tai.dto.TopicDto;
import nazianoorani.tai.networkmanager.AppController;
import nazianoorani.tai.util.Constants;
import nazianoorani.tai.util.NetworkUtil;
import nazianoorani.tai.util.SnackBarUtil;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,View.OnClickListener {

    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    @InjectView(R.id.container)
    ViewPager mViewPager;

    @InjectView(R.id.tabs)
    TabLayout mTabLayout;
//    @InjectView(R.id.courses)
//    TextView mTxtCourses;
//    @InjectView(R.id.tests)
//    TextView mTxtTests;
//    @InjectView(R.id.analytics)
//    TextView mTxtAnalytics;
    @InjectView(R.id.drawer_layout)
    DrawerLayout drawer;
    private SectionsPagerAdapter mSectionsPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        setSupportActionBar(toolbar);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        // Set up the ViewPager with the sections adapter.
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);

//        FragmentManager fm = getSupportFragmentManager();
//        fm.beginTransaction().add(R.id.container,new MainFragment()).commit();


//
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            moveTaskToBack(true);
             finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_profile) {
            Intent intent = new Intent(this,ProfileActivity.class);
            startActivity(intent);
            // Handle the camera action

        } else if (id == R.id.nav_contact) {

            Intent intent = new Intent(this,ContactActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_faq) {
            Intent intent = new Intent(this,FAQActivity.class);
            startActivity(intent);


        } else if (id == R.id.nav_logout) {
            if(NetworkUtil.isNetworkAvailable(this)){
            Firebase ref = new Firebase("https://tai.firebaseio.com");
            ref.unauth();
            finish();
            }else {
                SnackBarUtil.display(this,getString(R.string.no_internet),Snackbar.LENGTH_LONG);
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
//            case R.id.courses :
//                mTxtCourses.setSelected(true);
//                mTxtCourses.setClickable(false);
//                FragmentManager fm = getSupportFragmentManager();
//                fm.beginTransaction().addToBackStack(null).replace(R.id.container,new CoursesFragment()).commit();
//                break;
        }

    }
//    public void toggleTextCourses(){
//        mTxtCourses.setClickable(true);
//        mTxtCourses.setSelected(false);
//    }



//    private void fetchTopics() {
//
//        if (!NetworkUtil.isNetworkAvailable(this)){
////            mProgressBar.setVisibility(View.GONE);
//            SnackBarUtil.display(this,getString(R.string.no_internet), Snackbar.LENGTH_LONG);
//        }else {
//            String CHAPTER_URL = Constants.BASE_URL + "7/Chemistry/chapters.json";
//            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, CHAPTER_URL, null, new Response.Listener<JSONObject>() {
//                @Override
//                public void onResponse(JSONObject response) {
//                    try {
//
//                        if (!topicList.isEmpty()) {
//                            topicList.clear();
//                        }
//                        Iterator<String> iter = response.keys();
//                        while (iter.hasNext()) {
//                            String key = iter.next();
//
//                            JSONObject jsonObject = response.getJSONObject(key);
//
//                            TopicDto dto = new TopicDto();
//                            if (jsonObject.has("topic")) {
//                                dto.setTopic(jsonObject.getString("topic"));
//                            }
//                            topicList.add(dto);
//                        }
////                        mProgressBar.setVisibility(View.INVISIBLE);
//                        prepareListData();
//                        expListAdapter.notifyDataSetChanged();
//
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                        SnackBarUtil.display(MainActivity.this,e.getMessage(), Snackbar.LENGTH_LONG);
//
//                    }
//                }}, new Response.ErrorListener() {
//                @Override
//                public void onErrorResponse(VolleyError volleyError) {
//                    SnackBarUtil.display(MainActivity.this,volleyError.getMessage(), Snackbar.LENGTH_LONG);
//                }
//            });
//            AppController.getInstance().addToRequestQueue(request);
//        }
//    }
//
//    private void fetchSubTopics(final String topic) {
//        if (!NetworkUtil.isNetworkAvailable(this)){
////            mProgressBar.setVisibility(View.GONE);
//            SnackBarUtil.display(this,getString(R.string.no_internet), Snackbar.LENGTH_LONG);
//        }else {
//            String CHAPTER_URL = Constants.BASE_URL + "7/Chemistry/"+topic+".json";
//            final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, CHAPTER_URL, null, new Response.Listener<JSONObject>() {
//                @Override
//                public void onResponse(JSONObject response) {
//
//
//                    if(!subTopicList.isEmpty()){
//                        subTopicList.clear();
//                    }
//                    Iterator<String> iter = response.keys();
//                    while (iter.hasNext()) {
//                        String key = iter.next();
//                        try {
//                            JSONObject jsonObject = response.getJSONObject(key);
//                            SubTopicDto dto = new SubTopicDto();
//                            if (jsonObject.has("topic")) {
//                                dto.setTopic(jsonObject.getString("topic"));
//                            }
//                            if (jsonObject.has("time")) {
//                                dto.setTime(jsonObject.getString("time"));
//                            }
//                            if (jsonObject.has("url")) {
//                                dto.setURL(jsonObject.getString("url"));
//                            }
//                            subTopicList.add(dto);
//                        } catch (JSONException e) {
//                            Log.e("JSON-ERROR",e.toString());
//                        }
//                    }
//
//
//                }
//            }, new Response.ErrorListener() {
//                @Override
//                public void onErrorResponse(VolleyError volleyError) {
//                    SnackBarUtil.display(MainActivity.this,volleyError.getMessage(), Snackbar.LENGTH_LONG);
//                }
//            });
//            AppController.getInstance().addToRequestQueue(request);
//        }
//
//    }


}
