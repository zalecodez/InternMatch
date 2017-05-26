package com.zalehacks.apps.internmatch;


import android.content.Context;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {
    public static final String ARG_USER_ID = "UserId";

    ProfileActionListener mCallback;

    private int mUserId;
    Instance mInstance;

    ImageView mPicHolder;

    TextView mNameText;
    TextView mEmailText;
    TextView mGenderText;
    TextView mLevelText;
    TextView mGpaText;

    RecyclerView mPossessedRecycler;
    RecyclerView mDesiredRecycler;

    private ImageButton mEditPossessedButton;
    private TextView mNoPossessedText;
    private ImageButton mEditDesiredButton;
    private TextView mNoDesiredText;

    public static ProfileFragment newInstance(){
        //Bundle args = new Bundle();
        //args.putInt(ARG_USER_ID, userId);

        ProfileFragment frag = new ProfileFragment();
        //frag.setArguments(args);

        return frag;
    }

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            mCallback = (ProfileActionListener)context;
        }catch(ClassCastException e){
            throw new ClassCastException("Activity must implement ProfileActionListener "+e);
        }

        mUserId = LoggedPref.getLoggedId(getActivity());
        if(mUserId == -1 || Instance.get(getActivity()).getmUser().getmId() != mUserId)
            mCallback.onLogout();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_profile, container, false);

        mInstance = Instance.get(getActivity());
        int noPosStrId;
        int noDesStrId;
        if(Instance.get(getActivity()).getType() == User.Type.student){
            noPosStrId = R.string.no_possessed_student;
            noDesStrId = R.string.no_desired_student;
        }
        else{
            noPosStrId = R.string.no_possessed_company;
            noDesStrId = R.string.no_desired_company;
        }

        mPicHolder = (ImageView)v.findViewById(R.id.image_placeholder);

        if(Instance.get(getActivity()).getType() == User.Type.company){
            mPicHolder.setImageResource(R.drawable.comppicholder);
        }

        mNameText = (TextView)v.findViewById(R.id.text_profile_name);
        mNameText.setText(Instance.get(getActivity()).getName());

        mEmailText = (TextView) v.findViewById(R.id.text_profile_email);
        mEmailText.setText(Instance.get(getActivity()).getEmail().toString());

        mGenderText = (TextView)v.findViewById(R.id.text_profile_gender);
        mGenderText.setText(mInstance.getGender());

        mLevelText = (TextView)v.findViewById(R.id.text_profile_level);
        mLevelText.setText(mInstance.getLevel());

        mGpaText = (TextView)v.findViewById(R.id.text_profile_gpa);
        mGpaText.setText("GPA: "+Float.toString(mInstance.getGpa()));


        mNoPossessedText = (TextView)v.findViewById(R.id.text_no_possessed);
        mNoPossessedText.setText(noPosStrId);

        mPossessedRecycler = (RecyclerView) v.findViewById(R.id.recycler_view_possessed);
        mPossessedRecycler.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        mEditPossessedButton = (ImageButton)v.findViewById(R.id.imagebutton_edit_possessed);
        mEditPossessedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String qualattrtype = Instance.QUALATTR_TYPE_POSSESSED;
                startActivity(EditQualAttrActivity.newIntent(getActivity(),qualattrtype));
            }
        });

        mNoDesiredText = (TextView)v.findViewById(R.id.text_no_desired);
        mNoDesiredText.setText(noDesStrId);

        mDesiredRecycler = (RecyclerView) v.findViewById(R.id.recycler_view_desired);
        mDesiredRecycler.setLayoutManager(new GridLayoutManager(getActivity(), 2));


        mEditDesiredButton = (ImageButton)v.findViewById(R.id.imagebutton_edit_desired);
        mEditDesiredButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String qualattrtype = Instance.QUALATTR_TYPE_DESIRED;
                startActivity(EditQualAttrActivity.newIntent(getActivity(),qualattrtype));
            }
        });

        refreshQualAttrs();

        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.profile_menu,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.menu_item_logout:
                mCallback.onLogout();
                return true;

            case R.id.menu_item_matches:
                startActivity(MatchActivity.newIntent(getActivity()));
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshQualAttrs();
    }

    public void refreshQualAttrs(){
        if(Instance.get(getActivity()).getPossessed() != null){
            mNoPossessedText.setVisibility(View.GONE);


            mPossessedRecycler.setVisibility(View.VISIBLE);
            mPossessedRecycler.setAdapter(new QualAttrAdapter(Instance.get(getActivity()).getPossessed()));
        }
        else{
            mNoPossessedText.setVisibility(View.VISIBLE);
            mPossessedRecycler.setVisibility(View.GONE);
        }

        if(Instance.get(getActivity()).getDesired() != null){
            mNoDesiredText.setVisibility(View.GONE);

            mDesiredRecycler.setVisibility(View.VISIBLE);
            mDesiredRecycler.setAdapter(new QualAttrAdapter(Instance.get(getActivity()).getDesired()));
        }
        else{
            mNoDesiredText.setVisibility(View.VISIBLE);
            mDesiredRecycler.setVisibility(View.GONE);
        }
    }

    public interface ProfileActionListener{
        void onLogout();
    }



    private class QualAttrHolder extends RecyclerView.ViewHolder{
        QualAttr mQualAttr;
        TextView mPossessedText;

        public QualAttrHolder(View itemView){
            super(itemView);
            mPossessedText = (TextView)itemView;
        }

        public void bindQualAttr(QualAttr qualAttr){
            mQualAttr = qualAttr;
            mPossessedText.setText(mQualAttr.getmName());
        }
    }
    private class QualAttrAdapter extends RecyclerView.Adapter<QualAttrHolder>{
        List<QualAttr> mQualAttrs;

        public QualAttrAdapter(List<QualAttr> qualattrs){
            mQualAttrs = qualattrs;
        }

        @Override
        public QualAttrHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View item = inflater.inflate(R.layout.qualattr_grid_item, parent, false);

            return new QualAttrHolder(item);
        }

        @Override
        public void onBindViewHolder(QualAttrHolder holder, int position) {
            holder.bindQualAttr(mQualAttrs.get(position));
        }

        @Override
        public int getItemCount() {
            return mQualAttrs.size();
        }
    }
}
