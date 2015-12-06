package com.example.deii.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.deii.Adapter.TopicFragmentAdapter;
import com.example.deii.Models.TopicModel;
import com.example.deii.Utils.CallBackInterface;
import com.example.deii.Utils.CallWebService;
import com.example.deii.Utils.CommonFunctions;
import com.example.deii.Utils.Constants;
import com.example.deii.Utils.ParsingResponse;
import com.example.deii.trustone.NavigationDrawerActivity;
import com.example.deii.trustone.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by deii on 11/30/2015.
 */
public class TopicFragment extends Fragment implements CallBackInterface {

    private int subCatID = 0;
    private String headerName = "";
    private RecyclerView recyclerTopicView;
    private ArrayList<TopicModel> model = null;
    private TopicFragmentAdapter adapter;

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

        ((NavigationDrawerActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((NavigationDrawerActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        ((NavigationDrawerActivity) getActivity()).mToolbar.setNavigationIcon(R.drawable.back);
        ((NavigationDrawerActivity) getActivity()).mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });

        InitViews();
    }

    private void InitViews() {

        /*NavigationDrawerActivity.getActivityActionBar()
        NavigationDrawerActivity.getActivityActionBar().setDisplayHomeAsUpEnabled(true);*/

        subCatID = getArguments().getInt("subCategoryID");
        headerName = getArguments().getString("className");
        NavigationDrawerActivity.changeClassName(headerName);

        recyclerTopicView = (RecyclerView) getView().findViewById(R.id.recyclerTopicView);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        recyclerTopicView.setLayoutManager(llm);

        callWebServiceForTopics();

    }


    private void callWebServiceForTopics() {
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

        try {
            if (object.getBoolean(Constants.STATUS_CODE))
                parseJsonResonse(object);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onJsonArrarSuccess(JSONArray array) {

    }

    @Override
    public void onFailure(String str) {
        CommonFunctions.showSnackBarWithoutAction(this.getView(), str);

    }

    private void parseJsonResonse(JSONObject object) {
        ParsingResponse resp = new ParsingResponse();
        try {
            model = resp.parseJsonArrayWithJsonObject(object.getJSONArray(Constants.DATA), TopicModel.class);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        adapter = new TopicFragmentAdapter(getActivity(), model);
        recyclerTopicView.setAdapter(adapter);

    }
}
