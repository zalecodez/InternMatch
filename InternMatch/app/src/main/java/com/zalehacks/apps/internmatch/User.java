package com.zalehacks.apps.internmatch;

import android.util.Log;

import java.util.List;

import static com.zalehacks.apps.internmatch.User.Type.student;

/**
 * Created by zale on 3/03/17.
 */

public class User {
    private static final String TAG = "User";

    public enum Type{
        student, company
    }

    private int mId;
    private Type mType;

    private Email mEmail;
    private String mPassword;
    private String mFname;
    private String mLname;
    private String mName;

    private String mGender;
    private String mLevel;
    private float mGpa;

    public String getmGender() {
        return mGender;
    }

    public void setmGender(String mGender) {
        this.mGender = mGender;
    }

    public String getmLevel() {
        return mLevel;
    }

    public void setmLevel(String mLevel) {
        this.mLevel = mLevel;
    }

    public float getmGpa() {
        return mGpa;
    }

    public void setmGpa(float mGpa) {
        this.mGpa = mGpa;
    }

    private List<QualAttr> mStudentQualities;
    private List<QualAttr> mCompanyAttributes;

    private List<Suggestion> mSuggestions;
    private List<Suggestion> mReceivedRequests;
    private List<Suggestion> mSentRequests;
    private List<Suggestion> mMatches;

    public Type getmType() {
        return mType;
    }

    public void setmType(Type mType) {
        this.mType = mType;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public Email getEmail() {
        return mEmail;
    }

    public void setEmail(Email email) {
        this.mEmail = email;
    }

    public String getmFname() {
        return mFname;
    }

    public void setmFname(String mFname) {
        this.mFname = mFname;
    }

    public String getmLname() {
        return mLname;
    }

    public void setmLname(String mLname) {
        this.mLname = mLname;
    }

    public void setmPassword(String mPassword) {
        this.mPassword = mPassword;
    }

    public String getmPassword() {
        return mPassword;
    }

    public int getmId() {
        return mId;
    }

    public void setmId(int mId) {
        this.mId = mId;
    }


    public void setmQualities(List<QualAttr> mQualities) {
        this.mStudentQualities = mQualities;
    }

    public void setmAttributes(List<QualAttr> mAttributes) {
        this.mCompanyAttributes = mAttributes;
    }

    public List<QualAttr> getDesired(){
        if(mType == student){
            return mCompanyAttributes;
        }
        else
            return mStudentQualities;
    }

    public List<QualAttr> getPossessed(){
        if(mType == student)
            return mStudentQualities;
        else
            return mCompanyAttributes;
    }

    public void setmSuggestions(List<Suggestion> mSuggestions) {
        this.mSuggestions = mSuggestions;
    }

    public List<Suggestion> getmSuggestions() {
        return this.mSuggestions;
    }
}
