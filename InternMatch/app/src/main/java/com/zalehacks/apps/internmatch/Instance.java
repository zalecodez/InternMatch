package com.zalehacks.apps.internmatch;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zale on 6/03/17.
 */

public class Instance {
    private static String TAG = "Instance";
    public static final String QUALATTR_TYPE_DESIRED = "desired";
    public static final String QUALATTR_TYPE_POSSESSED = "possessed";

    private static Instance sInstance;

    private Context mContext;
    private User mUser;
    private JSONObject mUserJSON;
    private List<QualAttr> mAllStudentQualities;
    private List<QualAttr> mAllCompanyAttributes;
    private List<Boolean> mHasQuals;
    private List<Boolean> mHasAttrs;

    private List<User> mMatches;
    private List<User> mRequests;

    public static Instance get(Context context){
        if(sInstance == null)
            sInstance = new Instance(context);
        return sInstance;
    }

    private Instance(Context context) {
        mContext = context.getApplicationContext();
        Log.d(TAG, "new user created");
        mUser = new User();
    }

    public void free(){
        if(sInstance != null)
            sInstance = null;
    }

    public String getName(){
        if(mUser.getmType() == User.Type.student)
            return mUser.getmFname()+" "+mUser.getmLname();
        else
            return mUser.getmName();
    }

    public Email getEmail(){
        return mUser.getEmail();
    }

    public User getmUser() {
        return mUser;
    }

    public void setUser(User user){
        mUser = user;
    }

    public void setUser(JSONObject json) throws JSONException{
        mUserJSON = json;

        JSONArray possessedArray = null;
        JSONArray desiredArray = null;
        JSONArray suggestionArray = null;
        mUser.setmId(json.getInt("id"));
        mUser.setEmail(new Email(json.getString("email")));
        mUser.setmPassword(json.getString("password"));

        if(json.has("possessed"))
            possessedArray = json.getJSONArray("possessed");

        if(json.has("desired"))
             desiredArray = json.getJSONArray("desired");

        if(json.has("suggestions")) {
            suggestionArray = json.getJSONArray("suggestions");
            Log.d(TAG, "has suggestions");
        }

        String type = json.getString("type");
        if(type.equals("student")) {
            mUser.setmType(User.Type.student);
            mUser.setmFname(json.getString("fname"));
            mUser.setmLname(json.getString("lname"));
            mUser.setmGender(json.getString("gender"));
            mUser.setmLevel(json.getString("level"));
            mUser.setmGpa(json.getLong("gpa"));

            setmQualities(possessedArray);
            setmAttributes(desiredArray);
            setSuggestions(suggestionArray, User.Type.student);

        }
        else {
            mUser.setmType(User.Type.company);
            mUser.setmName(json.getString("name"));

            setmQualities(desiredArray);
            setmAttributes(possessedArray);
            setSuggestions(suggestionArray, User.Type.company);
        }



    }

    public void removeQualAttr(String qualAttrType, String qualAttr){
        if(qualAttrType.equals("desired")){
            if(mUser.getmType() == User.Type.student){

            }
            else{

            }

        }
        else if(qualAttrType.equals("possessed")){
            if(mUser.getmType() == User.Type.student){

            }
            else{

            }
        }

    }

    public void removeQuality(){

    }

    public void setAllQualAttrs(JSONObject json)throws JSONException{
        JSONArray qualities;
        mAllStudentQualities = new ArrayList<>();
        mHasQuals = new ArrayList<>();
        if(json.has("qualities")){
            qualities = json.getJSONArray("qualities");
            int size = qualities.length();
            for(int i = 0; i < size; i++){
                QualAttr qual = new QualAttr(qualities.getJSONObject(i));
                mAllStudentQualities.add(i, qual);
                mHasQuals.add(hasQual(qual.getmId()));
                Log.d(TAG, "just added quality "+
                        mAllStudentQualities.get(i).getmName()+
                        " with id "+mAllStudentQualities.get(i).getmId()+
                        " to index "+i+" as "+mHasQuals.get(i));
            }
        }


        JSONArray attributes;
        mAllCompanyAttributes = new ArrayList<>();
        mHasAttrs = new ArrayList<>();
        if(json.has("attributes")) {
            attributes = json.getJSONArray("attributes");
            int size = attributes.length();
            for (int i = 0; i < size;i++) {
                QualAttr attr = new QualAttr(attributes.getJSONObject(i));
                mAllCompanyAttributes.add(i, attr);
                mHasAttrs.add(hasAttr(attr.getmId()));

                Log.d(TAG, "just added attribute "+
                        mAllCompanyAttributes.get(i).getmName()+
                        " with id "+mAllCompanyAttributes.get(i).getmId()+
                        " to index "+i+" as "+mHasAttrs.get(i));
            }
        }
        logHas();
    }

    public List getmAllCompanyAttributes(boolean data) {
        if(data)
            return mAllCompanyAttributes;
        else
            return mHasAttrs;
    }


    public List getmAllStudentQualities(boolean data) {
        if(data)
            return mAllStudentQualities;
        else
            return mHasQuals;

    }

    public List getAllPossibleDesired(boolean data){
        if(mUser.getmType() == User.Type.student)
            return getmAllCompanyAttributes(data);
        else
            return getmAllStudentQualities(data);
    }

    public List getAllPossiblePossessed(boolean data){
        if(mUser.getmType() == User.Type.company)
            return getmAllCompanyAttributes(data);
        else
            return getmAllStudentQualities(data);
    }

    public List getAllPossibleQualAttrs(String qualattrType, boolean data){
        if(qualattrType.equals(QUALATTR_TYPE_DESIRED))
            return getAllPossibleDesired(data);
        else
            return getAllPossiblePossessed(data);

    }

    public void setmQualities(List<Integer> posList){
        List<QualAttr> qualities = new ArrayList<>();
        if(posList != null) {
            if(!posList.isEmpty()){
                for (int i = posList.size(); --i >= 0; ) {
                    qualities.add(mAllStudentQualities.get(posList.get(i)));
                }
                mUser.setmQualities(qualities);
            }
            else{
                mUser.setmQualities(null);
            }
        }
    }

    public void setmQualities(JSONArray qualArray)throws JSONException{
        List<QualAttr> qualities = new ArrayList<>();
        if(qualArray != null) {
            for (int i = qualArray.length(); --i >= 0; ) {
                qualities.add(new QualAttr(qualArray.getJSONObject(i)));
            }
            mUser.setmQualities(qualities);
        }
    }

    public void setmAttributes(List<Integer>posList){
        List<QualAttr> attributes = new ArrayList<>();

        if(posList != null) {
            if(!posList.isEmpty()){
                for (int i = posList.size(); --i >= 0; )
                    attributes.add(mAllCompanyAttributes.get(posList.get(i)));

                mUser.setmAttributes(attributes);
            }
            else{
                mUser.setmAttributes(null);
            }
        }
    }

    public void setmAttributes(JSONArray attrArray)throws JSONException{
        List<QualAttr> attributes = new ArrayList<>();

        if(attrArray != null) {
            for (int i = attrArray.length(); --i >= 0; )
                attributes.add(new QualAttr(attrArray.getJSONObject(i)));

            mUser.setmAttributes(attributes);
        }
    }

    public boolean hasQual(int id){
        List<QualAttr> quals;

        if(mUser.getmType() == User.Type.company)
            quals = mUser.getDesired();
        else
            quals = mUser.getPossessed();

        if(quals == null)
            return false;

        for(int i = quals.size(); --i >=0; ){
            if(quals.get(i).getmId() == id)
                return true;
        }
        return false;
    }

    public boolean hasAttr(int id){
        List<QualAttr> attrs;

        if(mUser.getmType() == User.Type.student)
            attrs = mUser.getDesired();
        else
            attrs = mUser.getPossessed();

        if(attrs==null)
            return false;

        for(int i = attrs.size(); --i >=0; ){
            if(attrs.get(i).getmId() == id)
                return true;
        }
        return false;
    }

    public void changeQualAttrs(List<Integer> posList, List<Boolean> newHasQualAttrs, String qualAttrType){
        if(qualAttrType.equals(QUALATTR_TYPE_DESIRED) && getType()==User.Type.student
                || qualAttrType.equals(QUALATTR_TYPE_POSSESSED) && getType()==User.Type.company){
            setmAttributes(posList);
            mHasAttrs = newHasQualAttrs;
        }
        else{
            setmQualities(posList);
            mHasQuals = newHasQualAttrs;
        }
    }


    public void setSuggestions(JSONArray suggArray, User.Type type)throws JSONException{
        List<Suggestion> suggestions = new ArrayList<>();

        if(suggArray != null) {
            Log.d(TAG, "sugglength: "+suggArray.length());
            for (int i = suggArray.length(); --i >= 0; ) {

                suggestions.add(new Suggestion(suggArray.getJSONObject(i), type));
            }

            Log.d(TAG, "setting suggestions to user: "+suggestions.size());
            mUser.setmSuggestions(suggestions);
        }
        else{
            mUser.setmSuggestions(null);
        }
    }

    public List<QualAttr> getPossessed(){
        return mUser.getPossessed();
    }

    public List<QualAttr> getDesired(){
        return mUser.getDesired();
    }

    public List<Suggestion> getSuggestions(){
        Log.d(TAG, "getting suggestions");
        return mUser.getmSuggestions();
    }

    public User.Type getType(){
        return mUser.getmType();
    }

    public int getId(){
        return mUser.getmId();
    }

    public void logHas(){
        int size = mHasAttrs.size();
        Log.d(TAG, "attributes:");
        for(int i = 0; i < size; i++)
            Log.d(TAG, ""+mHasAttrs.get(i));
        size = mHasQuals.size();
        Log.d(TAG, "qualities:");
        for(int i = 0; i < size; i++)
            Log.d(TAG, ""+mHasQuals.get(i));

    }

    public float getGpa(){
        return mUser.getmGpa();
    }

    public String getGender(){
        return mUser.getmGender();
    }

    public String getLevel(){
        return mUser.getmLevel();
    }

}
