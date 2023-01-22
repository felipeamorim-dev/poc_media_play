package com.example.poc_media_play.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import com.example.poc_media_play.R;
import java.io.File;
import java.util.Objects;

public class PlayMusicActivity extends AppCompatActivity {

    private MediaPlayer mMediaPlay;
    private ImageButton btnPlay;
    private TextView tvDuration;
    private TextView tvTitle;
    private TextView tvCurrentTime;
    private SeekBar mSeekBar;
    private Runnable runnable;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_music);
        init();
        handler = new Handler();

        String title = getIntent().getStringExtra("title");

        String fileMusicPath = new File(getApplicationContext().getFilesDir(), "assets/" + title).getPath();
        Uri uri = Uri.parse(fileMusicPath);
        mMediaPlay = MediaPlayer.create(getApplicationContext(), uri);

        setAttInit(title, mMediaPlay.getDuration());

        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mMediaPlay.seekTo(progress);
                    seekBar.setProgress(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        btnPlay.setOnClickListener(view -> {
            if(mMediaPlay.isPlaying()) {
                int icon = R.drawable.pause_circle_outline;
                ImageButton ib = (ImageButton) view;
                ib.setImageResource(icon);
                stopMusic();
            } else if (!mMediaPlay.isPlaying()) {
                int icon = R.drawable.play_circle_outline;
                ImageButton ib = (ImageButton) view;
                ib.setImageResource(icon);
                playMusic();
            }
        });

    }

    private void init() {
        tvDuration = findViewById(R.id.tv_max_time);
        btnPlay = findViewById(R.id.btnStatusPlay);
        tvTitle = findViewById(R.id.tvMusicTitle);
        mSeekBar = findViewById(R.id.sbProgressAudio);
        tvCurrentTime = findViewById(R.id.tv_current_time);
    }

    private void setAttInit(String title, int duration) {
        mSeekBar.setMax(duration);
        tvTitle.setText(title);
        tvDuration.setText(calcTime(duration));
    }

    private String calcTime(int duration) {
        int minutes = (duration / 1000) / 60;
        int seconds = (duration / 1000) % 60;

        return String.format("%s:%s", minutes, seconds);
    }

    private void updateSeekBar() {
        int currentPos = mMediaPlay.getCurrentPosition();
        mSeekBar.setProgress(currentPos);
        tvCurrentTime.setText(calcTime(currentPos));
        runnable = this::updateSeekBar;
        handler.postDelayed(runnable, 1000);
    }

    @Override
    protected void onStart() {
        super.onStart();
        playMusic();
        updateSeekBar();
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopMusic();
    }

    private void playMusic() {
        if (Objects.nonNull(mMediaPlay)) {
            mMediaPlay.start();
        }
    }

    private void stopMusic() {
        if (mMediaPlay.isPlaying()) {
            mMediaPlay.pause();
        }
    }
}