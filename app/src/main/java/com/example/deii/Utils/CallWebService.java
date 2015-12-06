package com.example.deii.Utils;

import android.content.Context;
import android.support.design.widget.Snackbar;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.deii.trustone.MyApplication;
import com.example.deii.trustone.R;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Lenovo on 30-10-2015.
 */
public class CallWebService {

    private static Context context = null;

    private static CallWebService instance = null;

    private static CustomProgressDialog progressDialog;



    public static CallWebService getInstance(Context context) {
        instance.context = context;
        progressDialog = new CustomProgressDialog(context, R.drawable.syc);
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
                callBackinerface.onJsonObjectSuccess(response);

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
