package nazianoorani.tai.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import nazianoorani.tai.R;
import nazianoorani.tai.dto.SubTopicDto;
import nazianoorani.tai.dto.TopicDto;
import nazianoorani.tai.util.SnackBarUtil;

/**
 * Created by nazianoorani on 11/06/16.
 */
public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<TopicDto> listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<String, List<SubTopicDto>> listDataChild ;

    public ExpandableListAdapter(Context context, List<TopicDto> listDataHeader,
                                 HashMap<String, List<SubTopicDto>> listChildData) {
        this.context = context;
        this.listDataHeader = listDataHeader;
        this.listDataChild = listChildData;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return listDataChild.get(listDataHeader.get(groupPosition).getChapter()).get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

//        final String childText = (String) getChild(groupPosition, childPosition);
        SubTopicDto dto = (SubTopicDto) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.exp_list_item, null);
        }

        TextView txtListChild = (TextView) convertView
                .findViewById(R.id.lblListItem);
        TextView duration = (TextView) convertView.findViewById(R.id.duration);

        ImageView imageView = (ImageView) convertView.findViewById(R.id.img_thumbnail);
        String id = dto.getURL().substring(dto.getURL().indexOf("=")+1,dto.getURL().indexOf("=")+12);
        String url = "http://img.youtube.com/vi/" + id + "/1.jpg";
        Log.i("id",id);
        Log.i("url",url);

        Picasso.with(context).load(url).into(imageView);
        txtListChild.setText(dto.getTopic());
        duration.setText(dto.getTime());
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.listDataChild.get(listDataHeader.get(groupPosition).getChapter()).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.listDataHeader.get(groupPosition).getChapter();
    }

    @Override
    public int getGroupCount() {
        return this.listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
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
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
