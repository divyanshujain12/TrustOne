package com.example.deii.trustone;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.example.deii.Adapter.ExpandableListAdapter;
import com.example.deii.Fragments.SubCategoryFragment;
import com.example.deii.Models.ExpandedMenuModel;
import com.neopixl.pixlui.components.textview.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Lenovo on 16-10-2015.
 */
public class NavigationDrawerActivity extends ActionBarActivity implements ExpandableListView.OnChildClickListener{

    private DrawerLayout mDrawerLayout;

    ExpandableListView expandableList;
    List<ExpandedMenuModel> listDataHeader;
    HashMap<ExpandedMenuModel, List<String>> listDataChild;
    Toolbar mToolbar;
    private ExpandableListAdapter mMenuAdapter;
    private TextView txtName, txtEmail;
    public static TextView txtClassName = null;
    private FragmentManager manager;
    private FragmentTransaction fragmentTransaction;
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
        setUpToolbar();

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        setUpNavDrawer();

        expandableList = (ExpandableListView) findViewById(R.id.navigationmenu);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        txtName = (TextView) navigationView.findViewById(R.id.txtName);
        txtName.setText("Divyanshu Jain");

        txtEmail = (TextView) navigationView.findViewById(R.id.txtEmail);
        txtEmail.setText("DivyanshuJain12@hotmail.com");

        prepareListData();
        mMenuAdapter = new ExpandableListAdapter(NavigationDrawerActivity.this, listDataHeader, listDataChild, expandableList);
        // setting list adapter
        expandableList.setAdapter(mMenuAdapter);
        expandableList.setOnChildClickListener(this);
    }



    private void setUpToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        txtClassName = (TextView) mToolbar.findViewById(R.id.txtClassName);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
        }
    }

    private void prepareListData() {
        listDataHeader = new ArrayList<ExpandedMenuModel>();
        listDataChild = new HashMap<ExpandedMenuModel, List<String>>();

        ExpandedMenuModel item1 = new ExpandedMenuModel();
        item1.setIconName("Category");
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

    private void updateFragment(int categoryID,String name){
        manager = getSupportFragmentManager();
        fragmentTransaction = manager.beginTransaction();

        SubCategoryFragment fragment = SubCategoryFragment.newInstance(categoryID,name);
        fragmentTransaction.addToBackStack("fragment"+String.valueOf(categoryID));
        fragmentTransaction.replace(R.id.nav_contentframe, fragment);
        fragmentTransaction.commit();
    }
}