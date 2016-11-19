package com.example.deii.trustone;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import com.andexert.library.RippleView;
import com.example.deii.Utils.Constants;
import com.example.deii.Utils.MySharedPereference;
import com.neopixl.pixlui.components.textview.TextView;

import static com.example.deii.trustone.R.layout.splash_activity;


/**
 * Created by deii on 10/10/2015.
 */


public class SplashActivity extends AppCompatActivity implements RippleView.OnRippleCompleteListener {
    RippleView rippleLogIn, rippleSignUp;
    TextView txtLogIn, txtSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (!MySharedPereference.getInstance().getString(this, Constants.EMAIL_ID).contentEquals("")) {

            Intent intent = new Intent(this, NavigationDrawerActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        }
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(splash_activity);

        InitViews();


    }

    private void InitViews() {
        // getSupportActionBar().hide();


        txtLogIn = (TextView) findViewById(R.id.txtLogIn);
        txtSignUp = (TextView) findViewById(R.id.txtSignUp);

        rippleLogIn = (RippleView) findViewById(R.id.rippleLogIn);
        rippleLogIn.setOnRippleCompleteListener(this);
        rippleSignUp = (RippleView) findViewById(R.id.rippleSignUp);
        rippleSignUp.setOnRippleCompleteListener(this);


    }

    @Override
    public void onComplete(RippleView rippleView) {
        if (rippleView == rippleLogIn) {
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            Intent intent = new Intent(SplashActivity.this, SignUpActivity.class);
            startActivity(intent);
        }
    }
}
