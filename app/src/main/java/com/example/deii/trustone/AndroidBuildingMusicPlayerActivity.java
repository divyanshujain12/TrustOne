package com.example.deii.trustone;


import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.SeekBar;

import com.example.deii.Utils.Constants;
import com.example.deii.Utils.PlayerInterface;
import com.example.deii.Utils.Utilities;

public class AndroidBuildingMusicPlayerActivity extends Activity implements OnCompletionListener, SeekBar.OnSeekBarChangeListener, PlayerInterface {


    // Media Player
    private MediaPlayer mp;
    // Handler to update UI timer, progress bar etc,.
    private Handler mHandler = new Handler();
    ;

    private Utilities utils;
    private int seekForwardTime = 5000; // 5000 milliseconds
    private int seekBackwardTime = 5000; // 5000 milliseconds
    private int currentSongIndex = 0;
    private boolean isShuffle = false;
    private boolean isRepeat = false;
    private ImageButton btnPlay;
    private SeekBar songProgressBar;
    private com.neopixl.pixlui.components.textview.TextView songCurrentDurationLabel;
    private String AUDIO_URL = "";
    Intent intent = null;
    ProgressBar progressBar;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.streaming_mp3_player);


        mp = new MediaPlayer();

        utils = new Utilities();

        AUDIO_URL = getIntent().getStringExtra(Constants.DATA);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        btnPlay = (ImageButton) findViewById(R.id.ButtonTestPlayPause);
        songProgressBar = (SeekBar) findViewById(R.id.SeekBarTestPlay);
        songCurrentDurationLabel = (com.neopixl.pixlui.components.textview.TextView) findViewById(R.id.txtTotalTime);

        // Listeners
        songProgressBar.setOnSeekBarChangeListener(this); // Important
       /* mp.setOnCompletionListener(this); // Important

        playSong();
        mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mp.start();
            }
        });*/
        if (MusicService.getInstance(AndroidBuildingMusicPlayerActivity.this) == null) {
            progressBar.setVisibility(View.VISIBLE);
            MusicService.setSong(AUDIO_URL, "", "");
            intent = new Intent(this, MusicService.class);
            intent.setAction(MusicService.ACTION_PLAY);
            startService(intent);
        } else {
            progressBar.setVisibility(View.GONE);
            updateProgressBar();
        }
        btnPlay.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // check for already playing
                if (MusicService.getInstance(AndroidBuildingMusicPlayerActivity.this) != null) {
                    if (MusicService.getInstance(AndroidBuildingMusicPlayerActivity.this).isPlaying()) {
                        MusicService.getInstance(AndroidBuildingMusicPlayerActivity.this).pauseMusic();
                        btnPlay.setImageResource(R.drawable.audio_play);
                    } else {
                        MusicService.getInstance(AndroidBuildingMusicPlayerActivity.this).startMusic();
                        btnPlay.setImageResource(R.drawable.audio_pause);

                    }
                }

            }
        });
    }

    /**
     * Update timer on seekbar
     */

    public void updateProgressBar() {
        mHandler.postDelayed(mUpdateTimeTask, 100);
    }


    /**
     * Background Runnable thread
     */

    private Runnable mUpdateTimeTask = new Runnable() {
        public void run() {
            if (MusicService.mMediaPlayer != null) {
                long totalDuration = MusicService.getInstance(AndroidBuildingMusicPlayerActivity.this).getMusicDuration();
                long currentDuration = MusicService.getInstance(AndroidBuildingMusicPlayerActivity.this).getCurrentPosition();

                // Displaying time completed playing
                songCurrentDurationLabel.setText("" + utils.milliSecondsToTimer(currentDuration));

                // Updating progress bar
                int progress = (int) (utils.getProgressPercentage(currentDuration, totalDuration));
                //Log.d("Progress", ""+progress);
                songProgressBar.setProgress(progress);

                // Running this thread after 100 milliseconds
                mHandler.postDelayed(this, 100);
            }
        }
    };


    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromTouch) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        // remove message Handler from updating progress bar
        mHandler.removeCallbacks(mUpdateTimeTask);
    }


    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        mHandler.removeCallbacks(mUpdateTimeTask);
        int totalDuration = MusicService.getInstance(AndroidBuildingMusicPlayerActivity.this).getMusicDuration();
        int currentPosition = utils.progressToTimer(seekBar.getProgress(), totalDuration);

        // forward or backward to certain seconds
        MusicService.mMediaPlayer.seekTo(currentPosition);

        // update timer progress again
        updateProgressBar();
    }


    @Override
    public void onCompletion(MediaPlayer arg0) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // MusicService.getInstance(AndroidBuildingMusicPlayerActivity.this).getMediaPlayer().release();
    }

    @Override
    protected void onStop() {
        super.onStop();
       /* mHandler.removeCallbacks(mUpdateTimeTask);
        MusicService.getInstance(AndroidBuildingMusicPlayerActivity.this).getMediaPlayer().stop();*/

    }

    @Override
    public void Played() {
        songProgressBar.setOnSeekBarChangeListener(this);
        progressBar.setVisibility(View.GONE);
        updateProgressBar();
    }

    @Override
    public void error() {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        mHandler.removeCallbacks(mUpdateTimeTask);
      /*  MusicService.getInstance(AndroidBuildingMusicPlayerActivity.this).getMediaPlayer().stop();
        MusicService.getInstance(AndroidBuildingMusicPlayerActivity.this).getMediaPlayer().release();*/
        MusicService.getInstance(AndroidBuildingMusicPlayerActivity.this).resetService();
        /*if (intent != null) {
            stopService(intent);
        }*/
    }
}
