package nazianoorani.tai;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatSpinner;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import nazianoorani.tai.adapters.ExpandableListAdapter;
import nazianoorani.tai.dto.SubTopicDto;
import nazianoorani.tai.dto.TopicDto;
import nazianoorani.tai.networkmanager.AppController;
import nazianoorani.tai.util.AppUtil;
import nazianoorani.tai.util.Constants;
import nazianoorani.tai.util.NetworkUtil;
import nazianoorani.tai.util.SnackBarUtil;

/**
 * Created by nazianoorani on 11/06/16.
 */
public class CoursesFragment extends Fragment implements AdapterView.OnItemSelectedListener,ExpandableListView.OnChildClickListener,ExpandableListView.OnGroupExpandListener ,ExpandableListView.OnGroupClickListener,ExpandableListView.OnGroupCollapseListener{

    @InjectView(R.id.progressBarDetails)
    ProgressBar mProgressBar;
    @InjectView(R.id.listViewExp)
    ExpandableListView expListView;
    @InjectView(R.id.spinner)
    AppCompatSpinner mSpinner;

    @Nullable
    ArrayList<TopicDto> topicList = new ArrayList<>();
    ArrayList<SubTopicDto> subTopicList = new ArrayList<>();
    HashMap<String, List<SubTopicDto>> listDataChild = new HashMap<String, List<SubTopicDto>>();;
    ExpandableListAdapter expListAdapter;
    private static int previousGroup = -1;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_courses,container , false);
        ButterKnife.inject(this,view);
//        fetchTopics();
        mProgressBar.setVisibility(View.VISIBLE);
        mProgressBar.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.colorPrimary), android.graphics.PorterDuff.Mode.MULTIPLY);
        expListAdapter = new ExpandableListAdapter(getContext(), topicList, listDataChild);
        expListAdapter.notifyDataSetChanged();
        expListView.setOnChildClickListener(this);
        expListView.setOnGroupExpandListener(this);
        expListView.setOnGroupClickListener(this);
        expListView.setOnGroupCollapseListener(this);
        // Spinner click listener
        mSpinner.setOnItemSelectedListener(this);



        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        categories.add("Physics");
        categories.add("Chemistry");
        categories.add("Computers");
        categories.add("Biology");
        categories.add("Hindi");
        categories.add("History");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        mSpinner.setAdapter(dataAdapter);
//        prepareListData();
        expListView.setAdapter(expListAdapter);
        return view;

    }


    private void fetchTopics(String subject) {
            expListView.setAdapter(expListAdapter);
            mProgressBar.setVisibility(View.VISIBLE);
            String CHAPTER_URL = Constants.BASE_URL + "7/"+subject+"/chapters.json";
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, CHAPTER_URL, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {

                        if (!topicList.isEmpty()) {
                            topicList.clear();
                        }
                        Iterator<String> iter = response.keys();
                        while (iter.hasNext()) {
                            String key = iter.next();

                            JSONObject jsonObject = response.getJSONObject(key);

                            TopicDto dto = new TopicDto();
                            if (jsonObject.has("chapter")) {
                                dto.setChapter(jsonObject.getString("chapter"));
                            }
                            topicList.add(dto);
                        }
                        mProgressBar.setVisibility(View.INVISIBLE);
//                        prepareListData();
                        expListAdapter.notifyDataSetChanged();

                    } catch (JSONException e) {
                        e.printStackTrace();
                        mProgressBar.setVisibility(View.INVISIBLE);
                        expListView.setAdapter((android.widget.ExpandableListAdapter) null);
                        expListAdapter.notifyDataSetInvalidated();
                        SnackBarUtil.display(getContext(),e.getMessage(), Snackbar.LENGTH_LONG);
                        expListAdapter.notifyDataSetChanged();

                    }
                }}, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    expListAdapter.notifyDataSetInvalidated();
                    mProgressBar.setVisibility(View.INVISIBLE);
                    expListView.setAdapter((android.widget.ExpandableListAdapter) null);
                    SnackBarUtil.display(getContext(),"Currently Unavailable !", Snackbar.LENGTH_LONG);
                }
            });
            AppController.getInstance().addToRequestQueue(request);
//        }
    }

    private void fetchSubTopics(final String topic) {
            mProgressBar.setVisibility(View.VISIBLE);
            String CHAPTER_URL = Constants.BASE_URL + "7/Chemistry/"+topic+".json";
            final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, CHAPTER_URL, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {


                    if(!subTopicList.isEmpty()){
                        subTopicList.clear();
                    }
                    Iterator<String> iter = response.keys();
                    while (iter.hasNext()) {
                        String key = iter.next();
                        try {
                            JSONObject jsonObject = response.getJSONObject(key);
                            SubTopicDto dto = new SubTopicDto();
                            if (jsonObject.has("topic")) {
                                dto.setTopic(jsonObject.getString("topic"));
                            }
                            if (jsonObject.has("time")) {
                                dto.setTime(jsonObject.getString("time"));
                            }
                            if (jsonObject.has("url")) {
                                dto.setURL(jsonObject.getString("url"));
                            }
                            subTopicList.add(dto);
                            expListAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            mProgressBar.setVisibility(View.INVISIBLE);
                            Log.e("JSON-ERROR",e.toString());
                        }
                    }


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    mProgressBar.setVisibility(View.INVISIBLE);
                    SnackBarUtil.display(getContext(),"Currently Unavailable!", Snackbar.LENGTH_LONG);
                }
            });
            AppController.getInstance().addToRequestQueue(request);
//        }

    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String item = parent.getItemAtPosition(position).toString();
        // Showing selected spinner item
//        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
        fetchTopics(item);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

        String URL = listDataChild.get(topicList.get(groupPosition).getChapter()).get(childPosition).getURL();
        String ID = URL.substring(URL.indexOf("=")+1,URL.indexOf("=")+12);
        showVideo(URL,ID);
        return true;
    }




    private void showVideo(String URL , String ID) {

        if ( URL != null && ID != null) {
            if (AppUtil.isAppInstalled("com.google.android.youtube", getActivity())) {

                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + ID));
                    getActivity().startActivity(intent);
                } catch (ActivityNotFoundException ex) {
                    Intent intent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse(URL));
                    getActivity().startActivity(intent);
                }
            } else {
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://www.youtube.com/watch?v=" + ID));
                    getActivity().startActivity(intent);

                } catch (ActivityNotFoundException ex) {
                    SnackBarUtil.display(getContext(),"Cannot play this video",Snackbar.LENGTH_LONG);
                }

            }

        }
    }

    @Override
    public void onGroupExpand(int groupPosition) {
        // Collapse previous parent if expanded.
        if ((previousGroup != -1) && (groupPosition != previousGroup)) {
            expListView.collapseGroup(previousGroup);
        }
        previousGroup = groupPosition;


    }

    @Override
    public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
        if(topicList.size()!=0) {
            fetchSubTopics(topicList.get(groupPosition).getChapter());
            listDataChild.put(topicList.get(groupPosition).getChapter(), subTopicList);
            expListAdapter.notifyDataSetChanged();
//            new Handler().postDelayed(new Runnable() {
//
//            /*
//             * Showing splash screen with a timer. This will be useful when you
//             * want to show case your app logo / company
//             */
//
//                @Override
//                public void run() {
                    // This method will be executed once the timer is over
                    mProgressBar.setVisibility(View.INVISIBLE);



//                }
//            }, 1000);

        }
        return false;
    }

    @Override
    public void onGroupCollapse(int groupPosition) {
        subTopicList.clear();
        listDataChild.put(topicList.get(groupPosition).getChapter(),subTopicList);
        expListAdapter.notifyDataSetChanged();

    }
}
