package com.zalehacks.apps.internmatch;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by zale on 4/03/17.
 */

public class Connector {
    public static final String TAG = "Connector";
    public static final String PATH_LOGIN = "login.php";
    public static final String PATH_GET_QUALATTRS = "getqualattrs.php";
    public static final String PATH_SET_QUALATTRS = "setqualattrs.php";
    public static final String PATH_REGISTER = "registeruser.php";
    public static final String PATH_SEND_REQUEST = "sendrequest.php";
    //public static final Uri SITE = Uri.parse("http://192.168.100.3");
    public static final Uri SITE = Uri.parse("http://internmatch.byethost24.com");


    private Context mContext;

    public Connector(Context context){
        mContext = context;
    }


    public boolean login(Email email, String pass, Context context){
        Map<String, String> params = new HashMap<>();
        Uri loginSite = SITE.buildUpon()
                .appendPath(PATH_LOGIN)
                .build();

        String response;


        Log.d(TAG, "email: "+email.toString());
        params.put("email", email.toString());
        params.put("password",pass);

        try{
            response=connect(loginSite.toString(), params);
        }catch(IOException e){
            Log.e(TAG, "Login connection Error: ", e);
            return false;
        }

        if(response == null)
            return false;

        try {
            Instance.get(context).setUser(new JSONObject(response));

            return getQualAttrs();
        }catch(JSONException e){
            Log.e(TAG, "Error reading JSON ", e);
            return false;
        }
    }

    public boolean register(User u){
        Map<String, String> params = new HashMap<>();
        Uri registerSite = SITE.buildUpon()
                .appendPath(PATH_REGISTER)
                .build();

        String response;

        params.put("usertype", u.getmType().toString());
        params.put("email", u.getEmail().toString());
        params.put("password", u.getmPassword());

        if(u.getmType() == User.Type.student) {
            params.put("fname", u.getmFname());
            params.put("lname", u.getmLname());
            params.put("gender", u.getmGender());
            params.put("level", u.getmLevel());
            params.put("gpa", Float.toString(u.getmGpa()));

            Log.d(TAG, "registering user with gender level gpa: "+ u.getmGender()+ u.getmLevel()+ u.getmGpa());
        }
        else
            params.put("name", u.getmName());

        try {
            response = connect(registerSite.toString(), params);
        }catch(IOException e){
            Log.e(TAG, "Connection Error", e);
            return false;
        }

        if(response == null)
            return false;

        if(response.equals("true")) {
            Log.d(TAG, "register returning true");
            return true;
        }


        Log.d(TAG, "register returning false because response=\""+response+"\"");
        return false;
    }

    private boolean request(int source, int target, User.Type type){
        Uri requestPath = SITE.buildUpon()
                .appendPath(PATH_SEND_REQUEST)
                .build();

        Map<String, String> params = new HashMap<>();

        String response;

        params.put("source", Integer.toString(source));
        params.put("target", Integer.toString(target));
        params.put("type", type.toString());

        try{
            response = connect(requestPath.toString(), params);
        }catch(IOException e){
            return false;
        }

        if(response.equals("true"))
            return true;

        return false;
    }

    public boolean getQualAttrs(){
        Uri path = SITE.buildUpon()
               .appendPath(PATH_GET_QUALATTRS)
               .build();
        try {
            String response = connect(path.toString(), null);

            if(response == null)
                return false;

            try {
                Instance.get(mContext).setAllQualAttrs(new JSONObject(response));
                return true;
            }catch(JSONException e){
                return false;
            }

        }catch(IOException e) {
            return false;
        }
    }

    public boolean setQualAttrs(List<Boolean> hasQualAttrs, String qualAttrType){
        Instance instance = Instance.get(mContext);
        Uri path = SITE.buildUpon()
                .appendPath(PATH_SET_QUALATTRS)
                .build();

        Map<String, String> params = new HashMap<>();
        params.put("id",""+instance.getId());
        params.put("type", instance.getType().toString());
        Log.d(TAG, "Changing type"+qualAttrType);

        List<Integer> posList = new ArrayList<>();
        List<QualAttr> qualAttrs = Instance.get(mContext).
                getAllPossibleQualAttrs(qualAttrType, true);

        int size = hasQualAttrs.size();
        for(int i = 0; i < size; i++){
            if(hasQualAttrs.get(i)){
                posList.add(i);
                params.put(qualAttrType+"["+i+"]", ""+qualAttrs.get(i).getmId());
            }
        }

        if(posList.isEmpty())
            params.put(qualAttrType, "");

        String response;
        try {
            response = connect(path.toString(), params);
        }
        catch(IOException e){
            Log.e(TAG, "Error connecting to change qualattr",e);
            return false;
        }

        if(!response.equals("error")) {
            try{
                JSONObject jsonObject = new JSONObject(response);
                if(!jsonObject.isNull("suggestions"))
                    instance.setSuggestions(new JSONObject(response).getJSONArray("suggestions"), instance.getType());
                else
                    instance.setSuggestions(null, instance.getType());
            }
            catch(JSONException e){
                Log.e(TAG,"Error getting suggestions",e);
                return false;
            }
            instance.changeQualAttrs(posList, hasQualAttrs, qualAttrType);
            return true;
        }

        return false;
    }

    private String connect(String urlString, Map<String, String> params) throws IOException{
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(mContext);
        String cookie = pref.getString(mContext.getString(R.string.cookie_key), mContext.getString(R.string.cookie_default));
        HttpURLConnection conn = null;
        BufferedReader reader = null;

        try{
            URL url = new URL(urlString);

            conn = (HttpURLConnection) url.openConnection();

            conn.setRequestProperty("USER-AGENT", "Chrome/48.0.2564.109");
            conn.setRequestProperty("Cookie", cookie);
            //conn.setRequestProperty("Cookie", "__test=677be0e51c75e519efd42d40f4f221c5");
            conn.setRequestProperty("ACCEPT-LANGUAGE", "en-US,en;0.5");
            //conn.addRequestProperty("Referer", "http://panel.byethost.com/panel/indexcp.php?option=phpconfig&ttt=-7495075692907245440");

            conn.setDoInput(true);
            conn.setDoOutput(true);

            Log.d(TAG, "connection set");

            OutputStream os;
            if(params != null) {
                conn.setRequestMethod("POST");
                os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(getQuery(params));
                writer.flush();
                writer.close();
                os.close();
            }

            conn.connect();



            Log.d(TAG, "connection connected : "+conn.getInstanceFollowRedirects()+conn.getResponseCode()+ " "+conn.getResponseMessage());

            if(conn.getResponseCode() != HttpURLConnection.HTTP_OK){
                Log.d(TAG, "bad response code: "+ conn.getResponseCode()+conn.getResponseMessage());

                throw new IOException(conn.getResponseMessage()+": with "+urlString);
            }
            InputStream in = conn.getInputStream();
            Log.d(TAG, "got input stream");

            StringBuffer buffer = new StringBuffer();

            if(in == null) {
                Log.d(TAG, "input stream null");
                return null;
            }

            reader = new BufferedReader(new InputStreamReader(in));

            String line;

            while((line = reader.readLine()) != null){
                buffer.append(line+"\n");
            }

            if(buffer.length() == 0) {
                Log.d(TAG, "buffer zero");
                return null;
            }
            String response = buffer.toString();

            Log.d(TAG,"cookie: "+cookie);
            Log.d(TAG, "returning response: \""+response+"\"");


            //Toast.makeText(mContext,cookie, Toast.LENGTH_SHORT).show();

            return response.trim();
        }
        catch(IOException e){
            Log.e(TAG, "Error ",e);
            return null;
        }
        finally{
            if(conn != null)
                conn.disconnect();
            if(reader != null){
                try{
                    reader.close();
                }catch(final IOException e){
                    Log.e(TAG, "Error closing reader ",e);
                }
            }
        }

    }


    private String getQuery(Map<String,String> params) throws UnsupportedEncodingException{
        StringBuilder result = new StringBuilder();
        boolean first = true;

        for (Map.Entry<String, String> pair : params.entrySet()) {
            if(first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(pair.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(pair.getValue(), "UTF-8"));
        }

        return result.toString();
    }

}
