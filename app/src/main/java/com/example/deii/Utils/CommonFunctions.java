package com.example.deii.Utils;

import android.app.Activity;
import android.content.Context;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;

import com.example.deii.trustone.R;
import com.neopixl.pixlui.components.edittext.EditText;

/**
 * Created by deii on 10/12/2015.
 */
public class CommonFunctions {
    private Context context;
    public CommonFunctions(Context context){
        this.context = context;
    }


    public static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public void requestFocus(View view) {
        if (view.requestFocus()) {
            ((Activity) context).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    public boolean validateName(EditText inputName,TextInputLayout inputLayoutName) {
        if (inputName.getText().toString().trim().isEmpty()) {
            inputLayoutName.setError(((Activity) context).getString(R.string.err_msg_name));
            requestFocus(inputName);
            return false;
        } else {
            inputLayoutName.setErrorEnabled(false);
        }

        return true;
    }

    public boolean validateEmail(EditText inputEmail,TextInputLayout inputLayoutEmail) {
        String email = inputEmail.getText().toString().trim();

        if (email.isEmpty() || !isValidEmail(email)) {
            inputLayoutEmail.setError(((Activity) context).getString(R.string.err_msg_email));
            requestFocus(inputEmail);
            return false;
        } else {
            inputLayoutEmail.setErrorEnabled(false);
        }

        return true;
    }

    public boolean validatePassword(EditText inputPassword,TextInputLayout inputLayoutPassword) {
        if (inputPassword.getText().toString().trim().isEmpty()) {
            inputLayoutPassword.setError(((Activity) context).getString(R.string.err_msg_password));
            requestFocus(inputPassword);
            return false;
        } else {
            inputLayoutPassword.setErrorEnabled(false);
        }

        return true;
    }

    public boolean validatePhone(EditText inputPhone,TextInputLayout inputLayoutPhone) {
        try {
            if (Integer.parseInt(inputPhone.getText().toString().trim()) < 10) {
                inputLayoutPhone.setError(((Activity) context).getString(R.string.err_msg_number));
                requestFocus(inputPhone);
                return false;
            } else {
                inputLayoutPhone.setErrorEnabled(false);
            }
        }catch(Exception e){
            e.printStackTrace();
        }

        return true;
    }



}