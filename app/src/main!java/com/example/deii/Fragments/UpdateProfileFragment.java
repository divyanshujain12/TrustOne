package com.example.deii.Fragments;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.andexert.library.RippleView;
import com.example.deii.Models.UserDetailModel;
import com.example.deii.Utils.CallBackInterface;
import com.example.deii.Utils.CallWebService;
import com.example.deii.Utils.CommonFunctions;
import com.example.deii.Utils.Constants;
import com.example.deii.Utils.ImageLoader;
import com.example.deii.Utils.MySharedPereference;
import com.example.deii.Utils.ParsingResponse;
import com.example.deii.Utils.RoundedImageView;
import com.example.deii.Utils.Utils;
import com.example.deii.trustone.NavigationDrawerActivity;
import com.example.deii.trustone.R;
import com.neopixl.pixlui.components.edittext.EditText;
import com.neopixl.pixlui.components.textview.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;

/**
 * Created by deii on 1/12/2016.
 */
public class UpdateProfileFragment extends Fragment implements CallBackInterface, RippleView.OnRippleCompleteListener, View.OnClickListener {

    private static final int CAMERA_REQUEST = 1414;
    private static final int GALLERY_REQUEST = 2424;
    File profileImageCaptured;
    String imagePath = "";
    private CommonFunctions commonFunctions;
    private EditText edtName, edtPhone;
    private RoundedImageView imgProfile;
    private TextView txtConnect;
    private Dialog dialog;
    private Uri outputFileUri;
    private Bitmap capturedImage;
    private TextInputLayout tilName, tilPhone;

    public static UpdateProfileFragment newInstance() {
        UpdateProfileFragment myFragment = new UpdateProfileFragment();
        return myFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.activity_update_profile, container, false);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        commonFunctions = new CommonFunctions(getActivity());
        commonFunctions.setActionBarWithBackButton(getActivity());
        NavigationDrawerActivity.setClassName("Update Profile");

        InitViews();
    }

    private void InitViews() {

        imgProfile = (RoundedImageView) getView().findViewById(R.id.imgProfile);
        ImageLoader loader = new ImageLoader(getActivity());
        loader.DisplayImage(MySharedPereference.getInstance().getString(getActivity(), Constants.PROFILE_IMAGE), imgProfile);
        imgProfile.setOnClickListener(this);

        edtName = (EditText) getView().findViewById(R.id.edtName);
        edtName.setText(MySharedPereference.getInstance().getString(getActivity(), Constants.USERNAME));
        tilName = (TextInputLayout) getView().findViewById(R.id.tilName);

        edtPhone = (EditText) getView().findViewById(R.id.edtPhone);
        edtPhone.setText(MySharedPereference.getInstance().getString(getActivity(), Constants.PHONE_NUMBER));
        tilPhone = (TextInputLayout) getView().findViewById(R.id.tilPhone);

        txtConnect = (TextView) getView().findViewById(R.id.txtConnect);
        txtConnect.setOnClickListener(this);
    }


    @Override
    public void onJsonObjectSuccess(JSONObject object) {
        try {

            String message = object.getString(Constants.MESSAGE);
            CommonFunctions.showSnackBarWithoutAction(getView(), message);
            if (object.optBoolean(Constants.STATUS_CODE)) {
                UserDetailModel userDetailModel = new ParsingResponse().parseJsonObject(object.getJSONObject(Constants.DATA), UserDetailModel.class);
                MySharedPereference.getInstance().setString(getActivity(), Constants.EMAIL_ID, userDetailModel.getEmailID());
                MySharedPereference.getInstance().setString(getActivity(), Constants.PHONE_NUMBER, userDetailModel.getPhoneNumber());
                MySharedPereference.getInstance().setString(getActivity(), Constants.USERNAME, userDetailModel.getUserName());
                MySharedPereference.getInstance().setString(getActivity(), Constants.PROFILE_IMAGE, userDetailModel.getProfileImage());

                ImageLoader imageLoader = new ImageLoader(getActivity());
                imageLoader.DisplayImage(userDetailModel.getProfileImage(), NavigationDrawerActivity.profileImage);
                NavigationDrawerActivity.txtEmail.setText(userDetailModel.getEmailID());
                NavigationDrawerActivity.txtName.setText(userDetailModel.getUserName());

                getActivity().onBackPressed();
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
    public void onComplete(RippleView rippleView) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgProfile:
                showPicSelectDialog(getActivity());
                break;

            case R.id.txtConnect:

                if (!commonFunctions.validateName(edtName, tilName))
                    return;


                if (!commonFunctions.validatePhone(edtPhone, tilPhone))
                    return;

                CallWebService.getInstance(getActivity()).hitJSONObjectVolleyWebService(Constants.WebServices.UPDATE_PROFILE, createJsonForUpdateProfile(), this);

        }
    }

    private void showPicSelectDialog(final Context ctx) {

        dialog = new Dialog(ctx, R.style.DialogSlideAnim1);
        dialog.setContentView(R.layout.dialog_select_image);
        dialog.getWindow().setBackgroundDrawableResource(
                android.R.color.transparent);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.BOTTOM;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);


        TextView chooseFromGallery = (TextView) dialog
                .findViewById(R.id.text_fromGallery);
        chooseFromGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    loadPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, GALLERY_REQUEST);
                } else
                    startGallery();
            }
        });

        TextView takeFromCamera = (TextView) dialog
                .findViewById(R.id.text_fromCamera);
        takeFromCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                    loadPermissions(Manifest.permission.CAMERA, CAMERA_REQUEST);
                else
                    startCamera();
            }
        });

        TextView imgCancel = (TextView) dialog
                .findViewById(R.id.text_dialogClose);
        imgCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    protected void startCamera() {
        // TODO Auto-generated method stub
        profileImageCaptured = new File(
                Environment.getExternalStorageDirectory() + "/" + "temp.png");
        if (profileImageCaptured.exists())
            profileImageCaptured.delete();

        outputFileUri = Uri.fromFile(profileImageCaptured);
        Intent intent = new Intent(
                android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, CAMERA_REQUEST);

    }

    protected void startGallery() {
        // TODO Auto-generated method stub
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, GALLERY_REQUEST);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_REQUEST
                && resultCode == Activity.RESULT_OK) {

            imagePath = profileImageCaptured.getAbsolutePath();

            capturedImage = Utils.decodeFile(profileImageCaptured);
            capturedImage = Utils.rotateImageBitmap(imagePath, capturedImage);

            imgProfile.setImageBitmap(capturedImage);


        } else {
            if (requestCode == GALLERY_REQUEST
                    && resultCode == Activity.RESULT_OK) {

                Uri selectedImage = data.getData();

                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = getActivity().getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                if (cursor.moveToFirst()) {
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    imagePath = cursor.getString(columnIndex);
                }

                capturedImage = Utils.decodeFile(new File(imagePath));

                capturedImage = Utils.rotateImageBitmap(imagePath, capturedImage);

                imgProfile.setImageBitmap(capturedImage);


            }
        }
    }

    private HashMap<String, String> createJsonForUpdateProfile() {
        HashMap<String, String> outerJsonObject = new HashMap<String, String>();
        try {
            String imageBase64 = "";


            outerJsonObject.put(Constants.EMAIL_ID, MySharedPereference.getInstance().getString(getActivity(), Constants.EMAIL_ID));
            outerJsonObject.put(Constants.USERNAME, edtName.getText().toString());
            outerJsonObject.put(Constants.PHONE_NUMBER, edtPhone.getText().toString());
          /*  outerJsonObject.put("city", edtCity.getText().toString());
            outerJsonObject.put(Constants.PROFESSIONAL_LICENSE, edtLicense.getText().toString());
            outerJsonObject.put(Constants.STATE, stringState);*/
            if (!imagePath.isEmpty())

            {
                imageBase64 = Utils.encodeFileToBase64Binary(new File(imagePath));
            }
            outerJsonObject.put(Constants.PROFILE_IMAGE, imageBase64);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return outerJsonObject;
    }
    private void loadPermissions(String perm, int requestCode) {
        if (ContextCompat.checkSelfPermission(getActivity(), perm) != PackageManager.PERMISSION_GRANTED) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), perm)) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{perm}, requestCode);
            }

        } else {
            switch (requestCode) {
                case CAMERA_REQUEST: {
                    // If request is cancelled, the result arrays are empty.
                    startCamera();
                }
                break;
                case GALLERY_REQUEST: {
                    startGallery();
                }
                break;
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case CAMERA_REQUEST: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // granted
                    startCamera();
                }
                break;
            }
            case GALLERY_REQUEST: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // granted
                    startGallery();
                }
                break;
            }

        }
    }
}
