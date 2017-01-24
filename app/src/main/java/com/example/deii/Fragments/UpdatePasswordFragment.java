package com.example.deii.Fragments;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.andexert.library.RippleView;
import com.example.deii.Utils.CallBackInterface;
import com.example.deii.Utils.CallWebService;
import com.example.deii.Utils.CommonFunctions;
import com.example.deii.Utils.Constants;
import com.example.deii.Utils.MySharedPereference;
import com.example.deii.trustone.NavigationDrawerActivity;
import com.example.deii.trustone.R;
import com.neopixl.pixlui.components.edittext.EditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by deii on 12/14/2015.
 */
public class UpdatePasswordFragment extends Fragment implements CallBackInterface, RippleView.OnRippleCompleteListener {

    private CommonFunctions commonFunctions;
    private TextInputLayout tilPreviousPass, tilNewPass, tilConfirmPass;
    private EditText edtOldPassword, edtNewPassword, edtConfirmPassword;
    private RippleView rippleContinue;
    private String strOldPass, strEmailID;
    private String strNewPassword;

    public static UpdatePasswordFragment newInstance() {
        UpdatePasswordFragment myFragment = new UpdatePasswordFragment();
        return myFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.update_password_fragment, container, false);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        commonFunctions = new CommonFunctions(getActivity());
        commonFunctions.setActionBarWithBackButton(getActivity());

        InitViews();
    }

    private void InitViews() {

        NavigationDrawerActivity.setClassName("Update Password");

        strOldPass = MySharedPereference.getInstance().getString(getActivity(), Constants.PASSWORD);
        strEmailID = MySharedPereference.getInstance().getString(getActivity(), Constants.EMAIL_ID);

        tilPreviousPass = (TextInputLayout) getView().findViewById(R.id.tilPreviousPass);
        tilNewPass = (TextInputLayout) getView().findViewById(R.id.tilNewPass);
        tilConfirmPass = (TextInputLayout) getView().findViewById(R.id.tilConfirmPass);

        edtOldPassword = (EditText) getView().findViewById(R.id.edtOldPassword);
        edtNewPassword = (EditText) getView().findViewById(R.id.edtNewPassword);
        edtConfirmPassword = (EditText) getView().findViewById(R.id.edtConfirmPassword);

        rippleContinue = (RippleView) getView().findViewById(R.id.rippleContinue);
        rippleContinue.setOnRippleCompleteListener(this);
    }

    @Override
    public void onComplete(RippleView rippleView) {

        strNewPassword = edtNewPassword.getText().toString();
        String strConfirmPass = edtConfirmPassword.getText().toString();
        String strOldEnteredPass = edtOldPassword.getText().toString();

        if (!commonFunctions.compareOldPassword(edtOldPassword, tilPreviousPass, strOldPass)) {
            return;
        }
        if (!commonFunctions.validatePassword(edtNewPassword, tilNewPass)) {
            return;
        }
        if (!commonFunctions.validatePassword(edtConfirmPassword, tilConfirmPass)) {
            return;
        }
        if (!strNewPassword.contentEquals(strConfirmPass)) {
            CommonFunctions.showSnackBarWithoutAction(getView(), "Password Mismatch!");
        }

        CallWebServiceToUpdatePassword(strNewPassword);

    }

    private void CallWebServiceToUpdatePassword(String strNewPassword) {

        CallWebService.getInstance(getActivity()).hitJSONObjectVolleyWebService(Constants.WebServices.UPDATE_PASSWORD, createJSONForUpdatePassword(strNewPassword), this);
    }


    private HashMap<String, String> createJSONForUpdatePassword(String newPassword) {
        HashMap<String, String> outerJsonObject = new HashMap<String, String>();
        try {

            outerJsonObject.put(Constants.EMAIL_ID, strEmailID);
            outerJsonObject.put(Constants.PASSWORD, newPassword);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return outerJsonObject;
    }

    @Override
    public void onJsonObjectSuccess(JSONObject object) {


        try {
            MySharedPereference.getInstance().setString(getActivity(), Constants.PASSWORD, strEmailID);
            CommonFunctions.showSnackBarWithoutAction(getView(), object.getString(Constants.MESSAGE));
            getActivity().onBackPressed();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onJsonArrarSuccess(JSONArray array) {

    }

    @Override
    public void onFailure(String str) {
        CommonFunctions.showSnackBarWithoutAction(getView(), str);
    }


}
