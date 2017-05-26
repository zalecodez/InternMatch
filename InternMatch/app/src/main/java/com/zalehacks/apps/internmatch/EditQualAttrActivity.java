package com.zalehacks.apps.internmatch;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class EditQualAttrActivity extends AppCompatActivity {

    public static final String QUALATTR_TYPE_EXTRA = "com.zalehacks.apps.internmatch.qualattrtype";

    public static Intent newIntent(Context context, String qualAttrType){
        Intent i = new Intent(context, EditQualAttrActivity.class);
        i.putExtra(QUALATTR_TYPE_EXTRA, qualAttrType);
        return i;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        String qualattrType = getIntent().getStringExtra(QUALATTR_TYPE_EXTRA);

        FragmentManager fm = getSupportFragmentManager();

        if(fm.findFragmentById(R.id.fragment_container) == null){
            fm.beginTransaction()
                    .add(R.id.fragment_container, EditQualAttrFragment.newInstance(qualattrType))
                    .commit();
        }
    }
}
