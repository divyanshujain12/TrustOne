package com.example.deii.trustone;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.deii.Utils.CommonFunctions;
import com.example.deii.Utils.Constants;
import com.example.deii.Utils.CustomProgressDialog;
import com.example.deii.Utils.RoundedImageView;
import com.example.deii.Utils.Utils;
import com.neopixl.pixlui.components.edittext.EditText;
import com.neopixl.pixlui.components.textview.TextView;

import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;


/**
 * Created by deii on 10/12/2015.
 */
public class SignUpActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    private static final int CAMERA_REQUEST = 1414;
    private static final int GALLERY_REQUEST = 2424;
    File profileImageCaptured;
    String imagePath = "";
    private Toolbar toolbar;
    private EditText edtName, edtPhone, edtEmail, edtCity, edtLicense;
    private RoundedImageView imgProfile;
    private Dialog dialog;
    private Uri outputFileUri;
    private Bitmap capturedImage;
    private CommonFunctions functions;
    private TextInputLayout tilName, tilEmail, tilPhone, tilCity, tilLicense;
    private TextView txtConnect;
    private CustomProgressDialog progressDialog;
    private Spinner spinState;
    private String stringState = "Alabama";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.sign_up_activity);

        InitViews();

    }

    private void InitViews() {

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        //toolbar.setTitle("Sign Up");
        setSupportActionBar(toolbar);

        progressDialog = new CustomProgressDialog(this, R.drawable.syc);
        spinState = (Spinner) findViewById(R.id.spinState);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.state_array, R.layout.text_view);
        adapter.setDropDownViewResource(R.layout.text_view);
        spinState.setAdapter(adapter);
        spinState.setOnItemSelectedListener(this);

        imgProfile = (RoundedImageView) findViewById(R.id.imgProfile);
        imgProfile.setOnClickListener(this);

        txtConnect = (TextView) findViewById(R.id.txtConnect);
        txtConnect.setOnClickListener(this);

        tilName = (TextInputLayout) findViewById(R.id.tilName);
        edtName = (EditText) findViewById(R.id.edtName);
        edtName.addTextChangedListener(new MyTextWatcher(edtName));

        tilEmail = (TextInputLayout) findViewById(R.id.tilEmail);
        edtEmail = (EditText) findViewById(R.id.edtEmail);
        edtEmail.addTextChangedListener(new MyTextWatcher(edtEmail));

        tilPhone = (TextInputLayout) findViewById(R.id.tilPhone);
        edtPhone = (EditText) findViewById(R.id.edtPhone);
        edtPhone.addTextChangedListener(new MyTextWatcher(edtPhone));

        tilCity = (TextInputLayout) findViewById(R.id.tilCity);
        edtCity = (EditText) findViewById(R.id.edtCity);
        edtCity.addTextChangedListener(new MyTextWatcher(edtCity));

        tilLicense = (TextInputLayout) findViewById(R.id.tilLicense);
        edtLicense = (EditText) findViewById(R.id.edtLicense);



        functions = new CommonFunctions(this);
    }

    public void backPressed(View v) {
        onBackPressed();
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
                startGallery();
            }
        });

        TextView takeFromCamera = (TextView) dialog
                .findViewById(R.id.text_fromCamera);
        takeFromCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
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
                Cursor cursor = this.getContentResolver().query(selectedImage,
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgProfile:
                showPicSelectDialog(SignUpActivity.this);
                break;

            case R.id.txtConnect:

                if (!functions.validateName(edtName, tilName)) {
                    return;
                }

                if (!functions.validateEmail(edtEmail, tilEmail)) {
                    return;
                }

                if (!functions.validatePhone(edtPhone, tilPhone)) {
                    return;
                }
                if (!functions.validateCity(edtCity, tilCity))
                    return;
                progressDialog.show();
                JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, Constants.WebServices.SIGN_UP, new JSONObject(createJsonForSignUP()), new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (progressDialog.isShowing() && progressDialog != null)
                                progressDialog.dismiss();

                            boolean status = response.optBoolean(Constants.STATUS_CODE);
                            if (status) {
                                Utils.showEmailAlert(SignUpActivity.this, "Password has been mailed to your email id kindly check it in your inbox as well as junk folder.");
                            }

                            Toast.makeText(SignUpActivity.this, response.getString(Constants.MESSAGE), Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                            ;
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        if (progressDialog.isShowing() && progressDialog != null)
                            progressDialog.dismiss();
                        Toast.makeText(getBaseContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                        error.printStackTrace();
                    }
                });
                MyApplication.getInstance(this).addToRequestQueue(request);
                break;

        }
    }

    private HashMap<String, String> createJsonForSignUP() {
        HashMap<String, String> outerJsonObject = new HashMap<String, String>();
        try {
            String imageBase64 = "";


            outerJsonObject.put(Constants.EMAIL_ID, edtEmail.getText().toString());
            outerJsonObject.put(Constants.USERNAME, edtName.getText().toString());
            outerJsonObject.put(Constants.PHONE_NUMBER, edtPhone.getText().toString());
            outerJsonObject.put("city", edtCity.getText().toString());
            outerJsonObject.put(Constants.PROFESSIONAL_LICENSE, edtLicense.getText().toString());
            outerJsonObject.put(Constants.STATE, stringState);
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



    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        stringState = ((TextView) view).getText().toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

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
                case R.id.edtName:
                    functions.validateName(edtName, tilName);
                    break;
                case R.id.edtEmail:
                    functions.validateEmail(edtEmail, tilEmail);
                    break;
                case R.id.edtPhone:
                    functions.validatePhone(edtPhone, tilPhone);
                    break;
                case R.id.edtCity:
                    functions.validateCity(edtCity, tilCity);
                    break;
            }
        }
    }
}
