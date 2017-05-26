package com.zalehacks.apps.internmatch;


import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class AboutFragment extends Fragment {

    RecyclerView mInfoRecycler;
    String[] mAboutHeadings;
    String[] mAboutDetails;

    public AboutFragment() {
        // Required empty public constructor
    }

    public static Fragment newInstance(){
        Fragment fragment = new AboutFragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_about, container, false);

        Resources res = getResources();
        mAboutHeadings = res.getStringArray(R.array.info_headings);
        mAboutDetails = res.getStringArray(R.array.info_details);

        Log.d("ABOUT", mAboutDetails[0]);


        mInfoRecycler = (RecyclerView) v.findViewById(R.id.recycler_view_about);
        mInfoRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        mInfoRecycler.setAdapter(new AboutTextAdapter(mAboutHeadings, mAboutDetails));
        return v;
    }


    private class AboutTextHolder extends RecyclerView.ViewHolder{
        TextView mHeadingText;
        TextView mDetailText;

        public AboutTextHolder(View itemView){
            super(itemView);
            mHeadingText = (TextView)itemView.findViewById(R.id.text_about_item_heading);
            mDetailText = (TextView) itemView.findViewById(R.id.text_about_item_detail);

        }

        public void bindInfo(String heading, String detail){
            mHeadingText.setText(heading);
            mDetailText.setText(detail);
        }



    }

    private class AboutTextAdapter extends RecyclerView.Adapter<AboutTextHolder>{

        String mHeadings[];
        String mDetails[];
        public AboutTextAdapter(String[] headings, String[] details){
            mHeadings = headings;
            mDetails = details;
        }

        @Override
        public AboutTextHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(getActivity()).inflate(R.layout.about_list_item, parent,false);

            AboutTextHolder holder = new AboutTextHolder(itemView);

            return holder;
        }

        @Override
        public void onBindViewHolder(AboutTextHolder holder, int position) {
            holder.bindInfo(mHeadings[position],mDetails[position]);
        }

        @Override
        public int getItemCount() {
            return mHeadings.length;
        }
    }

}
