package nazianoorani.tai;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import nazianoorani.tai.dto.MainDto;

/**
 * Created by nazianoorani on 11/06/16.
 */
public class MainFragment extends Fragment {
    @InjectView(R.id.rcycStudy)
    RecyclerView mRcycStudy;
    @InjectView(R.id.rcycFun)
    RecyclerView mRcycFun;
    @InjectView(R.id.rcycEdit)
    RecyclerView mRcycEditor;
    @InjectView(R.id.tv_study_section)
    TextView tvStudySection;
    @InjectView(R.id.tv_fun_section)
    TextView tvFunSection;
    @InjectView(R.id.tv_editor)
    TextView tvEditor;
    HorizontalRecyclerAdapter mAdapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home,container,false);
        ButterKnife.inject(this,view);
//        tvStudySection.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "blackjack.otf"));
//        tvFunSection.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "blackjack.otf"));
//        tvEditor.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "blackjack.otf"));
        ArrayList<MainDto> list = new ArrayList<>();
        MainDto mainDto = new MainDto();
        mainDto.setSubName("Chemistry");
        list.add(mainDto);
        mainDto = new MainDto();
        mainDto.setSubName("Physics");
        list.add(mainDto);
        mainDto = new MainDto();
        mainDto.setSubName("Maths");
        list.add(mainDto);
        mainDto = new MainDto();
        mainDto.setSubName("History");
        list.add(mainDto);
        mainDto = new MainDto();
        mainDto.setSubName("Hindi");
        list.add(mainDto);
        mAdapter = new HorizontalRecyclerAdapter(list);
        initRecylers();
//        ((MainActivity)getActivity()).toggleTextCourses();
        return view;
    }

    private void initRecylers(){
        mRcycStudy.setAdapter(mAdapter);
        mRcycFun.setAdapter(mAdapter);
        mRcycEditor.setAdapter(mAdapter);
        mRcycStudy.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
        mRcycFun.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
        mRcycEditor.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));

    }



    public class HorizontalRecyclerAdapter extends RecyclerView.Adapter<HorizontalRecyclerAdapter.HorizontalRecyclerViewHolder> {

        ArrayList<MainDto>list;
        public HorizontalRecyclerAdapter(ArrayList<MainDto>list){
            this.list= list;
        }
        @Override
        public HorizontalRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_recycler_item,parent,false);
            HorizontalRecyclerViewHolder viewHolder = new HorizontalRecyclerViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(HorizontalRecyclerViewHolder holder, int position) {
            holder.textView.setText(list.get(position).getSubName());
            holder.img.setBackgroundResource(R.mipmap.img_subjects);
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public class HorizontalRecyclerViewHolder extends RecyclerView.ViewHolder{
            TextView textView;
            ImageView img;
            public HorizontalRecyclerViewHolder(View itemView) {
                super(itemView);
                textView = (TextView) itemView.findViewById(R.id.sub_name);
                img = (ImageView) itemView.findViewById(R.id.imgSub);

            }
        }
    }


}
