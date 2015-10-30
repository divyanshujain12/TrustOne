package com.example.deii.trustone;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.ExpandableListView;

import com.example.deii.Adapter.ExpandableListAdapter;
import com.example.deii.Fragments.HomeFragment;
import com.example.deii.Fragments.SubCategoryFragment;
import com.example.deii.Models.ExpandedMenuModel;
import com.example.deii.Utils.CallBackInterface;
import com.example.deii.Utils.CallWebService;
import com.example.deii.Utils.Constants;
import com.example.deii.Utils.ImageLoader;
import com.example.deii.Utils.MySharedPereference;
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
public class NavigationDrawerActivity extends ActionBarActivity implements ExpandableListView.OnChildClickListener, CallBackInterface {

    private DrawerLayout mDrawerLayout;

    ExpandableListView startHereMenu, horizonMenu, healerMenu;
    List<ExpandedMenuModel> listDataHeader;
    HashMap<ExpandedMenuModel, List<String>> listDataChild;
    Toolbar mToolbar;
    private ExpandableListAdapter mMenuAdapter;
    private TextView txtName, txtEmail;
    public static TextView txtClassName = null;
    private FragmentManager manager;
    private FragmentTransaction fragmentTransaction;
    private String EmailID = "";

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


        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        startHereMenu = (ExpandableListView) findViewById(R.id.startHereMenu);
        horizonMenu = (ExpandableListView) findViewById(R.id.horizonMenu);
        healerMenu = (ExpandableListView) findViewById(R.id.healerMenu);

        setUpToolbar();

        setUpNavDrawer();

        setUpNavDrawerHeader();

        setUpNavDrawerExpandableList();

        updateHomeFragment(1, "H O M E");

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
    private void setUpNavDrawerExpandableList() {
        prepareListData("Start Here");
        mMenuAdapter = new ExpandableListAdapter(NavigationDrawerActivity.this, listDataHeader, listDataChild, startHereMenu);
        // setting list adapter
        startHereMenu.setAdapter(mMenuAdapter);
        startHereMenu.setOnChildClickListener(this);

        prepareListData("Expand Your Horizon");
        mMenuAdapter = new ExpandableListAdapter(NavigationDrawerActivity.this, listDataHeader, listDataChild, horizonMenu);
        // setting list adapter
        horizonMenu.setAdapter(mMenuAdapter);
        horizonMenu.setOnChildClickListener(this);

        prepareListData("Become Master Healer");
        mMenuAdapter = new ExpandableListAdapter(NavigationDrawerActivity.this, listDataHeader, listDataChild, healerMenu);
        // setting list adapter
        healerMenu.setAdapter(mMenuAdapter);
        healerMenu.setOnChildClickListener(this);
    }

    // Setting Up ToolBar
    private void setUpToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        txtClassName = (TextView) mToolbar.findViewById(R.id.txtClassName);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
        }
    }


    // Prepare DataList for Expandable List Group And Child
    private void prepareListData(String categoryName) {
        listDataHeader = new ArrayList<ExpandedMenuModel>();
        listDataChild = new HashMap<ExpandedMenuModel, List<String>>();

        ExpandedMenuModel item1 = new ExpandedMenuModel();
        item1.setIconName(categoryName);
        item1.setIconImg(R.drawable.list_icon);
        // Adding data header
        listDataHeader.add(item1);


        List<String> heading1 = new ArrayList<String>();
        heading1.add("Submenu 1");
        heading1.add("Submenu 2");
        heading1.add("Submenu 3");
        heading1.add("Submenu 4");
        heading1.add("Submenu 5");
        heading1.add("Submenu 6");
        heading1.add("Submenu 7");
        heading1.add("Submenu 8");
        heading1.add("Submenu 9");
        heading1.add("Submenu 10");

        listDataChild.put(listDataHeader.get(0), heading1);// Header, Child data


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

    public static void changeClassName(String name) {
        txtClassName.setOldDeviceTextAllCaps(true);
        txtClassName.setText(name);
    }

    @Override
    public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {

        updateFragment(i1, ((TextView) view.findViewById(R.id.submenu)).getText().toString());
        mDrawerLayout.closeDrawers();

        return false;
    }

    private void updateFragment(int categoryID, String name) {
        manager = getSupportFragmentManager();
        fragmentTransaction = manager.beginTransaction();

        SubCategoryFragment fragment = SubCategoryFragment.newInstance(categoryID, name);
        fragmentTransaction.addToBackStack("fragment" + String.valueOf(categoryID));
        fragmentTransaction.replace(R.id.nav_contentframe, fragment);
        fragmentTransaction.commit();
    }

    private void updateHomeFragment(int categoryID, String name) {
        manager = getSupportFragmentManager();
        fragmentTransaction = manager.beginTransaction();
        HomeFragment fragment = new HomeFragment();
        // fragmentTransaction.addToBackStack("fragment"+String.valueOf(categoryID));
        fragmentTransaction.replace(R.id.nav_contentframe, fragment);
        fragmentTransaction.commit();
    }

    private void callWebServiceForHome() {
        CallWebService.getInstance(this).hitJSONObjectVolleyWebService(Constants.WebServices.HOME, createJsonForHome(), this);
    }

    private HashMap<String, String> createJsonForHome() {
        HashMap<String, String> outerJsonObject = new HashMap<String, String>();
        try {

            outerJsonObject.put(Constants.EMAIL_ID, EmailID);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return outerJsonObject;
    }

    @Override
    public void onJsonObjectSuccess(JSONObject object) {

    }

    @Override
    public void onJsonArrarSuccess(JSONArray array) {

    }

    @Override
    public void onFailure(String str) {

    }
}