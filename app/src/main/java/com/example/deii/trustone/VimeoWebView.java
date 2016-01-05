package com.example.deii.trustone;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.example.deii.Utils.Constants;

/**
 * Created by deii on 12/25/2015.
 */
public class VimeoWebView extends Activity {

    private WebView webview;
    private ProgressDialog progressBar;
    private String videoID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.vimeo_web_view);

        InitViews();

    }

    private void InitViews() {

        videoID = getIntent().getStringExtra(Constants.DATA);

        this.webview = (WebView) findViewById(R.id.webView);

        WebSettings settings = webview.getSettings();
        settings.setJavaScriptEnabled(true);
        webview.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setAppCacheEnabled(true);
        webview.getSettings().setDomStorageEnabled(true);


        webview.getSettings().setPluginState(WebSettings.PluginState.ON);


        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();

        progressBar = ProgressDialog.show(VimeoWebView.this, "Please Wait...", "Loading...");

        webview.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.i("", "Processing webview url click...");
                view.loadUrl(url);
                return true;
            }

            public void onPageFinished(WebView view, String url) {
                Log.i("", "Finished loading URL: " + url);
                if (progressBar.isShowing()) {
                    progressBar.dismiss();
                }
            }

            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Log.e("", "Error: " + description);
                Toast.makeText(VimeoWebView.this, "Oh no! " + description, Toast.LENGTH_SHORT).show();

                // alertDialog.show();
            }
        });
        webview.loadUrl("http://player.vimeo.com/video/" + videoID + "?player_id=player&autoplay=1&title=0&byline=0&portrait=0&api=1&maxheight=480&maxwidth=800");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
