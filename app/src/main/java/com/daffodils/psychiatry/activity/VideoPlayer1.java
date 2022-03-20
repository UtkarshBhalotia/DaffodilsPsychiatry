package com.daffodils.psychiatry.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.Nullable;

import com.daffodils.psychiatry.R;
import com.daffodils.psychiatry.helper.AppController;
import com.daffodils.psychiatry.helper.GlobalConst;

public class VideoPlayer1 extends Activity {

    Context context;
    Activity activity;
    Bundle bundle;
    String videoUrl ="";
    ProgressBar progressBar;
    VideoView videoView;
    MediaController mediaController;
    int index = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lyt_video_player);

        context = getApplicationContext();
        progressBar = new ProgressBar(context);

        Intent i = getIntent();
        videoUrl = i.getStringExtra("VideoURL");

       /* Bundle bundle = VideoPlayer1.getArguments();
        if (bundle != null) {
            videoUrl = bundle.getString("VideoURL");
        }
        */

      //  if (AppController.isConnected(activity)){
            progressBar.setVisibility(View.VISIBLE);
            videoView = findViewById(R.id.videoView);
            Uri uri = Uri.parse(videoUrl);
            videoView.setVideoURI(uri);
            mediaController = new MediaController(context);
            mediaController.setAnchorView(videoView);
            mediaController.setMediaPlayer(videoView);
            videoView.setMediaController(mediaController);
            videoView.start();
            progressBar.setVisibility(View.GONE);

       /* } else {
            Toast.makeText(context, "Please check your internet connection", Toast.LENGTH_LONG).show();
        }*/

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
                    @Override
                    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
                        videoView.setMediaController(mediaController);
                        mediaController.setAnchorView(videoView);

                    }
                });
            }
        });

        videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                Log.d("API123", "What " + what + " extra " + extra);
                return false;
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        GlobalConst.TOOLBAR_TITLE = "Video Player";
    }

}
