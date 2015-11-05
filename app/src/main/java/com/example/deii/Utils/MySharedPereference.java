package com.example.deii.Utils;

import android.content.Context;
import android.content.SharedPreferences;


/**
 * Created by Lenovo on 08-09-2015.
 */
public class MySharedPereference {

    public static MySharedPereference instance;

    private MySharedPereference() {
    }

    public static MySharedPereference getInstance() {
        if (instance == null) {
            instance = new MySharedPereference();
        }
        return instance;
    }

    public void setString(Context context, String Key, String Value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.TRUST_ONE_PREFERENCE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Key, Value);
        editor.commit();
    }


    public String getString(Context context, String Key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.TRUST_ONE_PREFERENCE, Context.MODE_PRIVATE);
        String requestToken = sharedPreferences.getString(Key, "");
        return requestToken;
    }


}
