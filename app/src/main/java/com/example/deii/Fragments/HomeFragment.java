package com.example.deii.Fragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.ToxicBakery.viewpager.transforms.CubeOutTransformer;
import com.andexert.library.RippleView;
import com.example.deii.Adapter.CustomPagerAdapter;
import com.example.deii.Utils.CirclePageIndicator;
import com.example.deii.trustone.NavigationDrawerActivity;
import com.example.deii.trustone.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Lenovo on 16-10-2015.
 */
public class HomeFragment extends Fragment implements RippleView.OnRippleCompleteListener, Animation.AnimationListener {
    private View view;
    private RippleView startHereRipple, horizonRipple, masterHealRipple, lockedRipple;
    private FragmentManager manager;
    private FragmentTransaction fragmentTransaction;
    private Animation left_in1, right_in1, left_in2, right_in2;
    boolean firstLeftIn = false, firstRightIn = false;
    private ViewPager pager;
    private CustomPagerAdapter adapter;
    private CirclePageIndicator mIndicator;
    private Timer time;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.home_fragment, container, false);

        InitViews();

        return view;

    }

    private void InitViews() {

        firstLeftIn = false;
        firstRightIn = false;

        NavigationDrawerActivity.changeClassName("H O M E");

        pager = (ViewPager) view.findViewById(R.id.pager);
        adapter = new CustomPagerAdapter(getActivity(), NavigationDrawerActivity.productsModel);
        pager.setAdapter(adapter);
        pager.setPageTransformer(true, new CubeOutTransformer());

        mIndicator = (CirclePageIndicator) view.findViewById(R.id.indicator);
        mIndicator.setViewPager(pager);

        startHereRipple = (RippleView) view.findViewById(R.id.startHereRipple);
        startHereRipple.setOnRippleCompleteListener(this);

        horizonRipple = (RippleView) view.findViewById(R.id.horizonRipple);
        horizonRipple.setVisibility(View.GONE);
        horizonRipple.setOnRippleCompleteListener(this);

        masterHealRipple = (RippleView) view.findViewById(R.id.masterHealRipple);
        masterHealRipple.setVisibility(View.GONE);
        masterHealRipple.setOnRippleCompleteListener(this);

        lockedRipple = (RippleView) view.findViewById(R.id.lockedRipple);
        lockedRipple.setVisibility(View.GONE);
        lockedRipple.setOnRippleCompleteListener(this);

        left_in1 = AnimationUtils.loadAnimation(getActivity(), R.anim.left_in);
        left_in1.setAnimationListener(this);
        right_in1 = AnimationUtils.loadAnimation(getActivity(), R.anim.right_in);
        right_in1.setAnimationListener(this);

        left_in2 = AnimationUtils.loadAnimation(getActivity(), R.anim.left_in);
        left_in2.setAnimationListener(this);
        right_in2 = AnimationUtils.loadAnimation(getActivity(), R.anim.right_in);
        right_in2.setAnimationListener(this);

        startHereRipple.startAnimation(left_in1);

        time = new Timer();
        time.schedule(task, 2000, 2000);
    }

    @Override
    public void onComplete(RippleView rippleView) {
        updateFragment(1, "Sub Cat");
    }

    private void updateFragment(int categoryID, String name) {
        manager = getActivity().getSupportFragmentManager();
        fragmentTransaction = manager.beginTransaction();

        SubCategoryFragment fragment = SubCategoryFragment.newInstance(categoryID, name);
        fragmentTransaction.addToBackStack("fragment" + String.valueOf(categoryID));
        fragmentTransaction.replace(R.id.nav_contentframe, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {
        if (animation == left_in1) {
            left_in1.cancel();
            horizonRipple.setVisibility(View.VISIBLE);
            horizonRipple.startAnimation(right_in1);
        } else if (animation == right_in1) {
            masterHealRipple.setVisibility(View.VISIBLE);
            masterHealRipple.startAnimation(left_in2);
        } else if (animation == left_in2) {
            left_in2.cancel();
            lockedRipple.setVisibility(View.VISIBLE);
            lockedRipple.startAnimation(right_in2);
        }

    }

    TimerTask task = new TimerTask() {
        @Override
        public void run() {

            handler.sendEmptyMessage(0);

        }
    };

    @Override
    public void onResume() {
        super.onResume();


    }

    @Override
    public void onStop() {
        super.onStop();
        /*if (time != null)
            time.cancel();
            time = null;*/

    }

    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            if (NavigationDrawerActivity.productsModel.size() - 1 > pager.getCurrentItem()) {
                pager.setCurrentItem(pager.getCurrentItem() + 1);
            } else {
                pager.setCurrentItem(0);
            }
            return false;
        }
    });
}
