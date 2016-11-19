package com.example.deii.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ExpandableListView;

import com.ToxicBakery.viewpager.transforms.CubeOutTransformer;
import com.andexert.library.RippleView;
import com.example.deii.Adapter.CustomPagerAdapter;
import com.example.deii.Models.CategoryModel;
import com.example.deii.Utils.AlertMessage;
import com.example.deii.Utils.CirclePageIndicator;
import com.example.deii.Utils.Constants;
import com.example.deii.Utils.MySharedPereference;
import com.example.deii.Utils.SnackBarCallback;
import com.example.deii.Utils.Utils;
import com.example.deii.trustone.AlertDialogInterface;
import com.example.deii.trustone.NavigationDrawerActivity;
import com.example.deii.trustone.PaymentActivity;
import com.example.deii.trustone.R;
import com.neopixl.pixlui.components.textview.TextView;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Lenovo on 16-10-2015.
 */
public class HomeFragment extends Fragment implements RippleView.OnRippleCompleteListener, Animation.AnimationListener, SnackBarCallback {
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
    private TextView txtStartHereValue, txtHorizonValue, txtMasterHealValue, txtLockedValue;
    private TimerTask task;
    private TextView title;
    int selectedExpandPosition;
    CategoryModel categoryModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.home_fragment, container, false);

        InitViews();

        return view;

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ((NavigationDrawerActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((NavigationDrawerActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        ((NavigationDrawerActivity) getActivity()).mToolbar.setNavigationIcon(R.drawable.menu);
        ((NavigationDrawerActivity) getActivity()).mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((NavigationDrawerActivity) getActivity()).mDrawerLayout.openDrawer(GravityCompat.START);
                ;
            }
        });
    }

    private void InitViews() {

        firstLeftIn = false;
        firstRightIn = false;

        NavigationDrawerActivity.setClassName("H O M E");

        pager = (ViewPager) view.findViewById(R.id.pager);
        adapter = new CustomPagerAdapter(getActivity(), NavigationDrawerActivity.bannerList);
        pager.setAdapter(adapter);
        pager.setPageTransformer(true, new CubeOutTransformer());

        mIndicator = (CirclePageIndicator) view.findViewById(R.id.indicator);
        mIndicator.setViewPager(pager);

        title = (TextView) view.findViewById(R.id.title);
        title.setText(NavigationDrawerActivity.bannerList.get(0).getTitle());

        startHereRipple = (RippleView) view.findViewById(R.id.startHereRipple);
        txtStartHereValue = (TextView) view.findViewById(R.id.txtStartHereValue);

        startHereRipple.setOnRippleCompleteListener(this);

        horizonRipple = (RippleView) view.findViewById(R.id.horizonRipple);
        txtHorizonValue = (TextView) view.findViewById(R.id.txtHorizonValue);

        horizonRipple.setVisibility(View.GONE);
        horizonRipple.setOnRippleCompleteListener(this);

        masterHealRipple = (RippleView) view.findViewById(R.id.masterHealRipple);
        txtMasterHealValue = (TextView) view.findViewById(R.id.txtMasterHealValue);

        masterHealRipple.setVisibility(View.GONE);
        masterHealRipple.setOnRippleCompleteListener(this);

        lockedRipple = (RippleView) view.findViewById(R.id.lockedRipple);
        txtLockedValue = (TextView) view.findViewById(R.id.txtLockedValue);

        lockedRipple.setVisibility(View.GONE);
        lockedRipple.setOnRippleCompleteListener(this);

        for (int i = 0; i < NavigationDrawerActivity.categoryList.size(); i++) {
            switch (i) {
                case 0:
                    startHereRipple.setTag(NavigationDrawerActivity.categoryList.get(i).getName());
                    txtStartHereValue.setText(NavigationDrawerActivity.categoryList.get(i).getName());
                    startHereRipple.setId(i);
                    break;
                case 1:
                    horizonRipple.setTag(NavigationDrawerActivity.categoryList.get(i).getName());
                    txtHorizonValue.setText(NavigationDrawerActivity.categoryList.get(i).getName());
                    horizonRipple.setId(i);
                    break;
                case 2:
                    masterHealRipple.setTag(NavigationDrawerActivity.categoryList.get(i).getName());
                    txtMasterHealValue.setText(NavigationDrawerActivity.categoryList.get(i).getName());
                    masterHealRipple.setId(i);
                    break;
                case 3:
                    lockedRipple.setTag(NavigationDrawerActivity.categoryList.get(i).getName());
                    txtLockedValue.setText(NavigationDrawerActivity.categoryList.get(i).getName());
                    lockedRipple.setId(i);
                    break;
            }
        }

        left_in1 = AnimationUtils.loadAnimation(getActivity(), R.anim.left_in);
        left_in1.setAnimationListener(this);
        right_in1 = AnimationUtils.loadAnimation(getActivity(), R.anim.right_in);
        right_in1.setAnimationListener(this);

        left_in2 = AnimationUtils.loadAnimation(getActivity(), R.anim.left_in);
        left_in2.setAnimationListener(this);
        right_in2 = AnimationUtils.loadAnimation(getActivity(), R.anim.right_in);
        right_in2.setAnimationListener(this);

        startHereRipple.startAnimation(left_in1);
        if (time == null) {
            time = new Timer();
            task = new TimerTask() {
                @Override
                public void run() {
                    handler.sendEmptyMessage(0);
                }
            };
            time.schedule(task, 2000, 2000);
        }
    }

    @Override
    public void onComplete(RippleView rippleView) {

        selectedExpandPosition = rippleView.getId();
        categoryModel = NavigationDrawerActivity.categoryList.get(rippleView.getId());
        if (!categoryModel.getSubcategories().isEmpty()) {
            if (categoryModel.getType().equalsIgnoreCase(NavigationDrawerActivity.TYPE_PAID) && categoryModel.getPaidStatus().equals("0")) {
                String categoryName = (String) rippleView.getTag();
                showSubscriptionAlert(categoryName);

            }else {
                SubCategoryFragment fragment = SubCategoryFragment.newInstance(rippleView.getId(), NavigationDrawerActivity.categoryList.get(rippleView.getId()).getName());
                NavigationDrawerActivity.updateFragment(fragment);
            }
        } else {
            Utils.showAlert(getActivity(), "Coming Soon...", "ALERT");
        }
    }

    private void showSubscriptionAlert(String categoryName) {
        AlertMessage.DialogWithTwoButtons(getActivity(), "ALERT", categoryName + " " + getString(R.string.paid_service_dialog_text) + categoryModel.getSubscription_amount(), new AlertDialogInterface() {
            @Override
            public void Yes() {

                doAction();
            }

            @Override
            public void No() {

            }
        });
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

    @Override
    public void onResume() {
        super.onResume();


    }

    @Override
    public void onStop() {
        super.onStop();
    }

    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            if (NavigationDrawerActivity.bannerList != null) {
                if (NavigationDrawerActivity.bannerList.size() - 1 > pager.getCurrentItem()) {
                    pager.setCurrentItem(pager.getCurrentItem() + 1);
                    title.setText(NavigationDrawerActivity.bannerList.get(pager.getCurrentItem()).getTitle());

                } else {
                    pager.setCurrentItem(0);
                    title.setText(NavigationDrawerActivity.bannerList.get(0).getTitle());
                }
            }
            return false;
        }
    });

    @Override
    public void onDestroy() {
        super.onDestroy();
        time.cancel();
    }

    @Override
    public void doAction() {
        Intent intent = new Intent(getActivity(), PaymentActivity.class);
        intent.putExtra(Constants.CATEGORY_ID, categoryModel.getCategory_id());
        intent.putExtra(Constants.CATEGORY_NAME, categoryModel.getName());
        intent.putExtra(Constants.PRICE, categoryModel.getSubscription_amount());
        intent.putExtra(Constants.PAGE_NO, selectedExpandPosition);
        startActivity(intent);
    }
}
