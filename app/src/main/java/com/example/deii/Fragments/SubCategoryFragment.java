package com.example.deii.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
    private RippleView rippleKnowMore, rippleBack;
    private FragmentManager manager;
    private FragmentTransaction fragmentTransaction;
    private SubCatCustomAdapter adapter;

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

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ((NavigationDrawerActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((NavigationDrawerActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        ((NavigationDrawerActivity) getActivity()).mToolbar.setNavigationIcon(R.drawable.back);
        ((NavigationDrawerActivity) getActivity()).mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //NavigationDrawerActivity.manager.popBackStack();

                getActivity().onBackPressed();
            }
        });


    }

    private void InitViews() {

        instance = getActivity();
        categoryID = getArguments().getInt("categoryID");

        NavigationDrawerActivity.setClassName(getArguments().getString("className"));

        my_recycler_view = (ListView) view.findViewById(R.id.my_recycler_view);

        rippleBack = (RippleView) view.findViewById(R.id.rippleBack);
        rippleBack.setOnRippleCompleteListener(this);

        rippleKnowMore = (RippleView) view.findViewById(R.id.rippleKnowMore);
        rippleKnowMore.setOnRippleCompleteListener(this);

        adapter = new SubCatCustomAdapter(getActivity(), NavigationDrawerActivity.categoryList.get(categoryID).getSubcategories());
        my_recycler_view.setAdapter(adapter);


    }

    @Override
    public void onComplete(RippleView rippleView) {

        if (rippleView == rippleBack) {
            getActivity().onBackPressed();
        } else if (rippleView == rippleKnowMore) {

            int selectedPos = adapter.selectedPosition;
            int subCatID = Integer.parseInt(NavigationDrawerActivity.categoryList.get(categoryID).getSubcategories().get(selectedPos).getSubcategory_id());
            String name = NavigationDrawerActivity.categoryList.get(categoryID).getSubcategories().get(selectedPos).getName();

            TopicFragment fragment = TopicFragment.newInstance(subCatID, name);
            NavigationDrawerActivity.updateFragment(fragment);
        }
    }
}
