package com.example.deii.trustone;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.WindowManager;

import com.andexert.library.RippleView;
import com.example.deii.Utils.CallBackInterface;
import com.example.deii.Utils.CallWebService;
import com.example.deii.Utils.CommonFunctions;
import com.example.deii.Utils.Constants;
import com.example.deii.Utils.Utils;
import com.neopixl.pixlui.components.edittext.EditText;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by deii on 12/13/2015.
 */
public class ForgotPassword extends ActionBarActivity implements CallBackInterface, RippleView.OnRippleCompleteListener {
    private RippleView rippleSend, rippleCancel;
    private EditText edtEmail;
    private CommonFunctions functions;
    private TextInputLayout tilEmail;
    String EmailID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.forgot_password);

        InitViews();

    }

    private void InitViews() {

        functions = new CommonFunctions(this);

        rippleSend = (RippleView) findViewById(R.id.rippleSend);
        rippleSend.setOnRippleCompleteListener(this);
        rippleCancel = (RippleView) findViewById(R.id.rippleCancel);
        rippleCancel.setOnRippleCompleteListener(this);

        tilEmail = (TextInputLayout) findViewById(R.id.tilEmail);
        edtEmail = (EditText) findViewById(R.id.edtEmail);
    }

    @Override
    public void onJsonObjectSuccess(JSONObject object) {
        Utils.showAlert(this, "Email Sent. Kindly check email in your inbox as well as junk folder!","ALERT");
    }

    @Override
    public void onJsonArrarSuccess(JSONArray array) {

    }

    @Override
    public void onFailure(String str) {
        CommonFunctions.showSnackBarWithoutAction(getCurrentFocus(),str);

    }

    private HashMap<String, String> createJsonForForgotPassword() {
        HashMap<String, String> outerJsonObject = new HashMap<String, String>();
        try {

            outerJsonObject.put(Constants.EMAIL_ID, EmailID);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return outerJsonObject;
    }

    @Override
    public void onComplete(RippleView rippleView) {
        if (rippleView == rippleSend) {

            EmailID = edtEmail.getText().toString();
            if (functions.validateEmail(edtEmail, tilEmail)) {
                CallWebService.getInstance(this).hitJSONObjectVolleyWebService(Constants.WebServices.FORGOT_PASSWORD, createJsonForForgotPassword(), this);
            }

        } else {
            finish();
        }

    }
}
