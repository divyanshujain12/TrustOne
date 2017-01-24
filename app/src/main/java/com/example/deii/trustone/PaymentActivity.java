package com.example.deii.trustone;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.andexert.library.RippleView;
import com.example.deii.Fragments.SubCategoryFragment;
import com.example.deii.Utils.AlertMessage;
import com.example.deii.Utils.CallBackInterface;
import com.example.deii.Utils.CallWebService;
import com.example.deii.Utils.CommonFunctions;
import com.example.deii.Utils.Constants;
import com.example.deii.Utils.FourDigitCardFormatWatcher;
import com.example.deii.Utils.MySharedPereference;
import com.example.deii.Utils.SnackBarCallback;
import com.neopixl.pixlui.components.checkbox.CheckBox;
import com.neopixl.pixlui.components.edittext.EditText;
import com.neopixl.pixlui.components.textview.TextView;
import com.rey.material.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by divyanshu on 6/10/2016.
 */
public class PaymentActivity extends AppCompatActivity implements Spinner.OnItemSelectedListener, CompoundButton.OnCheckedChangeListener, CallBackInterface, RippleView.OnRippleCompleteListener, SnackBarCallback {

    @InjectView(R.id.txtProductName)
    TextView txtProductName;
    @InjectView(R.id.txtPrice)
    TextView txtPrice;
    Toolbar toolbar;
    @InjectView(R.id.edtDesc)
    EditText edtDesc;
    @InjectView(R.id.tilDesc)
    TextInputLayout tilDesc;
    @InjectView(R.id.edtStreet)
    EditText edtStreet;
    @InjectView(R.id.tilStreet)
    TextInputLayout tilStreet;
    @InjectView(R.id.edtZip)
    EditText edtZip;
    @InjectView(R.id.tilZip)
    TextInputLayout tilZip;
    @InjectView(R.id.edtCardNumber)
    EditText edtCardNumber;
    @InjectView(R.id.tilCardNumber)
    TextInputLayout tilCardNumber;
    @InjectView(R.id.spMonth)
    Spinner spMonth;
    @InjectView(R.id.spYear)
    Spinner spYear;
    @InjectView(R.id.edtCvv)
    EditText edtCvv;
    @InjectView(R.id.tilCvv)
    TextInputLayout tilCvv;
    @InjectView(R.id.edtName)
    EditText edtName;
    @InjectView(R.id.tilName)
    TextInputLayout tilName;
    @InjectView(R.id.cartLL)
    LinearLayout cartLL;
    @InjectView(R.id.txtSubmit)
    TextView txtSubmit;
    @InjectView(R.id.rippleSubmit)
    RippleView rippleSubmit;
    List<String> yearList = new ArrayList<>();
    List<CharSequence> monthList = new ArrayList<>();
    ArrayAdapter<String> yearAdapter, monthAdapter;
    String categoryID, categoryName, categoryPrice, descriptionString, streetString, zipString, creditcardString, monthString, yearString, cvvString, nameString, autoSubsribe = "1";
    @InjectView(R.id.CBcardSave)
    CheckBox CBcardSave;
    CommonFunctions commonFunctions;
    @InjectView(R.id.back)
    ImageView back;
    private int pos;
    String[] monthArray = null, yearArray = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.payment_activity);
        ButterKnife.inject(this);

        InitViews();

    }

    private void InitViews() {

        categoryID = getIntent().getStringExtra(Constants.CATEGORY_ID);
        categoryName = getIntent().getStringExtra(Constants.CATEGORY_NAME);
        categoryPrice = "$ " + getIntent().getStringExtra(Constants.PRICE);
        pos = getIntent().getIntExtra(Constants.PAGE_NO, -1);

        txtProductName.setText(categoryName);
        txtPrice.setText(categoryPrice);

        rippleSubmit.setOnRippleCompleteListener(this);

        toolbar = (Toolbar) findViewById(R.id.customToolbar);
        setSupportActionBar(toolbar);

        commonFunctions = new CommonFunctions(this);

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);

        for (int i = 0; i < 20; i++) {
            yearList.add(String.valueOf(year + i));
        }

        edtCardNumber.addTextChangedListener(new FourDigitCardFormatWatcher());

        yearAdapter = new ArrayAdapter<>(this, R.layout.spinner_text_view, yearList);
        spYear.setAdapter(yearAdapter);
        spYear.setOnItemSelectedListener(this);

        monthArray = getResources().getStringArray(R.array.month_array);
        monthAdapter = new ArrayAdapter<>(this, R.layout.spinner_text_view, getResources().getStringArray(R.array.month_array));
        spMonth.setAdapter(monthAdapter);
        spMonth.setOnItemSelectedListener(this);

        monthString = "01";
        yearString = String.valueOf(year);

    }


    @Override
    public void onItemSelected(Spinner parent, View view, int position, long id) {

        if (parent == spYear) {
            yearString = yearList.get(position);
            yearString = yearString.substring(2, yearString.length());
        } else if (parent == spMonth) {
            monthString = monthArray[position];

        }

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked)
            autoSubsribe = "1";
        else
            autoSubsribe = "0";

    }

    @Override
    public void onComplete(RippleView rippleView) {
        checkAndPost();
    }

    private void checkAndPost() {
        descriptionString = edtDesc.getText().toString();
        streetString = edtStreet.getText().toString();
        zipString = edtZip.getText().toString();
        creditcardString = edtCardNumber.getText().toString();
        cvvString = edtCvv.getText().toString();
        nameString = edtName.getText().toString();

       /* if (!commonFunctions.validateEmpty(edtDesc, tilDesc))
            return;
        else*/
        if (!commonFunctions.validateEmpty(edtStreet, tilStreet))
            return;
        else if (!commonFunctions.validateEmpty(edtZip, tilZip))
            return;
        else if (!commonFunctions.validateEmpty(edtCardNumber, tilCardNumber))
            return;
        else if (!commonFunctions.validateEmpty(edtCvv, tilCvv))
            return;
        else if (!commonFunctions.validateEmpty(edtName, tilName))
            return;


        CallWebService.getInstance(this).hitJSONObjectVolleyWebService(Constants.WebServices.CHECKOUT, createJsonForPay(), this);
    }

    private HashMap<String, String> createJsonForPay() {
        categoryPrice = categoryPrice.replace("$", "");
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put(Constants.CATEGORY_ID, categoryID);
        hashMap.put(Constants.EMAIL, MySharedPereference.getInstance().getString(this, Constants.EMAIL_ID));
        hashMap.put(Constants.CARD, creditcardString);
        hashMap.put(Constants.EXP, monthString + yearString);
        hashMap.put(Constants.AMOUNT, categoryPrice);
        hashMap.put(Constants.CARD_HOLDER, nameString);
        hashMap.put(Constants.STREET, streetString);
        hashMap.put(Constants.ZIP, zipString);
        // hashMap.put(Constants.DESCRIPTION, descriptionString);
        hashMap.put(Constants.CVV, cvvString);
        //hashMap.put(Constants.AUTO_SUBSCRIBE, autoSubsribe);

        return hashMap;
    }

    @Override
    public void onJsonObjectSuccess(JSONObject object) {
        try {

            NavigationDrawerActivity.categoryList.get(pos).setPaidStatus("1");
            AlertMessage.showAlertDialogWithCallBack(this, getString(R.string.congratulation), object.getString(Constants.MESSAGE), this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onJsonArrarSuccess(JSONArray array) {

    }

    @Override
    public void onFailure(String str) {
        AlertMessage.showAlertDialog(this, "Sorry", str, false);
    }


    @OnClick(R.id.back)
    public void onClick() {
        onBackPressed();
    }

    @Override
    public void doAction() {

        /*SubCategoryFragment fragment = SubCategoryFragment.newInstance(pos, categoryName);
        NavigationDrawerActivity.updateFragment(fragment);
        finish();*/
        Intent intent = new Intent(this, NavigationDrawerActivity.class);
        startActivity(intent);
        finish();
    }
}
