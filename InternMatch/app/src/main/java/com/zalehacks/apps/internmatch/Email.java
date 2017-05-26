package com.zalehacks.apps.internmatch;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zale on 4/03/17.
 */

public class Email {
    private static final String TAG = "Email";

    private List<String> mDomains;
    private String mUsername;

    public Email(String email){
        mDomains = new ArrayList<>();
        StringBuilder temp = new StringBuilder();
        boolean at = false;


        char[] emailArr = email.toCharArray();
        for(char c : emailArr){
            switch(c) {
                case '@':
                    mUsername = temp.toString();
                    temp = new StringBuilder();
                    at = true;
                    break;
                case '.':
                    mDomains.add(temp.toString());
                    temp = new StringBuilder();
                    break;
                default:
                    temp.append(c);
                    break;
            }

        }

        if(!at){
            mUsername = temp.toString();
        }
        else{
            mDomains.add(temp.toString());
        }


    }

    @Override
    public String toString() {
        StringBuilder email = new StringBuilder();
        boolean first = true;

        email.append(mUsername);
        for(String s : mDomains){

            if(first) {
                email.append("@");
                first = false;
            }
            else
                email.append(".");

            email.append(s);
        }
        return email.toString() ;
    }
}
