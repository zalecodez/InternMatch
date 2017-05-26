package com.zalehacks.apps.internmatch;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by zale on 9/03/17.
 */

public class QualAttr {
    private int mAttrId;
    private String mAttrName;

    public QualAttr(int mAttrId, String mAttrName){
        this.mAttrId = mAttrId;
        this.mAttrName = mAttrName;
    }

    public QualAttr(JSONObject json)throws JSONException{
        this.mAttrId = json.getInt("id");
        this.mAttrName = json.getString("name");
    }

    public int getmId() {
        return mAttrId;
    }

    public String getmName() {
        return mAttrName;
    }

    public void setmAttrId(int mAttrId) {
        this.mAttrId = mAttrId;
    }

    public void setmAttrName(String mAttrName) {
        this.mAttrName = mAttrName;
    }
}
