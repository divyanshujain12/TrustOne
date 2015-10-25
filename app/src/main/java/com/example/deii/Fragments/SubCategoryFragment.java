package com.example.deii.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.deii.Adapter.SubCatCustomAdapter;
import com.example.deii.trustone.NavigationDrawerActivity;
import com.example.deii.trustone.R;

import java.util.ArrayList;

/**
 * Created by Lenovo on 16-10-2015.
 */
public class SubCategoryFragment extends Fragment {
    private View view;
    private int categoryID;
    private Context instance;
    private String className;
    private ListView my_recycler_view;
    private ArrayList<String> subCatList = new ArrayList<>();
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

        view = inflater.inflate(R.layout.subcategory, container, false);

        InitViews();

        return view;

    }

    private void InitViews() {

        instance = getActivity();
        categoryID = getArguments().getInt("categoryID");

        NavigationDrawerActivity.changeClassName(getArguments().getString("className"));

        my_recycler_view = (ListView) view.findViewById(R.id.my_recycler_view);

        subCatList.add("Dr. Michael Dobbins");
        subCatList.add("Product Intro");

        SubCatCustomAdapter adapter = new SubCatCustomAdapter(getActivity(),subCatList);
        my_recycler_view.setAdapter(adapter);


    }
}
