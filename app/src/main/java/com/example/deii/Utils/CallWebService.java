package com.example.deii.Utils;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.deii.trustone.MyApplication;
import com.example.deii.trustone.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Lenovo on 30-10-2015.
 */
public class CallWebService {

    private static Context context = null;

    private static CallWebService instance = null;

    private static CustomProgressDialog progressDialog = null;


    public static CallWebService getInstance(Context context) {
        instance.context = context;
        if (context != null)
            progressDialog = new CustomProgressDialog(context, R.drawable.syc);
        else
            progressDialog = null;
        if (instance == null) {
            instance = new CallWebService();
        }

        return instance;
    }

    public void hitJSONObjectVolleyWebService(String url, HashMap<String, String> json, final CallBackInterface callBackinerface) {
        if (progressDialog != null)
            progressDialog.show();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(json), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    if (response.getBoolean(Constants.STATUS_CODE))
                        callBackinerface.onJsonObjectSuccess(response);
                    else
                        callBackinerface.onFailure(response.optString(Constants.MESSAGE));
                } catch (JSONException e) {
                    callBackinerface.onFailure(e.getMessage());
                    e.printStackTrace();
                }

                if (progressDialog != null)
                    progressDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callBackinerface.onFailure(error.getMessage());
                if (progressDialog != null)
                    progressDialog.dismiss();

            }
        });
        MyApplication.getInstance(context).addToRequestQueue(request);
    }

}
