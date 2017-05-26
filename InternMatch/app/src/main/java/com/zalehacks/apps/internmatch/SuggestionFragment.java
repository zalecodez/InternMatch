package com.zalehacks.apps.internmatch;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.view.menu.MenuView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class SuggestionFragment extends Fragment {

    ProfileFragment.ProfileActionListener mCallback;

    RecyclerView mMatchRecyclerView;
    TextView mNoSuggestionsText;

    List<Suggestion> mSuggestions;

    public SuggestionFragment() {
        // Required empty public constructor
    }

    public static SuggestionFragment newInstance() {

        Bundle args = new Bundle();

        SuggestionFragment fragment = new SuggestionFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mSuggestions = Instance.get(getActivity()).getSuggestions();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        /*
        try{
            mCallback = (ProfileFragment.ProfileActionListener)context;
        }catch(ClassCastException e){
            throw new ClassCastException("Actvity must implement ProfileActionistener "+e);
        }
        */
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedinstancestate) {

        View v = inflater.inflate(R.layout.fragment_match, container, false);

        mMatchRecyclerView = (RecyclerView)v.findViewById(R.id.recycler_view_matches);
        mNoSuggestionsText = (TextView)v.findViewById(R.id.text_no_suggestions);

        if(mSuggestions == null){
            mMatchRecyclerView.setVisibility(View.GONE);
            mNoSuggestionsText.setVisibility(View.VISIBLE);
        }
        else {
            mMatchRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
            mMatchRecyclerView.setAdapter(new MatchGridAdapter(mSuggestions));

        }

        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.profile_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()) {
            case R.id.menu_item_logout:
                HomeActivity.logout(getActivity());
                return true;
            case R.id.menu_item_profile:
                getActivity().onBackPressed();
                //startActivity(new Intent(getActivity(), HomeActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private class MatchHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView mPicHolderImage;
        TextView mNameTextView;
        Button mConnectButton;
        Suggestion mSuggestion;

        public MatchHolder(View itemView){
            super(itemView);
            mNameTextView = (TextView)itemView.findViewById(R.id.text_view_grid_name);

            mPicHolderImage = (ImageView)itemView.findViewById(R.id.image_view_grid);

            mConnectButton = (Button)itemView.findViewById(R.id.button_connect);
        }

        public void bindMatch(Suggestion suggestion){
            mSuggestion = suggestion;

            mNameTextView.setText(mSuggestion.getmMatchName());

            if(Instance.get(getActivity()).getType() == User.Type.company){
                mPicHolderImage.setImageResource(R.drawable.picholder);
            }

            mConnectButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Intent i = new Intent();
            i.setAction(Intent.ACTION_SEND);
            i.setData(Uri.parse("mailto:"));
            i.putExtra(Intent.EXTRA_EMAIL, new String[]{mSuggestion.getmMatchEmail().toString()});
            i.setType("text/plain");
            if(i.resolveActivity(getActivity().getPackageManager()) != null)
                getActivity().startActivity(i);

            StringBuilder connectString = new StringBuilder("Contact ");
            connectString.append(mSuggestion.getmMatchName());
            connectString.append(" at ");
            connectString.append(mSuggestion.getmMatchEmail().toString());
            connectString.append("!");

            Toast.makeText(getActivity(), connectString.toString(),Toast.LENGTH_LONG).show();
        }
    }

    private class MatchGridAdapter extends RecyclerView.Adapter<MatchHolder>{
        List<Suggestion> mSuggestions;

        public MatchGridAdapter(List<Suggestion> suggestions){
            mSuggestions = suggestions;
        }

        @Override
        public MatchHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View v = inflater.inflate(R.layout.matched_user_item, parent, false);

            return new MatchHolder(v);
        }

        @Override
        public void onBindViewHolder(MatchHolder holder, int position) {
            holder.bindMatch(mSuggestions.get(position));
        }

        @Override
        public int getItemCount() {
            return mSuggestions.size();
        }
    }

}
