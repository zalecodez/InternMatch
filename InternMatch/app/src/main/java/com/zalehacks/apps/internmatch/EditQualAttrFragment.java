package com.zalehacks.apps.internmatch;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * A simple {@link Fragment} subclass.
 */
public class EditQualAttrFragment extends Fragment {
    public static final String TAG = "EditQualAttrFragment";

    private static final String ARG_QUALATTR_TYPE = "qualAttrType";

    private String mQualAttrType;
    private List<QualAttr> mAllQualAttrs;
    private List<Boolean> mHasQualAttrs;
    private List<Boolean> mTempHasQualAttrs;

    private ImageButton mSaveButton;
    private TextView mTitle;
    private RecyclerView mEditQualAttrRecycler;

    public static EditQualAttrFragment newInstance(String qualattrType){
        Bundle args = new Bundle();
        args.putString(ARG_QUALATTR_TYPE, qualattrType);

        EditQualAttrFragment frag = new EditQualAttrFragment();
        frag.setArguments(args);
        return frag;
    }

    public EditQualAttrFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        mQualAttrType = getArguments().getString(ARG_QUALATTR_TYPE);

        mAllQualAttrs = new ArrayList<>();
        mHasQualAttrs = new ArrayList<>();

        Log.d(TAG, "mAllQualAttrs being set getting "+mQualAttrType);
        mAllQualAttrs = (ArrayList<QualAttr>)Instance.get(getActivity()).getAllPossibleQualAttrs(mQualAttrType, true);
        mHasQualAttrs = (ArrayList<Boolean>)Instance.get(getActivity()).getAllPossibleQualAttrs(mQualAttrType, false);

        int size = mHasQualAttrs.size();
        mTempHasQualAttrs = new ArrayList<>();
        for(int i = 0; i < size;i++){
            mTempHasQualAttrs.add(i, mHasQualAttrs.get(i));
        }

        Instance.get(getActivity()).logHas();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_edit_qual_attr, container, false);

        mTitle = (TextView)v.findViewById(R.id.text_edit_qualattrs_title);
        int stringId;
        if(Instance.get(getActivity()).getType() == User.Type.student){
            if(mQualAttrType.equals(Instance.QUALATTR_TYPE_DESIRED)) {
                stringId = R.string.edit_desired_student;
                Log.d(TAG, "1. THE QUALTYPE IS "+mQualAttrType +" AND STUDENT TYPE IS "+Instance.get(getActivity()).getType()+" SO STRING IS "+getString(stringId));
            }
            else {
                stringId = R.string.edit_possessed_student;
                Log.d(TAG, "2. THE QUALTYPE IS "+mQualAttrType +" AND STUDENT TYPE IS "+Instance.get(getActivity()).getType()+" SO STRING IS "+getString(stringId));
            }
        }
        else{
            if(mQualAttrType.equals(Instance.QUALATTR_TYPE_POSSESSED)) {
                stringId = R.string.edit_possessed_company;
                Log.d(TAG, "3. THE QUALTYPE IS "+mQualAttrType +" AND STUDENT TYPE IS "+Instance.get(getActivity()).getType()+" SO STRING IS "+getString(stringId));
            }
            else {
                stringId = R.string.edit_desired_company;
                Log.d(TAG, "4. THE QUALTYPE IS "+mQualAttrType +" AND STUDENT TYPE IS "+Instance.get(getActivity()).getType()+" SO STRING IS "+getString(stringId));
            }
        }
        mTitle.setText(stringId);

        mEditQualAttrRecycler = (RecyclerView) v.findViewById(R.id.recycler_view_edit_qualattr);
        mEditQualAttrRecycler.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        mEditQualAttrRecycler.setAdapter(new EditQualAttrAdapter(mAllQualAttrs, mTempHasQualAttrs));

        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.edit_qualattr_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.menu_item_save_qualattr:
                new ChangeQualAttrTask().execute();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onSave(boolean success){
        if(success){
        }
        else{
        }
        getActivity().onBackPressed();
    }


    private class EditQualAttrHolder extends RecyclerView.ViewHolder{
        private TextView mQualAttrTitle;
        private CheckBox mQualAttrCheckBox;

        public EditQualAttrHolder(View itemView){
            super(itemView);

            mQualAttrTitle = (TextView) itemView.findViewById(R.id.text_qualattr_item_title);
            mQualAttrCheckBox = (CheckBox)itemView.findViewById(R.id.checkbox_qualattr);

        }

        public void bindQualAttr(String name, boolean checked, final int position){
            mQualAttrTitle.setText(name);
            mQualAttrCheckBox.setChecked(checked);

            mQualAttrCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    mTempHasQualAttrs.remove(position);
                    mTempHasQualAttrs.add(position, b);
                    Instance.get(getActivity()).logHas();
                }
            });
        }
    }

    private class EditQualAttrAdapter extends RecyclerView.Adapter<EditQualAttrHolder>{
        private List<QualAttr> mAllQualAttrs;
        private List<Boolean> mHasQualAttrs;

        public EditQualAttrAdapter(List<QualAttr> qualAttrs, List<Boolean> hasQualAttrs){
            mAllQualAttrs = qualAttrs;
            mHasQualAttrs = hasQualAttrs;
        }


        @Override
        public EditQualAttrHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View itemView = inflater.inflate(R.layout.edit_qualattr_grid_item, parent, false);

            return new EditQualAttrHolder(itemView);
        }

        @Override
        public void onBindViewHolder(EditQualAttrHolder holder, int position) {
            holder.bindQualAttr(mAllQualAttrs.get(position).getmName(), mHasQualAttrs.get(position), position);
        }


        @Override
        public int getItemCount() {
            return mAllQualAttrs.size();
        }
    }



    private class ChangeQualAttrTask extends AsyncTask<Void, Void, Boolean>{

        @Override
        protected Boolean doInBackground(Void... voids) {
            return new Connector(getActivity()).setQualAttrs(mTempHasQualAttrs, mQualAttrType);
        }

        @Override
        protected void onPostExecute(Boolean success) {
            super.onPostExecute(success);

            if (success)
                onSave(success);
        }
    }

}
