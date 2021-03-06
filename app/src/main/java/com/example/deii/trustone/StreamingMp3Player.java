package com.example.deii.trustone;

/**
 * Created by Lenovo on 06-11-2015.
 */

import android.app.Activity;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageButton;
import android.widget.SeekBar;

import com.example.deii.Utils.CommonFunctions;
import com.example.deii.Utils.Constants;

import java.util.concurrent.TimeUnit;

public class StreamingMp3Player extends Activity implements OnClickListener, OnTouchListener, OnCompletionListener, OnBufferingUpdateListener {

    private ImageButton buttonPlayPause;
    private SeekBar seekBarProgress;
    //public EditText editTextSongURL;

    private MediaPlayer mediaPlayer;
    private int mediaFileLengthInMilliseconds; // this value contains the song duration in milliseconds. Look at getDuration() method in MediaPlayer class
    private String AUDIO_URL = "";
    private final Handler handler = new Handler();
    private CoordinatorLayout coordinatorL;
    private int currentState = 0;
    private com.neopixl.pixlui.components.textview.TextView txtTotalTime;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.streaming_mp3_player);

        initView();


    }

    /**
     * This method initialise all the views in project
     */
    private void initView() {

        AUDIO_URL = getIntent().getStringExtra(Constants.DATA);

        coordinatorL = (CoordinatorLayout) findViewById(R.id.coordinatorL);

        buttonPlayPause = (ImageButton) findViewById(R.id.ButtonTestPlayPause);
        buttonPlayPause.setOnClickListener(this);

        seekBarProgress = (SeekBar) findViewById(R.id.SeekBarTestPlay);
        seekBarProgress.setMax(99); // It means 100% .0-99
        seekBarProgress.setOnTouchListener(this);

        txtTotalTime = (com.neopixl.pixlui.components.textview.TextView) findViewById(R.id.txtTotalTime);


    }

    /**
     * Method which updates the SeekBar primary progress by current song playing position
     */
    private void primarySeekBarProgressUpdater() {
        seekBarProgress.setProgress((int) (((float) mediaPlayer.getCurrentPosition() / mediaFileLengthInMilliseconds) * 100)); // This math construction give a percentage of "was playing"/"song length"

        if (mediaPlayer.isPlaying()) {
            mediaFileLengthInMilliseconds = mediaFileLengthInMilliseconds - 1000;
            txtTotalTime.setText(String.valueOf(String.format("%02d : %02d",
                    TimeUnit.MILLISECONDS.toMinutes(mediaFileLengthInMilliseconds),
                    TimeUnit.MILLISECONDS.toSeconds(mediaFileLengthInMilliseconds) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(mediaFileLengthInMilliseconds)))));
            Runnable notification = new Runnable() {
                public void run() {
                    primarySeekBarProgressUpdater();
                }
            };
            handler.postDelayed(notification, 1000);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ButtonTestPlayPause) {
            /** ImageButton onClick event handler. Method which start/pause mediaplayer playing */
            //  mediaFileLengthInMilliseconds = mediaPlayer.getDuration(); // gets the song length in milliseconds from URL

            if (mediaPlayer == null) {
                InitializeMediaPlayer();
            }
            else if (!mediaPlayer.isPlaying()) {

                mediaPlayer.seekTo(currentState);
                seekBarProgress.setProgress(currentState);
                mediaPlayer.start();
                buttonPlayPause.setImageResource(R.drawable.audio_pause);
                primarySeekBarProgressUpdater();
            } else {
                currentState = mediaPlayer.getCurrentPosition();
                mediaPlayer.pause();
                buttonPlayPause.setImageResource(R.drawable.audio_play);
            }


        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (v.getId() == R.id.SeekBarTestPlay) {
            /** Seekbar onTouch event handler. Method which seeks MediaPlayer to seekBar primary progress position*/
            if (mediaPlayer.isPlaying()) {
                SeekBar sb = (SeekBar) v;
                int playPositionInMillisecconds = (mediaFileLengthInMilliseconds / 100) * sb.getProgress();
                mediaPlayer.seekTo(playPositionInMillisecconds);
                sb.setProgress(playPositionInMillisecconds);
            }
        }
        return false;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        /** MediaPlayer onCompletion event handler. Method which calls then song playing is complete*/
        buttonPlayPause.setImageResource(R.drawable.audio_play);
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        /** Method which updates the SeekBar secondary progress by current song loading from URL position*/
        seekBarProgress.setSecondaryProgress(percent);
    }

    private void InitializeMediaPlayer() {

        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setOnBufferingUpdateListener(this);
            mediaPlayer.setOnCompletionListener(this);
            mediaPlayer.reset();
            mediaPlayer.setDataSource(AUDIO_URL);
            mediaPlayer.prepareAsync();


            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {

                    mediaFileLengthInMilliseconds = mp.getDuration();
                    txtTotalTime.setText(String.valueOf(String.format("%02d : %02d",
                            TimeUnit.MILLISECONDS.toMinutes(mediaFileLengthInMilliseconds),
                            TimeUnit.MILLISECONDS.toSeconds(mediaFileLengthInMilliseconds) -
                                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(mediaFileLengthInMilliseconds)))));
                    //int inSecond = Integer.parseInt(String.valueOf(TimeUnit.MILLISECONDS.toSeconds(mediaFileLengthInMilliseconds)));
                    seekBarProgress.setMax(mediaFileLengthInMilliseconds);
                    mp.start();
                    primarySeekBarProgressUpdater();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            CommonFunctions.showSnackBarWithoutAction(coordinatorL, e.toString());
        }
    }
}