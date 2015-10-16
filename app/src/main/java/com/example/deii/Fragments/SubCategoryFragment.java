package com.example.deii.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.deii.trustone.NavigationDrawerActivity;
import com.example.deii.trustone.R;

/**
 * Created by Lenovo on 16-10-2015.
 */
public class SubCategoryFragment extends Fragment {
    private View view;
    private int categoryID;
    private Context instance;
    private String className;

    public static SubCategoryFragment newInstance(int pos,String name) {
        SubCategoryFragment myFragment = new SubCategoryFragment();

        Bundle args = new Bundle();
        args.putInt("categoryID", pos);
        args.putString("className",name);
        myFragment.setArguments(args);

        return myFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.home_fragment, container, false);

        InitViews();

        return view;

    }

    private void InitViews() {

        instance = getActivity();
        categoryID = getArguments().getInt("categoryID");

        NavigationDrawerActivity.changeClassName(getArguments().getString("className"));

        if(categoryID/2 == 0)
            view.setBackgroundColor(instance.getResources().getColor(android.R.color.holo_red_light));
        else
            view.setBackgroundColor(instance.getResources().getColor(android.R.color.holo_blue_light));
    }
}
