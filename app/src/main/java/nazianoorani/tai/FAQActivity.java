package nazianoorani.tai;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import nazianoorani.tai.adapters.ExpandableListAdapter;
import nazianoorani.tai.dto.SubTopicDto;
import nazianoorani.tai.util.Constants;

/**
 * Created by nazianoorani on 15/06/16.
 */


public class FAQActivity extends AppCompatActivity {
    @InjectView(R.id.exp_list_faq)
    ExpandableListView expList;
    @InjectView(R.id.toolbar_faq)
    Toolbar toolbar;
    ExpListAdapter expandableListAdapter;
    ArrayList<String> header = new ArrayList<>();
    ArrayList<String> answers = new ArrayList<>();
    private HashMap<String,String> listDataChild  = new HashMap<>(); ;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);
        ButterKnife.inject(this);
        setSupportActionBar(toolbar);
        toolbar.setTitle("FAQ");
        header.add(Constants.FAQ_Q1);
        header.add(Constants.FAQ_Q2);
        header.add(Constants.FAQ_Q3);
        header.add(Constants.FAQ_Q4);
        answers.add(Constants.FAQ_A1);
        answers.add(Constants.FAQ_A2);
        answers.add(Constants.FAQ_A3);
        answers.add(Constants.FAQ_A4);
        for(int i =0;i<header.size(); i++){
       listDataChild.put(header.get(i),answers.get(i));
        }
        expandableListAdapter = new ExpListAdapter(this,header,listDataChild);
        expList.setAdapter(expandableListAdapter);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
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

    class ExpListAdapter extends BaseExpandableListAdapter{
        private HashMap<String,String> listDataChild ;
        ArrayList<String> listDataHeader;
        Context context;
        public ExpListAdapter(Context context,ArrayList<String >listDataHeader,HashMap<String,String>listDataChild){
            this.context = context;
            this.listDataHeader = listDataHeader;
            this.listDataChild = listDataChild;
        }

        @Override
        public int getGroupCount() {
            return listDataHeader.size();
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return 1;
        }

        @Override
        public Object getGroup(int groupPosition) {
            return listDataHeader.get(groupPosition);
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return listDataChild.get(listDataHeader.get(groupPosition));
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return 0;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            String headerTitle = (String) getGroup(groupPosition);
            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) this.context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.exp_list_header, null);
            }

            TextView lblListHeader = (TextView) convertView
                    .findViewById(R.id.lblListHeader);
            lblListHeader.setTypeface(null, Typeface.BOLD);
            lblListHeader.setText(headerTitle);

            return convertView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

            final String childText = (String) getChild(groupPosition, childPosition);
            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) this.context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.exp_list_item_faq, null);
            }

            TextView txtListChild = (TextView) convertView
                    .findViewById(R.id.tv_list_faq);
            txtListChild.setText(childText);
            return convertView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return false;
        }
    }
}
