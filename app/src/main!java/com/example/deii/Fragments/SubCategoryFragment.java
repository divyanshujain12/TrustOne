package com.example.deii.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.andexert.library.RippleView;
import com.example.deii.Adapter.SubCatCustomAdapter;
import com.example.deii.Utils.AlertMessage;
import com.example.deii.Utils.Constants;
import com.example.deii.Utils.MySharedPereference;
import com.example.deii.trustone.AlertDialogInterface;
import com.example.deii.trustone.NavigationDrawerActivity;
import com.example.deii.trustone.R;
import com.neopixl.pixlui.components.textview.TextView;

import java.util.ArrayList;

/**
 * Created by Lenovo on 16-10-2015.
 */
public class SubCategoryFragment extends Fragment implements RippleView.OnRippleCompleteListener {


    private View view;

    private int clickedCategoryPos;
    private Context instance;
    private String className;
    private ListView my_recycler_view;
    private ArrayList<String> subCatList = new ArrayList<>();
    private RippleView rippleKnowMore, rippleBack;
    private FragmentManager manager;
    private FragmentTransaction fragmentTransaction;
    private SubCatCustomAdapter adapter;
    //public static com.neopixl.pixlui.components.textview.TextView txtAbout;

    public static SubCategoryFragment newInstance(int pos, String name) {
        SubCategoryFragment myFragment = new SubCategoryFragment();

        Bundle args = new Bundle();
        args.putInt("clickedCategoryPos", pos);
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
        clickedCategoryPos = getArguments().getInt("clickedCategoryPos");

        NavigationDrawerActivity.setClassName(getArguments().getString("className"));

        //txtAbout = (com.neopixl.pixlui.components.textview.TextView) view.findViewById(R.id.txtabout);

        my_recycler_view = (ListView) view.findViewById(R.id.my_recycler_view);

        rippleBack = (RippleView) view.findViewById(R.id.rippleBack);
        rippleBack.setOnRippleCompleteListener(this);

        rippleKnowMore = (RippleView) view.findViewById(R.id.rippleKnowMore);
        rippleKnowMore.setOnRippleCompleteListener(this);
        rippleKnowMore.setVisibility(View.GONE);

        adapter = new SubCatCustomAdapter(getActivity(), NavigationDrawerActivity.categoryList.get(clickedCategoryPos).getSubcategories());
        my_recycler_view.setAdapter(adapter);

        my_recycler_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                showDescriptionDialog(i);
            }
        });

        if (MySharedPereference.getInstance().getBoolean(getContext(), Constants.IS_FIRST_CLICK_IN_MASTER_HEALER) && NavigationDrawerActivity.categoryList.get(clickedCategoryPos).getCategory_id().equals("9")) {
            showDisclaimerAlert();
        }
    }

    @Override
    public void onComplete(RippleView rippleView) {

        if (rippleView == rippleBack) {
            getActivity().onBackPressed();
        } else if (rippleView == rippleKnowMore) {

            int selectedPos = adapter.selectedPosition;
            int subCatID = Integer.parseInt(NavigationDrawerActivity.categoryList.get(clickedCategoryPos).getSubcategories().get(selectedPos).getSubcategory_id());
            String name = NavigationDrawerActivity.categoryList.get(clickedCategoryPos).getSubcategories().get(selectedPos).getName();

            TopicFragment fragment = TopicFragment.newInstance(subCatID, name);
            NavigationDrawerActivity.updateFragment(fragment);
        }
    }

    private void showDescriptionDialog(final int pos) {
        final android.app.Dialog dialog = new android.app.Dialog(getActivity(), R.style.DialogSlideAnim1);
        dialog.getWindow().setBackgroundDrawableResource(
                android.R.color.transparent);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        // wlp.gravity = Gravity.BOTTOM;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);

        dialog.setContentView(R.layout.subcat_popup);

        dialog.setCancelable(false);

        TextView txtDescription = (TextView) dialog
                .findViewById(R.id.txtDescription);
        ((RippleView) dialog.findViewById(R.id.rippleContinue)).setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {
                dialog.dismiss();
                int selectedPos = pos;
                int subCatID = Integer.parseInt(NavigationDrawerActivity.categoryList.get(clickedCategoryPos).getSubcategories().get(selectedPos).getSubcategory_id());
                String name = NavigationDrawerActivity.categoryList.get(clickedCategoryPos).getSubcategories().get(selectedPos).getName();

                TopicFragment fragment = TopicFragment.newInstance(subCatID, name);
                NavigationDrawerActivity.updateFragment(fragment);
            }
        });

        txtDescription.setText(NavigationDrawerActivity.categoryList.get(clickedCategoryPos).getSubcategories().get(pos).getContent());

        ((ImageView) dialog.findViewById(R.id.canelButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void showDisclaimerAlert() {
        AlertMessage.DialogWithTwoButtons(getContext(), "DISCLAIMER", getString(R.string.disclaimer_alert_msg), new AlertDialogInterface() {
            @Override
            public void Yes() {
                MySharedPereference.getInstance().setBoolean(getActivity(), Constants.IS_FIRST_CLICK_IN_MASTER_HEALER, false);

            }

            @Override
            public void No() {
                getActivity().onBackPressed();
            }
        });
    }
}
