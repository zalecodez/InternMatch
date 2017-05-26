package com.zalehacks.apps.internmatch;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class AboutActivity extends AppCompatActivity {

    public static Intent newIntent(Context context){
        Intent i;
        i = new Intent(context, AboutActivity.class);
        return i;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fragment);

        FragmentManager fm = getSupportFragmentManager();

        if(fm.findFragmentById(R.id.fragment_container) == null){
            fm.beginTransaction()
                    .add(R.id.fragment_container, AboutFragment.newInstance())
                    .commit();
        }
    }
}
