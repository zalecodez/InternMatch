package com.zalehacks.apps.internmatch;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.PersistableBundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class HomeActivity extends AppCompatActivity
        implements LoginFragment.OnLoginListener,
        RegisterFragment.RegisterListener,
        ProfileFragment.ProfileActionListener{

    private static final String STATE_LOGGEDIN = "isLoggedIn";

    private boolean mLoggedin;
    private Fragment mFrag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        if(savedInstanceState == null){
            mLoggedin = LoggedPref.isLoggedIn(this);
        }
        else{
            mLoggedin = savedInstanceState.getBoolean(STATE_LOGGEDIN, false);
        }

        FragmentManager fm = getSupportFragmentManager();

        if(!mLoggedin){
            mFrag = LoginFragment.newInstance();
        }
        else{
            mFrag = ProfileFragment.newInstance();
        }

        if(fm.findFragmentById(R.id.fragment_container) == null){
            fm.beginTransaction()
                    .add(R.id.fragment_container, mFrag)
                    .commit();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putBoolean(STATE_LOGGEDIN, mLoggedin);
    }

    private void changeFragment(Fragment fragment){
        FragmentManager fm = getSupportFragmentManager();
        mFrag = fragment;
        fm.beginTransaction()
                .replace(R.id.fragment_container, mFrag)
                .commit();
    }

    @Override
    public void onLogin(boolean success, int id) {
        if(success) {
            changeFragment(ProfileFragment.newInstance());
            LoggedPref.setLogged(this, true, id);
        }
    }

    public void onLogout(){
        LoggedPref.setLogged(this, false, -1);
        Instance.get(this).free();
        changeFragment(LoginFragment.newInstance());
    }

    public static void logout(Context context){
        LoggedPref.setLogged(context,false,-1);
        Instance.get(context).free();
        Intent i = new Intent(context,HomeActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_CLEAR_TASK
                | Intent.FLAG_ACTIVITY_SINGLE_TOP
                | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }
    @Override
    public void onChangeToRegister() {
        changeFragment(RegisterFragment.newInstance());
    }

    @Override
    public void onChangeToLogin(boolean registered) {
        changeFragment(LoginFragment.newInstance());
    }
}
