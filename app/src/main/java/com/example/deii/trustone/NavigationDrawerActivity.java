package com.example.deii.trustone;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.ExpandableListView;
import android.widget.ImageView;

import com.example.deii.Adapter.ExpandableListAdapter;
import com.example.deii.Fragments.HomeFragment;
import com.example.deii.Fragments.TopicFragment;
import com.example.deii.Models.CategoryModel;
import com.example.deii.Models.ExpandedMenuModel;
import com.example.deii.Models.ProductsModel;
import com.example.deii.Models.SubCategoryModel;
import com.example.deii.Utils.AnimatedExpandableListView;
import com.example.deii.Utils.CallBackInterface;
import com.example.deii.Utils.CallWebService;
import com.example.deii.Utils.Constants;
import com.example.deii.Utils.ImageLoader;
import com.example.deii.Utils.MySharedPereference;
import com.example.deii.Utils.ParsingResponse;
import com.example.deii.Utils.RoundedImageView;
import com.neopixl.pixlui.components.textview.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Lenovo on 16-10-2015.
 */
public class NavigationDrawerActivity extends AppCompatActivity implements ExpandableListView.OnChildClickListener, ExpandableListView.OnGroupClickListener, CallBackInterface {

    public static TextView txtClassName = null;
    public static ArrayList<ProductsModel> productsModel;
    public DrawerLayout mDrawerLayout;
    private AnimatedExpandableListView startHereMenu, horizonMenu, healerMenu, lockedTopicsMenu;
    private List<ExpandedMenuModel> listDataHeader;
    private HashMap<ExpandedMenuModel, ArrayList<String>> listDataChild;
    public Toolbar mToolbar;
    private ArrayList<ExpandableListView> expandableListViewsList;
    private ExpandableListAdapter mMenuAdapter;
    private TextView txtName, txtEmail;


    private static FragmentManager manager;
    private FragmentTransaction fragmentTransaction;
    private String EmailID = "";
    static ActionBar actionBar;
    // public  ArrayList<SubCategoryModel> model;
    public static ArrayList<CategoryModel> categoryList;

    public static void setClassName(String name) {
        txtClassName.setOldDeviceTextAllCaps(true);
        txtClassName.setText(name);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.navigation_drawer_activity);

         /* to set the menu icon image*/
        InitViews();
    }

    private void InitViews() {

        manager = getSupportFragmentManager();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        startHereMenu = (AnimatedExpandableListView) findViewById(R.id.startHereMenu);
        horizonMenu = (AnimatedExpandableListView) findViewById(R.id.horizonMenu);
        healerMenu = (AnimatedExpandableListView) findViewById(R.id.healerMenu);
        lockedTopicsMenu = (AnimatedExpandableListView) findViewById(R.id.lockedTopicsMenu);
        categoryList = new ArrayList<>();
        productsModel = new ArrayList<>();
        expandableListViewsList = new ArrayList<>();
        expandableListViewsList.add(startHereMenu);
        expandableListViewsList.add(horizonMenu);
        expandableListViewsList.add(healerMenu);
        expandableListViewsList.add(lockedTopicsMenu);

        setUpToolbar();

        setUpNavDrawer();

        setUpNavDrawerHeader();

        callWebServiceForHome();


    }

    // setting up drawer Header with loginned user Detail
    private void setUpNavDrawerHeader() {
        String imageUrl = MySharedPereference.getInstance().getString(this, Constants.PROFILE_IMAGE);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        RoundedImageView profileImage = (RoundedImageView) navigationView.findViewById(R.id.imgProfile);
        ImageLoader loader = new ImageLoader(this);
        loader.DisplayImage(imageUrl, profileImage);

        txtName = (TextView) navigationView.findViewById(R.id.txtName);
        txtName.setText(MySharedPereference.getInstance().getString(this, Constants.USERNAME));


        EmailID = MySharedPereference.getInstance().getString(this, Constants.EMAIL_ID);
        txtEmail = (TextView) navigationView.findViewById(R.id.txtEmail);
        txtEmail.setText(EmailID);
    }

    // setting up Expandable ListView
    private void setUpNavDrawerExpandableList(String CategoryName, ArrayList<SubCategoryModel> subCat, ExpandableListView view, int pos) {
        prepareListData(CategoryName, subCat);
        mMenuAdapter = new ExpandableListAdapter(NavigationDrawerActivity.this, listDataHeader, listDataChild, view);
        view.setId(pos);
        // setting list adapter
        view.setAdapter(mMenuAdapter);
        view.setOnChildClickListener(this);
        view.setOnGroupClickListener(this);


    }

    // Setting Up ToolBar
    private void setUpToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        txtClassName = (TextView) mToolbar.findViewById(R.id.txtClassName);
        actionBar = getSupportActionBar();
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
        }
    }

    // Prepare DataList for Expandable List Group And Child
    private void prepareListData(String categoryName, ArrayList<SubCategoryModel> heading1) {
        listDataHeader = new ArrayList<ExpandedMenuModel>();
        listDataChild = new HashMap<ExpandedMenuModel, ArrayList<String>>();
        ArrayList<String> child = new ArrayList<>();


        for (SubCategoryModel mod : heading1) {
            child.add(mod.getName());
        }


        ExpandedMenuModel item1 = new ExpandedMenuModel();
        item1.setIconName(categoryName);
        item1.setIconImg(R.drawable.list_icon);
        // Adding data header
        listDataHeader.add(item1);

        listDataChild.put(listDataHeader.get(0), child);// Header, Child data


    }

    private void setUpNavDrawer() {
        if (mToolbar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            mToolbar.setNavigationIcon(R.drawable.menu);
            mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDrawerLayout.openDrawer(GravityCompat.START);
                }
            });
        }


    }

    @Override
    public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {

        int expandableListID = expandableListView.getId();

        int pos = Integer.parseInt(categoryList.get(expandableListID).getSubcategories().get(i1).getSubcategory_id());

        TopicFragment fragment = TopicFragment.newInstance(pos, ((TextView) view.findViewById(R.id.submenu)).getText().toString());
        updateFragment(fragment);
        mDrawerLayout.closeDrawers();

        return false;
    }

    public static void updateFragment(Fragment fragment) {

        FragmentTransaction fragmentTransaction = manager.beginTransaction();

        boolean fragShowing = manager.popBackStackImmediate(fragment.getClass().getName(), 0);

        if (!fragShowing) {
            if (!(fragment instanceof HomeFragment))
                fragmentTransaction.addToBackStack(fragment.getClass().getName());
            fragmentTransaction.replace(R.id.nav_contentframe, fragment);
            fragmentTransaction.commit();

        }
    }


    private void updateHomeFragment(int categoryID, String name) {
        manager = getSupportFragmentManager();
        fragmentTransaction = manager.beginTransaction();
        HomeFragment fragment = new HomeFragment();
        // fragmentTransaction.addToBackStack("fragment"+String.valueOf(categoryID));
        fragmentTransaction.replace(R.id.nav_contentframe, fragment);
        fragmentTransaction.commit();
    }

    public void updateTopicFragment(int subCategoryID, String name) {
        manager = getSupportFragmentManager();
        fragmentTransaction = manager.beginTransaction();
        boolean fragShowing = manager.popBackStackImmediate(name, 0);
        if (!fragShowing) {
            TopicFragment fragment = TopicFragment.newInstance(subCategoryID, name);
            fragmentTransaction.addToBackStack(name);
            fragmentTransaction.replace(R.id.nav_contentframe, fragment);
            fragmentTransaction.commit();
        }
    }


    private void callWebServiceForHome() {
        CallWebService.getInstance(this).hitJSONObjectVolleyWebService(Constants.WebServices.HOME, createJsonForHome(), this);
    }


    private HashMap<String, String> createJsonForHome() {
        HashMap<String, String> outerJsonObject = new HashMap<String, String>();
        try {

            outerJsonObject.put(Constants.webServiceSendKeys.EMAIL_ID, EmailID);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return outerJsonObject;
    }


    @Override
    public void onJsonObjectSuccess(JSONObject object) {

        try {
            ArrayList<SubCategoryModel> model = null;
            ParsingResponse resp = new ParsingResponse();

            JSONObject data = object.getJSONObject(Constants.DATA);
            JSONArray categoryArray = data.getJSONArray(Constants.CATEGORIES);


            for (int i = 0; i < categoryArray.length(); i++) {
                 /*
                  parsing categories of response
                 */
                CategoryModel categoryModel = resp.parseJsonObject(categoryArray.getJSONObject(i), CategoryModel.class);

                /*
                    parsing subcategories of the categories
                 */
                model = resp.parseJsonArrayWithJsonObject(categoryArray.getJSONObject(i).getJSONArray(Constants.SUB_CATEGORIES), SubCategoryModel.class);

                categoryModel.setSubcategories(model);

                categoryList.add(categoryModel);

                /*
                setting navigation drawer according name given in response
                 */
                setUpNavDrawerExpandableList(categoryModel.getName(), model, expandableListViewsList.get(i), i);


            }

            JSONArray productsArray = data.getJSONArray(Constants.PRODUCTS);
            productsModel = resp.parseJsonArrayWithJsonObject(productsArray, ProductsModel.class);

            updateFragment(new HomeFragment());


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onJsonArrarSuccess(JSONArray array) {

    }

    @Override
    public void onFailure(String str) {

    }

    @Override
    public boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long l) {

        ImageView imgGroupIdicator = (ImageView) view.findViewById(R.id.imgGroupIdicator);
        if (expandableListView.isGroupExpanded(i))
            imgGroupIdicator.setImageResource(R.drawable.arrow_right);
        else
            imgGroupIdicator.setImageResource(R.drawable.arrow_down);

        return false;
    }


    public void Logout(View v) {
        MySharedPereference.getInstance().clearSharedPreference(this);
        Intent intent = new Intent(this, SplashActivity.class);
        startActivity(intent);
        finish();
    }

    public static ActionBar getActivityActionBar() {

        return actionBar;
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();

    }
}