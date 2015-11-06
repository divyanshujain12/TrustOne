package com.example.deii.trustone;

/**
 * Created by Lenovo on 05-11-2015.
 */

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Config;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.deii.Utils.Constants;
import com.example.deii.Utils.YouTubeFailureRecoveryActivity;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayer.PlayerStyle;
import com.google.android.youtube.player.YouTubePlayerView;


public class YouTubePlayerActivity extends YouTubeFailureRecoveryActivity implements
        YouTubePlayer.OnInitializedListener {

    private static final int RECOVERY_DIALOG_REQUEST = 1;

    // YouTube player view
    private YouTubePlayerView youTubeView;
    private String VIDEO_CODE = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.you_tube_player);

        YouTubePlayerView youTubeView = (YouTubePlayerView) findViewById(R.id.youtube_view);
        youTubeView.initialize(getString(R.string.developer_key), this);
        VIDEO_CODE = getIntent().getStringExtra(Constants.DATA);
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player,
                                        boolean wasRestored) {
        if (!wasRestored) {
            player.cueVideo(/*"wKJ9KzGQq0w"*/VIDEO_CODE);
        }
    }

    @Override
    protected YouTubePlayer.Provider getYouTubePlayerProvider() {
        return (YouTubePlayerView) findViewById(R.id.youtube_view);
    }
}
