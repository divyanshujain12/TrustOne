package com.example.deii.trustone;

/**
 * Created by deii on 12/18/2015.
 */

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

import com.example.deii.ProductFragments.AudioFragment;
import com.example.deii.Utils.PlayerInterface;
import com.example.deii.Utils.Utilities;

public class PlayAllAudioActivity extends Activity implements OnCompletionListener, SeekBar.OnSeekBarChangeListener, View.OnClickListener, PlayerInterface {


    // Media Player
    private MediaPlayer mp;
    // Handler to update UI timer, progress bar etc,.
    private Handler mHandler = new Handler();
    ;
    private long lastTime = 0;
    private Utilities utils;
    private int seekForwardTime = 5000; // 5000 milliseconds
    private int seekBackwardTime = 5000; // 5000 milliseconds
    private int currentSongIndex = 0;
    private boolean isShuffle = false;
    private boolean isRepeat = false;
    private ImageButton btnPlay, ButtonTestPrevious, ButtonTestNext;
    private ProgressBar progressBar;
    private SeekBar songProgressBar;
    private com.neopixl.pixlui.components.textview.TextView songCurrentDurationLabel;
    private String AUDIO_URL = "";
    public int CurrentAudioPos = 0;
    Intent intent = null;
    public static int length = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.play_all_activity);
        utils = new Utilities();

        AUDIO_URL = AudioFragment.model.get(0).getUrl();
        length = AudioFragment.model.size();
        btnPlay = (ImageButton) findViewById(R.id.ButtonTestPlayPause);
        songProgressBar = (SeekBar) findViewById(R.id.SeekBarTestPlay);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);


        ButtonTestPrevious = (ImageButton) findViewById(R.id.ButtonTestPrevious);
        ButtonTestPrevious.setOnClickListener(this);

        ButtonTestNext = (ImageButton) findViewById(R.id.ButtonTestNext);
        ButtonTestNext.setOnClickListener(this);

        songCurrentDurationLabel = (com.neopixl.pixlui.components.textview.TextView) findViewById(R.id.txtTotalTime);


        if (MusicService.getInstance(PlayAllAudioActivity.this) == null) {
            progressBar.setVisibility(View.VISIBLE);
            MusicService.setSong(AUDIO_URL, "", "");
            intent = new Intent(this, MusicService.class);
            intent.setAction(MusicService.ACTION_PLAY);
            startService(intent);
        } else {
            progressBar.setVisibility(View.GONE);
            updateProgressBar();
        }

    }


    public void playSong() {
        // Play song
        try {
            progressBar.setVisibility(View.VISIBLE);


            MusicService.setSong(AUDIO_URL, "", "");
            MusicService.getInstance(PlayAllAudioActivity.this).initMediaPlayer();

            // Changing Button Image to pause image
            btnPlay.setImageResource(R.drawable.audio_pause);

            // set Progress bar values
            songProgressBar.setProgress(0);
            songProgressBar.setMax(100);
            // Updating progress bar
            updateProgressBar();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    public void updateProgressBar() {
        mHandler.postDelayed(mUpdateTimeTask, 100);
    }


    /**
     * Background Runnable thread
     */

    private Runnable mUpdateTimeTask = new Runnable() {
        public void run() {
            if (MusicService.mMediaPlayer != null) {


                long totalDuration = MusicService.getInstance(PlayAllAudioActivity.this).getMusicDuration();
                long currentDuration = MusicService.getInstance(PlayAllAudioActivity.this).getCurrentPosition();

                // Displaying time completed playing
                songCurrentDurationLabel.setText("" + utils.milliSecondsToTimer(currentDuration));
               /* if (lastTime != Long.parseLong(utils.milliSecondsToTimer(currentDuration)))
                    progressBar.setVisibility(View.GONE);
                else
                    progressBar.setVisibility(View.VISIBLE);*/


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
        int totalDuration = MusicService.getInstance(PlayAllAudioActivity.this).getMusicDuration();
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
       /* mp.release();
        mHandler.removeCallbacks(mUpdateTimeTask);
        mp.stop();*/
    }

    @Override
    protected void onStop() {
        super.onStop();
       /* mHandler.removeCallbacks(mUpdateTimeTask);
        mp.stop();*/

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onClick(View view) {

        if (view == ButtonTestNext) {
            if (CurrentAudioPos != length - 1) {
                CurrentAudioPos++;
                AUDIO_URL = AudioFragment.model.get(CurrentAudioPos).getUrl();
                playSong();

            }

        } else if (view == ButtonTestPrevious) {
            if (CurrentAudioPos != 0) {
                CurrentAudioPos--;
                AUDIO_URL = AudioFragment.model.get(CurrentAudioPos).getUrl();
                playSong();

            }
        } else if (view == btnPlay) {
            if (MusicService.getInstance(PlayAllAudioActivity.this) != null) {
                if (MusicService.getInstance(PlayAllAudioActivity.this).isPlaying()) {
                    MusicService.getInstance(PlayAllAudioActivity.this).pauseMusic();
                    btnPlay.setImageResource(R.drawable.audio_play);
                } else {
                    MusicService.getInstance(PlayAllAudioActivity.this).startMusic();
                    btnPlay.setImageResource(R.drawable.audio_pause);

                }
            }
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        mHandler.removeCallbacks(mUpdateTimeTask);
       /* MusicService.getInstance(PlayAllAudioActivity.this).getMediaPlayer().stop();
        MusicService.getInstance(PlayAllAudioActivity.this).getMediaPlayer().release();*/
        MusicService.getInstance(PlayAllAudioActivity.this).resetService();
        /*if (intent != null) {
            stopService(intent);
        }*/
        finish();
    }

    @Override
    public void Played() {
        songProgressBar.setOnSeekBarChangeListener(this);
        progressBar.setVisibility(View.GONE);
        updateProgressBar();
        SetMusicPlayerOnCompleteListener();
    }

    private void SetMusicPlayerOnCompleteListener() {

        MusicService.mMediaPlayer.setOnCompletionListener(new OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                if (CurrentAudioPos != length - 1 && MusicService.mState != MusicService.State.Preparing) {
                    CurrentAudioPos++;
                    AUDIO_URL = AudioFragment.model.get(CurrentAudioPos).getUrl();
                    MusicService.mMediaPlayer.stop();
                    MusicService.mMediaPlayer.reset();
                    playSong();

                }
            }
        });
    }

    @Override
    public void error() {

    }


}

