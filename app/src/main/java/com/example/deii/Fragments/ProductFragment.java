package com.example.deii.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.astuetz.PagerSlidingTabStrip;
import com.example.deii.Adapter.ProductsPagerAdapter;
import com.example.deii.Models.ProductTypeModel;
import com.example.deii.ProductFragments.AudioFragment;
import com.example.deii.ProductFragments.PDFFragment;
import com.example.deii.ProductFragments.PptFragment;
import com.example.deii.ProductFragments.VideosFragment;
import com.example.deii.Utils.CommonFunctions;
import com.example.deii.trustone.NavigationDrawerActivity;
import com.example.deii.trustone.R;
import com.rey.material.widget.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;

/**
 * Created by deii on 12/6/2015.
 */
public class ProductFragment extends Fragment implements ViewPager.OnPageChangeListener {

    private CommonFunctions commonFunctions;
    private ViewPager pager;
    private ProductsPagerAdapter productsPagerAdapter;
    public static int topicID = 0;
    private int listPos = 0;
    private HashSet<Integer> availableproducts = new HashSet<>();
    private String tabTitles[] = new String[]{"VIDEOS", "AUDIOS", "PDF", "PPT"};
    private Fragment[] listOfAllFragments = {new VideosFragment(), new AudioFragment(), new PDFFragment(), new PptFragment()};
    private LinkedList<String> listOfAvailableTabTitle = new LinkedList<>();
    private LinkedList<Fragment> listOfAvailableProducts = new LinkedList<>();

    public static ProductFragment newInstance(int pos, int listPos, String name) {
        ProductFragment myFragment = new ProductFragment();

        Bundle args = new Bundle();
        args.putInt("topicID", pos);
        args.putString("className", name);
        args.putInt("listpos", listPos);
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
        listPos = getArguments().getInt("listpos");
        ArrayList<ProductTypeModel> productTypeModels = TopicFragment.model.get(listPos).getProduct_array();
        for (int i = 0; i < productTypeModels.size(); i++) {
            availableproducts.add(Integer.parseInt(productTypeModels.get(i).getType()));
        }
        for (int i = 0; i < 5; i++) {

            if (availableproducts.contains(i)) {
                listOfAvailableProducts.add(listOfAllFragments[i - 1]);
                listOfAvailableTabTitle.add(tabTitles[i - 1]);
            }
        }


        pager.setOffscreenPageLimit(3);


        productsPagerAdapter = new ProductsPagerAdapter(getActivity().getSupportFragmentManager(), topicID, listOfAvailableTabTitle, listOfAvailableProducts);
        pager.setAdapter(productsPagerAdapter);

        PagerSlidingTabStrip tabsStrip = (PagerSlidingTabStrip) getView().findViewById(R.id.tabs);
        tabsStrip.setTextColor(getResources().getColor(android.R.color.white));
        tabsStrip.setIndicatorColor(getResources().getColor(R.color.sign_up_blue_color));
        tabsStrip.setIndicatorHeight(10);
        tabsStrip.setDividerColor(getResources().getColor(R.color.sign_up_blue_color));
        // Attach the view pager to the tab strip
        tabsStrip.setViewPager(pager);
        pager.setCurrentItem(0);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        Fragment frag = getFragmentManager().findFragmentById(R.id.pager);
        if (frag instanceof AudioFragment) {

        }

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
