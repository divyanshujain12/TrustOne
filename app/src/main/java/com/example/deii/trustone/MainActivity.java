package com.example.deii.trustone;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.andexert.library.RippleView;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.deii.Utils.CommonFunctions;
import com.example.deii.Utils.Constants;
import com.example.deii.Utils.Utils;
import com.neopixl.pixlui.components.edittext.EditText;

import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;


public class MainActivity extends ActionBarActivity implements RippleView.OnRippleCompleteListener {

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
        }
        else if(rippleView == rippleLogIn){


            if (!functions.validateEmail(edtEmail, tilEmail)) {
                return;
            }

            if (! functions.validatePhone(edtPassword, tilPassword)) {
                return;
            }

            callSignInWebService();

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

    private void callSignInWebService(){

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,Constants.WebServices.LOG_IN, new JSONObject(createJsonForSignIN()), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    boolean status = response.optBoolean(Constants.STATUS_CODE);
                    Toast.makeText(MainActivity.this, response.getString(Constants.MESSAGE), Toast.LENGTH_SHORT).show();
                }
                catch(Exception e){
                    e.printStackTrace();;
                }
            }
        }, new Response.ErrorListener() {
            @Override
             public void onErrorResponse(VolleyError error) {
                Toast.makeText(getBaseContext(),error.getMessage(),Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        });
        MyApplication.getInstance(this).addToRequestQueue(request);
    }

    private HashMap<String,String> createJsonForSignIN(){
        HashMap<String,String> outerJsonObject = new HashMap<String,String>();
        try {

            outerJsonObject.put(Constants.USERNAME,edtEmail.getText().toString());
            outerJsonObject.put(Constants.PASSWORD,edtPassword.getText().toString());

        }
        catch(Exception e){
            e.printStackTrace();
        }
        return outerJsonObject;
    }
}