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
import com.example.deii.Utils.Utils;
import com.example.deii.trustone.NavigationDrawerActivity;
import com.example.deii.trustone.R;
import com.neopixl.pixlui.components.edittext.EditText;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by deii on 12/17/2015.
 */
public class ContactUsFragment extends Fragment implements CallBackInterface, RippleView.OnRippleCompleteListener {

    private CommonFunctions commonFunctions;
    private TextInputLayout tilSubject, tilMessage;
    private EditText edtSubject, edtMessage;
    private RippleView rippleContinue;

    public static ContactUsFragment newInstance() {
        ContactUsFragment myFragment = new ContactUsFragment();
        return myFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.contact_us_fragment, container, false);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        commonFunctions = new CommonFunctions(getActivity());
        commonFunctions.setActionBarWithBackButton(getActivity());

        InitViews();
    }

    private void InitViews() {

        NavigationDrawerActivity.setClassName("Contact Us");

        tilSubject = (TextInputLayout) getView().findViewById(R.id.tilSubject);
        tilMessage = (TextInputLayout) getView().findViewById(R.id.tilMessage);

        edtSubject = (EditText) getView().findViewById(R.id.edtsubject);
        edtMessage = (EditText) getView().findViewById(R.id.edtMessage);

        rippleContinue = (RippleView) getView().findViewById(R.id.rippleContinue);
        rippleContinue.setOnRippleCompleteListener(this);

    }

    @Override
    public void onJsonObjectSuccess(JSONObject object) {

        Utils.showGoBackAlert(getActivity(), object.optString(Constants.MESSAGE));

    }

    @Override
    public void onJsonArrarSuccess(JSONArray array) {

    }

    @Override
    public void onFailure(String str) {
        CommonFunctions.showSnackBarWithoutAction(getView(), str);


    }

    @Override
    public void onComplete(RippleView rippleView) {
        String strSubject = edtSubject.getText().toString();
        String strMessage = edtMessage.getText().toString();


        if (!commonFunctions.validateEmpty(edtSubject, tilSubject)) {
            return;
        }
        if (!commonFunctions.validatePassword(edtMessage, tilMessage)) {
            return;
        }

        CallWebServiceToSendEnquiry(strSubject, strMessage);
    }

    private void CallWebServiceToSendEnquiry(String Subject, String message) {
        String EmailID = MySharedPereference.getInstance().getString(getActivity(), Constants.EMAIL_ID);
        String number = MySharedPereference.getInstance().getString(getActivity(), Constants.PHONE_NUMBER);
        String name = MySharedPereference.getInstance().getString(getActivity(), Constants.USERNAME);

        CallWebService.getInstance(getActivity()).hitJSONObjectVolleyWebService(Constants.WebServices.CONTACT_US, getContactUsJson(EmailID, number, name, Subject, message), this);


    }

    private HashMap<String, String> getContactUsJson(String Email, String number, String name, String subject, String message) {

        HashMap<String, String> map = new HashMap<>();
        map.put("name", name);
        map.put("email", Email);
        map.put("subject", subject);
        map.put("phone", number);
        map.put("message", message);

        return map;
    }
}
