package com.example.deii.ProductFragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.deii.Utils.CallBackInterface;
import com.example.deii.trustone.R;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by deii on 12/6/2015.
 */
public class VideosFragment extends Fragment implements CallBackInterface{

    public static VideosFragment newInstance(int pos, String name) {
        VideosFragment myFragment = new VideosFragment();

        Bundle args = new Bundle();
        args.putInt("topicID", pos);
        myFragment.setArguments(args);

        return myFragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.product_fragment, container, false);



    }

    @Override
    public void onJsonObjectSuccess(JSONObject object) {

    }

    @Override
    public void onJsonArrarSuccess(JSONArray array) {

    }

    @Override
    public void onFailure(String str) {

    }
}
