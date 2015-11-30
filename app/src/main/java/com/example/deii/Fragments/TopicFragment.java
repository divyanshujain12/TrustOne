package com.example.deii.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.deii.Utils.CallBackInterface;
import com.example.deii.Utils.CallWebService;
import com.example.deii.Utils.Constants;
import com.example.deii.trustone.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by deii on 11/30/2015.
 */
public class TopicFragment extends Fragment implements CallBackInterface{

    private int subCatID = 0;
    private String headerName = "";
    private RecyclerView recyclerTopicView;

    public static TopicFragment newInstance(int pos, String name) {
        TopicFragment myFragment = new TopicFragment();

        Bundle args = new Bundle();
        args.putInt("subCategoryID", pos);
        args.putString("className", name);
        myFragment.setArguments(args);

        return myFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.topic_fragment, container, false);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        InitViews();
    }

    private void InitViews() {

        subCatID = getArguments().getInt("subCategoryID");
        headerName = getArguments().getString("className");

        recyclerTopicView = (RecyclerView) getView().findViewById(R.id.recyclerTopicView);

    }


    private void callWebServiceForHome() {
        CallWebService.getInstance(getActivity()).hitJSONObjectVolleyWebService(Constants.WebServices.TOPIC_BY_SUB_CAT_ID, createJsonForHome(), this);
    }


    private HashMap<String, String> createJsonForHome() {
        HashMap<String, String> outerJsonObject = new HashMap<String, String>();
        try {

            outerJsonObject.put(Constants.webServiceSendKeys.SUB_CATEGORY_ID, String.valueOf(subCatID));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return outerJsonObject;
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
