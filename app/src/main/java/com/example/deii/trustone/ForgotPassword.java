package com.example.deii.trustone;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.WindowManager;

import com.andexert.library.RippleView;
import com.example.deii.Utils.CallBackInterface;
import com.example.deii.Utils.CallWebService;
import com.example.deii.Utils.CommonFunctions;
import com.example.deii.Utils.Constants;
import com.example.deii.Utils.Utils;
import com.neopixl.pixlui.components.edittext.EditText;
import com.neopixl.pixlui.components.textview.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by deii on 12/13/2015.
 */
public class ForgotPassword extends ActionBarActivity implements CallBackInterface, RippleView.OnRippleCompleteListener {

    @InjectView(R.id.txtSend)
    TextView txtSend;
    @InjectView(R.id.txtCancel)
    TextView txtCancel;
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
        ButterKnife.inject(this);

        InitViews();

    }

    private void InitViews() {

        functions = new CommonFunctions(this);
        tilEmail = (TextInputLayout) findViewById(R.id.tilEmail);
        edtEmail = (EditText) findViewById(R.id.edtEmail);
    }

    @Override
    public void onJsonObjectSuccess(JSONObject object) {
        Utils.showAlert(this, "Email Sent. Kindly check email in your inbox as well as junk folder!", "ALERT");
    }

    @Override
    public void onJsonArrarSuccess(JSONArray array) {

    }

    @Override
    public void onFailure(String str) {
        CommonFunctions.showSnackBarWithoutAction(getCurrentFocus(), str);

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
    }

    @OnClick({R.id.txtSend, R.id.txtCancel})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.txtSend:
                EmailID = edtEmail.getText().toString();
                if (functions.validateEmail(edtEmail, tilEmail)) {
                    CallWebService.getInstance(this).hitJSONObjectVolleyWebService(Constants.WebServices.FORGOT_PASSWORD, createJsonForForgotPassword(), this);
                }
                break;
            case R.id.txtCancel:
                finish();
                break;
        }
    }
}
