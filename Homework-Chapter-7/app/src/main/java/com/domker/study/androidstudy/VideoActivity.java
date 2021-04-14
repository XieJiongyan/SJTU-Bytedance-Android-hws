package com.domker.study.androidstudy;

import android.graphics.PixelFormat;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.VideoView;

public class VideoActivity extends AppCompatActivity {
    private Button buttonPlay;
    private Button buttonPause;
    private VideoView videoView;
    private SeekBar sb_normal;
    private int mediaDuration;
    private boolean isPlaying;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_video);

        buttonPause = findViewById(R.id.buttonPause);
        buttonPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isPlaying = false;
                videoView.pause();
            }
        });

        buttonPlay = findViewById(R.id.buttonPlay);
        buttonPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videoView.start();
                isPlaying = true;
                new MyThread().start();
            }
        });

        sb_normal = (SeekBar) findViewById(R.id.sb_normal);

        videoView = findViewById(R.id.videoView);
        videoView.getHolder().setFormat(PixelFormat.TRANSPARENT);
        videoView.setZOrderOnTop(true);
        videoView.setVideoPath(getVideoPath(R.raw.big_buck_bunny));
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mediaDuration = videoView.getDuration();
                sb_normal.setMax(mediaDuration);
            }
        });

        sb_normal.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.i("important", "onProgressChanged");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                Log.i("important", "onStartTrackingTouch");
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int progress = seekBar.getProgress();
                videoView.seekTo(progress);
                Log.i("important", "onStopTrackingTouch, " + progress);
            }
        });
        sb_normal.getProgress();
    }


    private String getVideoPath(int resId) {
        return "android.resource://" + this.getPackageName() + "/" + resId;
    }

    class MyThread extends Thread {
        @Override
        public void run() {
            super.run();
            while (isPlaying) {
                int currentPosition = videoView.getCurrentPosition();
                sb_normal.setProgress(currentPosition);
            }
        }
    }
}
