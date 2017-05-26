package com.zalehacks.apps.internmatch;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by zale on 9/03/17.
 */

public class StudentQuality {
    private int mQualId;
    private String mQualName;


    public StudentQuality(int mQualId, String mQualName){
        this.mQualId = mQualId;
        this.mQualName = mQualName;
    }
    public StudentQuality(JSONObject json) throws JSONException{
        this.setmQualId(json.getInt("id"));
        this.setmQualName(json.getString("name"));
    }

    public int getmQualId() {
        return mQualId;
    }

    public String getmQualName() {
        return mQualName;
    }

    public void setmQualId(int mQualId) {
        this.mQualId = mQualId;
    }

    public void setmQualName(String mQualName) {
        this.mQualName = mQualName;
    }

}
