package com.example.deii.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.andexert.library.RippleView;
import com.example.deii.Adapter.SubCatCustomAdapter;
import com.example.deii.trustone.NavigationDrawerActivity;
import com.example.deii.trustone.R;

import java.util.ArrayList;

/**
 * Created by Lenovo on 16-10-2015.
 */
public class SubCategoryFragment extends Fragment implements RippleView.OnRippleCompleteListener {


    private View view;

    private int categoryID;
    private Context instance;
    private String className;
    private ListView my_recycler_view;
    private ArrayList<String> subCatList = new ArrayList<>();
    private RippleView rippleBack;

    public static SubCategoryFragment newInstance(int pos, String name) {
        SubCategoryFragment myFragment = new SubCategoryFragment();

        Bundle args = new Bundle();
        args.putInt("categoryID", pos);
        args.putString("className", name);
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

        rippleBack = (RippleView) view.findViewById(R.id.rippleBack);
        rippleBack.setOnRippleCompleteListener(this);

        SubCatCustomAdapter adapter = new SubCatCustomAdapter(getActivity(), NavigationDrawerActivity.categoryList.get(categoryID).getSubcategories());
        my_recycler_view.setAdapter(adapter);


    }

    @Override
    public void onComplete(RippleView rippleView) {
        if (rippleView == rippleBack) {
            getActivity().onBackPressed();
        }
    }
}
