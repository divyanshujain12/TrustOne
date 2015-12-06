package com.example.deii.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.astuetz.PagerSlidingTabStrip;
import com.example.deii.Adapter.ProductsPagerAdapter;
import com.example.deii.Utils.CommonFunctions;
import com.example.deii.trustone.NavigationDrawerActivity;
import com.example.deii.trustone.R;

/**
 * Created by deii on 12/6/2015.
 */
public class ProductFragment extends Fragment {

    private CommonFunctions commonFunctions;
    private ViewPager pager;
    private ProductsPagerAdapter productsPagerAdapter;
    private int topicID = 0;

    public static ProductFragment newInstance(int pos, String name) {
        ProductFragment myFragment = new ProductFragment();

        Bundle args = new Bundle();
        args.putInt("topicID", pos);
        args.putString("className", name);
        myFragment.setArguments(args);

        return myFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.product_fragment, container, false);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        commonFunctions = new CommonFunctions(getActivity());
        commonFunctions.setActionBarWithBackButton(getActivity());

        InitViews();
    }

    private void InitViews() {

        pager = (ViewPager) getView().findViewById(R.id.pager);
        topicID = getArguments().getInt("topicID");
        NavigationDrawerActivity.setClassName(getArguments().getString("className"));



        productsPagerAdapter = new ProductsPagerAdapter(getActivity().getSupportFragmentManager(), topicID);
        pager.setAdapter(productsPagerAdapter);

        PagerSlidingTabStrip tabsStrip = (PagerSlidingTabStrip) getView().findViewById(R.id.tabs);
        tabsStrip.setTextColor(getResources().getColor(android.R.color.white));
        tabsStrip.setIndicatorColor(getResources().getColor(R.color.sign_up_blue_color));
        tabsStrip.setIndicatorHeight(10);
        tabsStrip.setDividerColor(getResources().getColor(R.color.sign_up_blue_color));
        // Attach the view pager to the tab strip
        tabsStrip.setViewPager(pager);
    }
}
