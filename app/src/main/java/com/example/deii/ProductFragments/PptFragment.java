package com.example.deii.ProductFragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.deii.Adapter.PptFragmentAdapter;
import com.example.deii.Fragments.ProductFragment;
import com.example.deii.Models.ProductsModel;
import com.example.deii.Utils.CallBackInterface;
import com.example.deii.Utils.CallWebService;
import com.example.deii.Utils.CommonFunctions;
import com.example.deii.Utils.Constants;
import com.example.deii.Utils.ParsingResponse;
import com.example.deii.Utils.RecyclerItemClickListener;
import com.example.deii.trustone.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by deii on 12/6/2015.
 */
public class PptFragment extends Fragment implements CallBackInterface {

    private CommonFunctions commonFunctions;
    private int PageNo = 1;
    private int topicID = 0;
    private ArrayList<ProductsModel> model = null;
    private RecyclerView productsRecycleView;
    private PptFragmentAdapter pptFragmentAdapter;
    private TextView noDataAvailable;
    private ProgressBar progressBar;
    private LinearLayoutManager llm;
    private boolean loading = true;

    public static PptFragment newInstance(int pos, String name) {
        PptFragment myFragment = new PptFragment();

        Bundle args = new Bundle();
        args.putInt("topicID", pos);
        myFragment.setArguments(args);

        return myFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.videos_fragment, container, false);


    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        InitViews();
    }

    private void InitViews() {


        model = new ArrayList<>();
        topicID = ProductFragment.topicID;
        progressBar = (ProgressBar) getView().findViewById(R.id.progressBar);
        noDataAvailable = (TextView) getView().findViewById(R.id.noDataAvailable);
        productsRecycleView = (RecyclerView) getView().findViewById(R.id.productsRecycleView);
        llm = new LinearLayoutManager(getActivity());
        productsRecycleView.setLayoutManager(llm);

        pptFragmentAdapter = new PptFragmentAdapter(getActivity(), model);
        productsRecycleView.setAdapter(pptFragmentAdapter);

        productsRecycleView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        // TODO Handle item click

                        String video_id = model.get(position).getUrl();
                        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(video_id));
                        getActivity().startActivity(i);


                    }
                })
        );
        productsRecycleView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                /*if (dy > 0) //check for scroll down
                {
                    int visibleItemCount = llm.getChildCount();
                    int totalItemCount = llm.getItemCount();
                    int pastVisiblesItems = llm.findFirstVisibleItemPosition();
                    if (loading) {
                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                            WebServiceCalled(true);
                            CallWebService.getInstance(null).hitJSONObjectVolleyWebService(Constants.WebServices.PRODUCT_BY_ID, createJSONForGetVideos(String.valueOf(topicID)), PptFragment.this);
                        }
                    }
                }*/
            }
        });

        CallWebService.getInstance(null).hitJSONObjectVolleyWebService(Constants.WebServices.PRODUCT_BY_ID, createJSONForGetVideos(String.valueOf(topicID)), this);

    }

    @Override
    public void onJsonObjectSuccess(JSONObject object) {
        dataSuccess(true);
        WebServiceCalled(false);
        ParsingResponse resp = new ParsingResponse();
        try {
            model = resp.parseJsonArrayWithJsonObject(object.getJSONArray(Constants.DATA), ProductsModel.class);
            pptFragmentAdapter.addAllData(model);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onJsonArrarSuccess(JSONArray array) {

    }

    @Override
    public void onFailure(String str) {
        dataSuccess(false);
        loading = false;
    }

    public HashMap<String, String> createJSONForGetVideos(String topicID) {

        HashMap<String, String> videosMap = new HashMap<>();
        videosMap.put(Constants.TOPIC_ID, topicID);
        videosMap.put(Constants.TYPE, "4");
        videosMap.put(Constants.PAGE_NO, String.valueOf(PageNo));
        videosMap.put(Constants.PAGE_SIZE, "100");
        PageNo++;
        return videosMap;

    }

    private void dataSuccess(boolean success) {
        progressBar.setVisibility(View.GONE);
        if (success)
            productsRecycleView.setVisibility(View.VISIBLE);
        else
            noDataAvailable.setVisibility(View.VISIBLE);
    }
    private void WebServiceCalled(boolean yes) {
        if (yes) {
            progressBar.setVisibility(View.VISIBLE);
            loading = false;
        } else {
            progressBar.setVisibility(View.GONE);
            loading = true;
        }

    }
}
