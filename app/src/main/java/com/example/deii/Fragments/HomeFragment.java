package com.example.deii.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.andexert.library.RippleView;
import com.example.deii.trustone.NavigationDrawerActivity;
import com.example.deii.trustone.R;

/**
 * Created by Lenovo on 16-10-2015.
 */
public class HomeFragment extends Fragment implements RippleView.OnRippleCompleteListener{
    private View view;
    private RippleView startHereRipple, horizonRipple, masterHealRipple, lockedRipple;
    private FragmentManager manager;
    private FragmentTransaction fragmentTransaction;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.home_fragment, container, false);

        InitViews();


        return view;

    }

    private void InitViews() {
        NavigationDrawerActivity.changeClassName("H O M E");
        startHereRipple = (RippleView) view.findViewById(R.id.startHereRipple);
        startHereRipple.setOnRippleCompleteListener(this);

        horizonRipple = (RippleView) view.findViewById(R.id.horizonRipple);
        horizonRipple.setOnRippleCompleteListener(this);

        masterHealRipple = (RippleView) view.findViewById(R.id.masterHealRipple);
        masterHealRipple.setOnRippleCompleteListener(this);

        lockedRipple = (RippleView) view.findViewById(R.id.lockedRipple);
        lockedRipple.setOnRippleCompleteListener(this);
    }

    @Override
    public void onComplete(RippleView rippleView) {
        updateFragment(1,"Sub Cat");
    }

    private void updateFragment(int categoryID,String name){
        manager = getActivity().getSupportFragmentManager();
        fragmentTransaction = manager.beginTransaction();

        SubCategoryFragment fragment = SubCategoryFragment.newInstance(categoryID,name);
        fragmentTransaction.addToBackStack("fragment"+String.valueOf(categoryID));
        fragmentTransaction.replace(R.id.nav_contentframe, fragment);
        fragmentTransaction.commit();
    }
}
