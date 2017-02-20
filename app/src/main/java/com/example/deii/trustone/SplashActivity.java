package com.example.deii.trustone;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.andexert.library.RippleView;
import com.example.deii.Utils.Constants;
import com.example.deii.Utils.MySharedPereference;
import com.neopixl.pixlui.components.textview.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


/**
 * Created by deii on 10/10/2015.
 */


public class SplashActivity extends AppCompatActivity  {

    @InjectView(R.id.imageView)
    ImageView imageView;
    @InjectView(R.id.txtLogIn)
    TextView txtLogIn;
    @InjectView(R.id.txtSignUp)
    TextView txtSignUp;

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
        setContentView(R.layout.splash_activity);
        ButterKnife.inject(this);

        InitViews();


    }

    private void InitViews() {
        // getSupportActionBar().hide();


    }


    @OnClick({R.id.txtLogIn, R.id.txtSignUp})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.txtLogIn:
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.txtSignUp:
                Intent intent1 = new Intent(SplashActivity.this, SignUpActivity.class);
                startActivity(intent1);
                break;
        }
    }
}
