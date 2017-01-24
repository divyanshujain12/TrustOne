package com.example.deii.trustone;

import android.content.Intent;
import android.graphics.Bitmap;
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
import android.widget.LinearLayout;

import com.android.volley.VolleyError;
import com.example.deii.Adapter.ExpandableListAdapter;
import com.example.deii.Fragments.ContactUsFragment;
import com.example.deii.Fragments.HomeFragment;
import com.example.deii.Fragments.MyOrdersFragment;
import com.example.deii.Fragments.SubCategoryFragment;
import com.example.deii.Fragments.TopicFragment;
import com.example.deii.Fragments.UpdatePasswordFragment;
import com.example.deii.Fragments.UpdateProfileFragment;
import com.example.deii.Models.BannerModel;
import com.example.deii.Models.CategoryModel;
import com.example.deii.Models.ExpandedMenuModel;
import com.example.deii.Models.ProductsModel;
import com.example.deii.Models.SubCategoryModel;
import com.example.deii.Utils.AlertMessage;
import com.example.deii.Utils.AnimatedExpandableListView;
import com.example.deii.Utils.CallBackInterface;
import com.example.deii.Utils.CallWebService;
import com.example.deii.Utils.Constants;
import com.example.deii.Utils.MySharedPereference;
import com.example.deii.Utils.ParsingResponse;
import com.example.deii.Utils.RoundedImageView;
import com.example.deii.Utils.SnackBarCallback;
import com.example.deii.Utils.Utils;
import com.neopixl.pixlui.components.textview.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Lenovo on 16-10-2015.
 */
public class NavigationDrawerActivity extends AppCompatActivity implements ExpandableListView.OnChildClickListener, ExpandableListView.OnGroupClickListener, ExpandableListView.OnGroupExpandListener, CallBackInterface, SnackBarCallback {

    public static TextView txtClassName = null;
    public ArrayList<ProductsModel> productsModel;
    public DrawerLayout mDrawerLayout;
    private AnimatedExpandableListView startHereMenu/*, horizonMenu, healerMenu, lockedTopicsMenu*/;
    private List<ExpandedMenuModel> listDataHeader;
    private HashMap<ExpandedMenuModel, ArrayList<String>> listDataChild;
    public Toolbar mToolbar;
    private LinearLayout headerLL;
    // private ArrayList<ExpandableListView> expandableListViewsList;
    private ExpandableListAdapter mMenuAdapter;
    private int selectedExpandPosition = -1;
    public static TextView txtName, txtEmail;


    private static FragmentManager manager;
    private FragmentTransaction fragmentTransaction;
    private String EmailID = "";
    static ActionBar actionBar;
    // public  ArrayList<SubCategoryModel> model;
    public static ArrayList<CategoryModel> categoryList;
    private boolean isFirstLogin = true;
    public static ArrayList<BannerModel> bannerList;
    private ImageView profileSetting;
    public static RoundedImageView profileImage;
    public static final String TYPE_PAID = "Paid";
    public static final String TYPE_UNPAID = "Unpaid";
    private CategoryModel categoryModel;

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

        categoryList = new ArrayList<>();
        bannerList = new ArrayList<>();

        setUpToolbar();

        setUpNavDrawer();

        setUpNavDrawerHeader();

        callWebServiceForHome();


    }

    // setting up drawer Header with loginned user Detail
    private void setUpNavDrawerHeader() {

        String imageUrl = MySharedPereference.getInstance().getString(this, Constants.PROFILE_IMAGE);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        profileSetting = (ImageView) navigationView.findViewById(R.id.profileSetting);
        profileSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateFragment(new UpdateProfileFragment());
                mDrawerLayout.closeDrawers();
            }
        });
        profileImage = (RoundedImageView) navigationView.findViewById(R.id.imgProfile);

        /*ImageLoader loader = new ImageLoader(this);
        loader.DisplayImage(imageUrl, profileImage);*/
        MyApplication.getInstance(this).getImageLoader().get(imageUrl, new com.android.volley.toolbox.ImageLoader.ImageListener() {
            @Override
            public void onResponse(com.android.volley.toolbox.ImageLoader.ImageContainer response, boolean isImmediate) {
                Bitmap bitmap = response.getBitmap();
                if (bitmap != null)
                    profileImage.setImageBitmap(bitmap);
            }

            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        headerLL = (LinearLayout) findViewById(R.id.headerLL);

        // headerLL.setBackgroundDrawable(new BitmapDrawable(getResources(), CommonFunctions.blur(Utils.getBitmap("http://fakeimg.pl/350x200/ff0000%2C128/000%2C255/"), 10, 1)));

        txtName = (TextView) navigationView.findViewById(R.id.txtName);
        txtName.setText(MySharedPereference.getInstance().getString(this, Constants.USERNAME));


        EmailID = MySharedPereference.getInstance().getString(this, Constants.EMAIL_ID);
        txtEmail = (TextView) navigationView.findViewById(R.id.txtEmail);
        txtEmail.setText(EmailID);
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

    // setting up Expandable ListView
    private void setUpNavDrawerExpandableList1() {
        prepareListData1();
        mMenuAdapter = new ExpandableListAdapter(NavigationDrawerActivity.this, listDataHeader, listDataChild, null);
        // view.setId(pos);
        // setting list adapter
        startHereMenu.setAdapter(mMenuAdapter);
        startHereMenu.setOnChildClickListener(this);
        startHereMenu.setOnGroupClickListener(this);
        startHereMenu.setOnGroupExpandListener(this);

    }

    // Prepare DataList for Expandable List Group And Child
    private void prepareListData1() {
        listDataHeader = new ArrayList<ExpandedMenuModel>();
        listDataChild = new HashMap<ExpandedMenuModel, ArrayList<String>>();
        ArrayList<String> child = null;

        for (int i = 0; i < categoryList.size(); i++) {
            child = new ArrayList<>();
            for (SubCategoryModel mod : categoryList.get(i).getSubcategories()) {
                child.add(mod.getName());
            }
            ExpandedMenuModel item1 = new ExpandedMenuModel();
            item1.setIconName(categoryList.get(i).getName());
            item1.setIconImg(R.drawable.list);
            // Adding data header
            listDataHeader.add(item1);
            listDataChild.put(item1, child);
        }
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
        categoryModel = categoryList.get(i);
        int pos = Integer.parseInt(categoryModel.getSubcategories().get(i1).getSubcategory_id());
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

            categoryList = resp.parseJsonArrayWithJsonObject(categoryArray, CategoryModel.class);
            setUpNavDrawerExpandableList1();

            JSONArray productsArray = data.getJSONArray(Constants.PRODUCTS);
            productsModel = resp.parseJsonArrayWithJsonObject(productsArray, ProductsModel.class);

            bannerList = resp.parseJsonArrayWithJsonObject(data.getJSONArray(Constants.BANNER), BannerModel.class);

            updateFragment(new HomeFragment());

            isFirstLogin = MySharedPereference.getInstance().getBoolean(this, Constants.LOG_IN);
            if (isFirstLogin) {
                Utils.showAlert(this, "The information provided here is expressly intended for Health Care Professionals only and is not necessarily the opinion of Standard Process® or Mediherb®. Statements have not been approved by the FDA, and do not claim to treat or diagnose any disease. The information within the TrustOne app should not be construed as a claim or representation that any formula or procedure mentioned constitutes a specific cure, palliative or ameliorative, for any condition. \n" +
                        "\n" +
                        "Health-related information provided here is not a substitute for medical advice. Neither TrustOne or the speakers, have evaluated the legal status of any products, services or recommendations with respect to state or federal laws, including scope of practice. Trust One and the speakers, do not and cannot accept responsibility for errors or omissions or for any consequences from applications of the information provided, and make no warranty, expressed or implied, with respect to the information provided.", "DISCLAIMER");

                MySharedPereference.getInstance().setBoolean(this, Constants.LOG_IN, false);
            }


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
    public boolean onGroupClick(final ExpandableListView expandableListView, final View view, final int i, long l) {

        selectedExpandPosition = i;
        categoryModel = categoryList.get(i);
        if (!NavigationDrawerActivity.categoryList.get(i).getSubcategories().isEmpty()) {

            if (categoryList.get(i).getType().equalsIgnoreCase(TYPE_PAID) && categoryModel.getPaidStatus().equals("0")) {

                String categoryName = ((TextView) view.findViewById(R.id.submenu)).getText().toString();

                showSubscriptionAlert(expandableListView, i, categoryName);
            } else if (MySharedPereference.getInstance().getBoolean(this, Constants.IS_FIRST_CLICK_IN_MASTER_HEALER) && categoryList.get(i).getCategory_id().equals("9")) {
                showDisclaimerAlert(expandableListView, view, i);
            } else {
                openDrawer(expandableListView, view, i);
            }
        } else {
            Utils.showAlert(this, "Coming Soon...", "ALERT");
        }


        return false;
    }

    private void showSubscriptionAlert(final ExpandableListView expandableListView, final int i, String categoryName) {
        AlertMessage.DialogWithTwoButtons(this, "ALERT", categoryName + " " + getString(R.string.paid_service_dialog_text) + categoryModel.getSubscription_amount(), new AlertDialogInterface() {
            @Override
            public void Yes() {

                expandableListView.collapseGroup(i);
                mDrawerLayout.closeDrawers();
                doAction();
            }

            @Override
            public void No() {
                expandableListView.collapseGroup(i);
            }
        });
    }

    private void openDrawer(ExpandableListView expandableListView, View view, int i) {
        ImageView imgGroupIdicator = (ImageView) view.findViewById(R.id.imgGroupIdicator);
        if (expandableListView.isGroupExpanded(i))
            imgGroupIdicator.setImageResource(R.drawable.arrow_right);
        else
            imgGroupIdicator.setImageResource(R.drawable.arrow_down);
    }

    private void showDisclaimerAlert(final ExpandableListView expandableListView, final View view, final int i) {
        AlertMessage.DialogWithTwoButtons(this, "DISCLAIMER", getString(R.string.disclaimer_alert_msg), new AlertDialogInterface() {
            @Override
            public void Yes() {
                MySharedPereference.getInstance().setBoolean(NavigationDrawerActivity.this, Constants.IS_FIRST_CLICK_IN_MASTER_HEALER, false);
                openDrawer(expandableListView, view, i);
            }

            @Override
            public void No() {
                expandableListView.collapseGroup(i);
            }
        });
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
        int count = manager.getBackStackEntryCount();
        boolean popped = false;
        if (count > 0)
            popped = manager.popBackStackImmediate();
        else
            super.onBackPressed();

    }

    public void UpdatePassword(View v) {

        UpdatePasswordFragment passwordFragment = UpdatePasswordFragment.newInstance();
        updateFragment(passwordFragment);
        mDrawerLayout.closeDrawers();
    }

    public void ContactUs(View v) {
        ContactUsFragment contactFragment = ContactUsFragment.newInstance();
        updateFragment(contactFragment);
        mDrawerLayout.closeDrawers();
    }

    @Override
    public void onGroupExpand(int i) {
        if (selectedExpandPosition != -1
                && i != selectedExpandPosition) {
            startHereMenu.collapseGroup(selectedExpandPosition);
        }
        selectedExpandPosition = i;
    }

    public void onDestroy() {
        super.onDestroy();
        txtName = null;
        txtEmail = null;
        txtClassName = null;
        profileImage = null;
        bannerList = null;
        categoryList = null;
    }

    public void ShareApp(View view) {
        Intent share = new Intent(android.content.Intent.ACTION_SEND);
        share.setType("text/plain");
        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        share.putExtra(Intent.EXTRA_SUBJECT, "App Url");
        share.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=com.freshtech.trustone");
        startActivity(Intent.createChooser(share, "Share App!"));

    }

    @Override
    public void doAction() {
        Intent intent = new Intent(this, PaymentActivity.class);
        intent.putExtra(Constants.CATEGORY_ID, categoryModel.getCategory_id());
        intent.putExtra(Constants.CATEGORY_NAME, categoryModel.getName());
        intent.putExtra(Constants.PRICE, categoryModel.getSubscription_amount());
        intent.putExtra(Constants.PAGE_NO, selectedExpandPosition);
        startActivity(intent);
    }

    public void MyOrder(View view) {
        mDrawerLayout.closeDrawers();
        updateFragment(new MyOrdersFragment());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        SubCategoryFragment fragment = SubCategoryFragment.newInstance(selectedExpandPosition, NavigationDrawerActivity.categoryList.get(selectedExpandPosition).getName());
        NavigationDrawerActivity.updateFragment(fragment);
    }
}