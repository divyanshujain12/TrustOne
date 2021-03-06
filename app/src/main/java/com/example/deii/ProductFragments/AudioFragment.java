package com.example.deii.ProductFragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.deii.Adapter.AudioFragmentAdapter;
import com.example.deii.Fragments.ProductFragment;
import com.example.deii.Models.ProductsModel;
import com.example.deii.Utils.CallBackInterface;
import com.example.deii.Utils.CallWebService;
import com.example.deii.Utils.CommonFunctions;
import com.example.deii.Utils.Constants;
import com.example.deii.Utils.ParsingResponse;
import com.example.deii.Utils.RecyclerItemClickListener;
import com.example.deii.trustone.AndroidBuildingMusicPlayerActivity;
import com.example.deii.trustone.PlayAllAudioActivity;
import com.example.deii.trustone.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by deii on 12/6/2015.
 */
public class AudioFragment extends Fragment implements CallBackInterface {

    private CommonFunctions commonFunctions;
    private static int PageNo = 1;
    private int topicID = 0;
    public static ArrayList<ProductsModel> model = null;
    private RecyclerView productsRecycleView;
    private AudioFragmentAdapter audioFragmentAdapter;
    private TextView noDataAvailable;
    private ProgressBar progressBar;
    private LinearLayoutManager llm;
    private boolean loading = true;
    private com.neopixl.pixlui.components.textview.TextView txtPlayAll;


    public static AudioFragment newInstance(int pos, String name) {
        AudioFragment myFragment = new AudioFragment();

        Bundle args = new Bundle();
        args.putInt("topicID", pos);
        myFragment.setArguments(args);

        return myFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.audio_fragment, container, false);


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
        txtPlayAll = (com.neopixl.pixlui.components.textview.TextView) getView().findViewById(R.id.txtPlayAll);
        txtPlayAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), PlayAllAudioActivity.class);
                startActivity(intent);
            }
        });
        llm = new LinearLayoutManager(getActivity());
        productsRecycleView.setLayoutManager(llm);

        audioFragmentAdapter = new AudioFragmentAdapter(getActivity(), model);
        productsRecycleView.setAdapter(audioFragmentAdapter);

        productsRecycleView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        // TODO Handle item click

                        String video_id = model.get(position).getUrl();
                        Intent intent = new Intent(getActivity(), AndroidBuildingMusicPlayerActivity.class);
                        intent.putExtra(Constants.DATA, video_id);
                        getActivity().startActivity(intent);


                    }
                })
        );

        CallWebService.getInstance(null).hitJSONObjectVolleyWebService(Constants.WebServices.PRODUCT_BY_ID, createJSONForGetVideos(String.valueOf(topicID)), this);
    }

    @Override
    public void onJsonObjectSuccess(JSONObject object) {
        dataSuccess(true);
        WebServiceCalled(false);
        ParsingResponse resp = new ParsingResponse();
        try {
            model = resp.parseJsonArrayWithJsonObject(object.getJSONArray(Constants.DATA), ProductsModel.class);
            if (model.size() > 1)
                txtPlayAll.setVisibility(View.VISIBLE);

            audioFragmentAdapter.addAllData(model);
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

    public static HashMap<String, String> createJSONForGetVideos(String topicID) {

        HashMap<String, String> videosMap = new HashMap<>();
        videosMap.put(Constants.TOPIC_ID, topicID);
        videosMap.put(Constants.TYPE, "2");
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

    @Override
    public void onDestroy() {
        super.onDestroy();
       model = null;
    }
}