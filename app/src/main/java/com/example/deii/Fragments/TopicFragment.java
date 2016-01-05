package com.example.deii.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.andexert.library.RippleView;
import com.example.deii.Adapter.TopicFragmentAdapter;
import com.example.deii.Models.TopicModel;
import com.example.deii.Utils.CallBackInterface;
import com.example.deii.Utils.CallWebService;
import com.example.deii.Utils.CommonFunctions;
import com.example.deii.Utils.Constants;
import com.example.deii.Utils.ParsingResponse;
import com.example.deii.Utils.RecyclerItemClickListener;
import com.example.deii.Utils.Utils;
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
public class TopicFragment extends Fragment implements CallBackInterface, RippleView.OnRippleCompleteListener {

    private static int subCatID = 0;
    private static String headerName = "";
    private RecyclerView recyclerTopicView;
    public static ArrayList<TopicModel> model = null;
    private TopicFragmentAdapter adapter;
    private RippleView rippleContinue;
    private CommonFunctions commonFunctions;

    @Override
    public void onResume() {
        super.onResume();

        NavigationDrawerActivity.setClassName(headerName);
        callWebServiceForTopics();
    }

    public static TopicFragment newInstance(int pos, String name) {
        TopicFragment myFragment = new TopicFragment();

        Bundle args = new Bundle();
        args.putInt("subCategoryID", pos);
        args.putString("className", name);
        subCatID = pos;
        headerName = name;
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

        commonFunctions = new CommonFunctions(getActivity());
        commonFunctions.setActionBarWithBackButton(getActivity());

        InitViews();
    }

    private void InitViews() {

        recyclerTopicView = (RecyclerView) getView().findViewById(R.id.recyclerTopicView);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        recyclerTopicView.setLayoutManager(llm);

        rippleContinue = (RippleView) getView().findViewById(R.id.rippleContinue);
        rippleContinue.setOnRippleCompleteListener(this);
        rippleContinue.setVisibility(View.GONE);


        recyclerTopicView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        // TODO Handle item click
                        if (model.get(position).getProduct_array().size() > 0) {
                            ProductFragment fragment = ProductFragment.newInstance(Integer.parseInt(model.get(position).getTopic_id()), position, model.get(position).getName());
                            NavigationDrawerActivity.updateFragment(fragment);
                        } else {
                            Utils.showAlert(getActivity(), "No product Available!","ALERT");
                        }

                    }
                })
        );

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

    @Override
    public void onComplete(RippleView rippleView) {
        if (model.get(adapter.selectedPosition).getProduct_array().size() > 0) {
            ProductFragment fragment = ProductFragment.newInstance(Integer.parseInt(model.get(adapter.selectedPosition).getTopic_id()), adapter.selectedPosition, model.get(adapter.selectedPosition).getName());
            NavigationDrawerActivity.updateFragment(fragment);
        } else {
            Utils.showAlert(getActivity(), "No product Available!","ALERT");
        }
    }

}
