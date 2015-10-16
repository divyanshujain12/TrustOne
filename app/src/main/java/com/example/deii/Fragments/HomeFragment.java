package com.example.deii.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.deii.trustone.R;

/**
 * Created by Lenovo on 16-10-2015.
 */
public class HomeFragment extends Fragment {
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.home_fragment, container, false);

        InitViews();


        return view;

    }

    private void InitViews() {

    }
}
