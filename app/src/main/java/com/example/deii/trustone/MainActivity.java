package com.example.deii.trustone;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;

import com.andexert.library.RippleView;
import com.example.deii.Utils.CallBackInterface;
import com.example.deii.Utils.CallWebService;
import com.example.deii.Utils.CommonFunctions;
import com.example.deii.Utils.Constants;
import com.example.deii.Utils.MySharedPereference;
import com.neopixl.pixlui.components.edittext.EditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;


public class MainActivity extends ActionBarActivity implements RippleView.OnRippleCompleteListener, CallBackInterface {

    RippleView rippleSignUp, rippleLogIn;
    private EditText edtEmail, edtPassword;
    private CommonFunctions functions;
    private TextInputLayout tilEmail, tilPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        InitViews();

    }

    private void InitViews() {

        rippleSignUp = (RippleView) findViewById(R.id.rippleSignUp);
        rippleSignUp.setOnRippleCompleteListener(this);

        rippleLogIn = (RippleView) findViewById(R.id.rippleLogIn);
        rippleLogIn.setOnRippleCompleteListener(this);


        tilEmail = (TextInputLayout) findViewById(R.id.tilEmail);
        edtEmail = (EditText) findViewById(R.id.edtEmail);
        edtEmail.addTextChangedListener(new MyTextWatcher(edtEmail));

        tilPassword = (TextInputLayout) findViewById(R.id.tilPassword);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
        edtPassword.addTextChangedListener(new MyTextWatcher(edtPassword));

        functions = new CommonFunctions(this);


    }

    @Override
    public void onComplete(RippleView rippleView) {
        if (rippleView == rippleSignUp) {
            Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
            startActivity(intent);
        } else if (rippleView == rippleLogIn) {


            if (!functions.validateEmail(edtEmail, tilEmail)) {
                return;
            }

            if (!functions.validatePhone(edtPassword, tilPassword)) {
                return;
            }

            callSignInWebService();

        }
    }

    @Override
    public void onJsonObjectSuccess(JSONObject object) {
        parseAndSaveDataInPreference(object);
    }

    @Override
    public void onJsonArrarSuccess(JSONArray array) {

    }

    @Override
    public void onFailure(String str) {
        CommonFunctions.showSnackBarWithoutAction(this.getCurrentFocus(), str);
    }

    private void callSignInWebService() {

        CallWebService.getInstance(this).hitJSONObjectVolleyWebService(Constants.WebServices.HOME, createJsonForSignIN(), this);
    }

    private HashMap<String, String> createJsonForSignIN() {
        HashMap<String, String> outerJsonObject = new HashMap<String, String>();
        try {

            outerJsonObject.put(Constants.USERNAME, edtEmail.getText().toString());
            outerJsonObject.put(Constants.PASSWORD, edtPassword.getText().toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return outerJsonObject;
    }

    private void parseAndSaveDataInPreference(JSONObject response) {
        try {

            JSONObject Data = response.getJSONObject(Constants.DATA);
            MySharedPereference.getInstance().setString(this, Constants.EMAIL_ID, Data.getString(Constants.EMAIL_ID));
            MySharedPereference.getInstance().setString(this, Constants.PHONE_NUMBER, Data.getString(Constants.PHONE_NUMBER));
            MySharedPereference.getInstance().setString(this, Constants.USERNAME, Data.getString(Constants.USERNAME));
            MySharedPereference.getInstance().setString(this, Constants.PROFILE_IMAGE, Data.getString(Constants.PROFILE_IMAGE));

            Intent intent = new Intent(MainActivity.this, NavigationDrawerActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {

                case R.id.edtEmail:
                    functions.validateEmail(edtEmail, tilEmail);
                    break;
                case R.id.edtPhone:
                    functions.validatePassword(edtPassword, tilPassword);
                    break;
            }
        }
    }
}