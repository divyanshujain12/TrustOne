package com.example.deii.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.deii.Adapter.MyOrderAdapter;
import com.example.deii.Models.MyOrderModel;
import com.example.deii.Utils.CallBackInterface;
import com.example.deii.Utils.CallWebService;
import com.example.deii.Utils.CommonFunctions;
import com.example.deii.Utils.Constants;
import com.example.deii.Utils.MySharedPereference;
import com.example.deii.Utils.ParsingResponse;
import com.example.deii.trustone.NavigationDrawerActivity;
import com.example.deii.trustone.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by divyanshu on 6/12/2016.
 */
public class MyOrdersFragment extends Fragment implements CallBackInterface {
    @InjectView(R.id.productsRecycleView)
    RecyclerView productsRecycleView;
    @InjectView(R.id.noDataAvailable)
    TextView noDataAvailable;
    private CommonFunctions commonFunctions;

    MyOrderAdapter myOrderAdapter;
    ArrayList<MyOrderModel> myOrderses = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.my_order, container, false);
        ButterKnife.inject(this, view);
        return view;

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        commonFunctions = new CommonFunctions(getActivity());
        commonFunctions.setActionBarWithBackButton(getActivity());
        NavigationDrawerActivity.setClassName("My Orders");

        InitViews();
    }

    private void InitViews() {

        productsRecycleView.setLayoutManager(new LinearLayoutManager(getActivity()));
        CallWebService.getInstance(getActivity()).hitJSONObjectVolleyWebService(Constants.WebServices.GET_ORDERS, createJsonForMyOrders(), this);

    }

    @Override
    public void onJsonObjectSuccess(JSONObject object) {
        ParsingResponse parsingResponse = new ParsingResponse();
        myOrderses = parsingResponse.parseJsonArrayWithJsonObject(object.optJSONArray(Constants.DATA), MyOrderModel.class);
        myOrderAdapter = new MyOrderAdapter(getActivity(), myOrderses);
        productsRecycleView.setAdapter(myOrderAdapter);
    }

    @Override
    public void onJsonArrarSuccess(JSONArray array) {

    }

    @Override
    public void onFailure(String str) {
        noDataAvailable.setVisibility(View.VISIBLE);
        productsRecycleView.setVisibility(View.GONE);
    }

    private HashMap<String, String> createJsonForMyOrders() {
        HashMap<String, String> outerJsonObject = new HashMap<String, String>();
        try {

            outerJsonObject.put(Constants.webServiceSendKeys.EMAIL_ID, MySharedPereference.getInstance().getString(getActivity(), Constants.EMAIL_ID));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return outerJsonObject;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}
