package com.zalehacks.apps.internmatch;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import static com.zalehacks.apps.internmatch.User.Type.student;

/**
 * Created by zale on 9/03/17.
 */

public class Suggestion {
    private static final String TAG = "Suggestion";

    private int mMatchId;
    private String mMatchName;
    private String mMatchFname;
    private String mMatchLname;
    private Email mMatchEmail;
    private float mMatchPercent;
    private User.Type mForType;

    public Suggestion(JSONObject json, User.Type type)throws JSONException{
        mMatchId = json.getInt("id");
        mForType = type;

        Log.d(TAG, "id "+mMatchId);
        if(mForType == student){
            mMatchName = json.getString("name");
            Log.d(TAG, "suggestion: "+mMatchName);
        }
        else{
            mMatchFname = json.getString("fname");
            mMatchLname = json.getString("lname");
            Log.d(TAG, "suggestion: "+mMatchFname+" "+mMatchLname);
        }

        mMatchEmail = new Email(json.getString("email"));
    }

    public int getmMatchId() {
        return mMatchId;
    }

    public void setmMatchId(int mMatchId) {
        this.mMatchId = mMatchId;
    }

    public String getmMatchName() {

        if(mForType == student)
            return mMatchName;
        else
            return mMatchFname+" "+mMatchLname;
    }

    public void setmMatchName(String mMatchName) {
        this.mMatchName = mMatchName;
    }

    public Email getmMatchEmail() {
        return mMatchEmail;
    }

    public void setmMatchEmail(Email mMatchEmail) {
        this.mMatchEmail = mMatchEmail;
    }

    public float getmMatchPercent() {
        return mMatchPercent;
    }

    public void setmMatchPercent(float mMatchPercent) {
        this.mMatchPercent = mMatchPercent;
    }
}
